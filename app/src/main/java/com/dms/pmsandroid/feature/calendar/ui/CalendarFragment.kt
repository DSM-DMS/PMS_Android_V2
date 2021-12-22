package com.dms.pmsandroid.feature.calendar.ui

import android.os.Bundle
import android.view.View
import com.dms.pmsandroid.R
import com.dms.pmsandroid.base.BaseFragment
import com.dms.pmsandroid.base.Event
import com.dms.pmsandroid.base.EventObserver
import com.dms.pmsandroid.databinding.FragmentCalendarBinding
import com.dms.pmsandroid.feature.calendar.CalendarDatePickerDialog
import com.dms.pmsandroid.feature.calendar.ui.decorator.*
import com.dms.pmsandroid.feature.calendar.viewmodel.CalendarViewModel
import com.jakewharton.rxbinding4.view.clicks
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
//todo 달 이동구현 & 이동시 점 찍기
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar),
    OnDateSelectedListener, OnMonthChangedListener {

    override val vm: CalendarViewModel by viewModel()

    private val alreadySetMonths = HashMap<Int, Boolean>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startLandingCalendarView()
        vm.loadSchedules()

        setDialog()
    }

    override fun observeEvent() {
        vm.run {
            doneEventsSetting.observe(viewLifecycleOwner, {
                drawEventDots(selectedDate.value!!.month + 1)
            })
            selectedDate.observe(viewLifecycleOwner, {
                val plusMonth = CalendarDay.from(it.year, it.month + 1, it.day)
                selectedEventModel.value = events.value?.get(plusMonth)
            })
            updateCurrentDate.observe(viewLifecycleOwner, EventObserver {
                val date = vm.selectedDate.value
                binding.calendarView.run{
                    currentDate = date
                    selectedDate = date
                }
                drawEventDots(date!!.month + 1)
            })
        }
    }

    private fun startLandingCalendarView() {
        binding.run {
            calendarShimmerContainer.startShimmer()
            calendarEventTv.text = getString(R.string.loading_events)
        }
        val currentDate = CalendarDay.today()
        vm.run {
            selectedDate.value = currentDate
            updateCurrentDate.value = Event(true)
        }
        setCalendarViewDecorator()
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        vm.selectedDate.value = date
    }

    override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay?) {
        val month = (date?.month ?: 0) + 1
        vm.showingMonth.value = date
        if (alreadySetMonths[month] == false) {
            drawEventDots(month)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        alreadySetMonths.clear()
    }

    private fun drawEventDots(month: Int) {
        val events = vm.events.value ?: return
        val dates = events.keys
        val decorators = ArrayList<EventDecorator>()

        Observable.fromIterable(dates)
            .filter { key ->
                alreadySetMonths[key.month] == false || isBeforeOrCurrentOfNextMonth(
                    key.month,
                    month
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .filter { events[it] != null }
            .subscribe({
                decorators.add(
                    EventDecorator(
                        it,
                        events[it]!!.eventTypes,
                        binding.calendarView.context
                    )
                )
            }, {
                binding.calendarShimmerContainer.hideShimmer()
            }, {
                binding.calendarView.addDecorators(decorators)
                binding.calendarShimmerContainer.hideShimmer()
                markAlreadySetMonths(month)
            })
    }

    private fun isBeforeOrCurrentOfNextMonth(mappingMonth: Int, selectedMonth: Int):Boolean =
        mappingMonth == selectedMonth || mappingMonth == selectedMonth - 1 || mappingMonth == selectedMonth + 1

    private fun markAlreadySetMonths(month: Int) {
        alreadySetMonths[month] = true
        alreadySetMonths[month + 1] = true
        alreadySetMonths[month - 1] = true
    }

    private val helperDialog by lazy {
        CalendarHelperDialog(vm)
    }

    private val datePickerDialog by lazy {
        CalendarDatePickerDialog(vm)
    }

    private fun setDialog() {
        setShowDatePickerDialog()
        setShowCalendarHelperDialog()
    }

    private fun setShowCalendarHelperDialog() {
        binding.calendarHelperTv.clicks().debounce(200, TimeUnit.MILLISECONDS).subscribe {
            helperDialog.show(requireActivity().supportFragmentManager, "HelperDialog")
        }
    }

    private fun setShowDatePickerDialog() {
        binding.calendarMonthTv.clicks().debounce(200, TimeUnit.MILLISECONDS).subscribe {
            datePickerDialog.show(requireActivity().supportFragmentManager, "CalendarDatePickerDialog")
        }
    }

    private fun setCalendarViewDecorator() {
        binding.calendarView.run {
            topbarVisible = false
            setWeekDayTextAppearance(R.style.saturdayColor)
            setDateSelected(currentDate, true)
            setOnDateChangedListener(this@CalendarFragment)
            setOnMonthChangedListener(this@CalendarFragment)
        }
        addDecorators()
    }

    private fun addDecorators() {
        binding.calendarView.addDecorators(
            DayDecorator(requireContext()),
            SaturdayDecorator(requireContext()),
            SundayDecorator(requireContext()),
            SelectedDayDecorator(requireContext())
        )
    }
}