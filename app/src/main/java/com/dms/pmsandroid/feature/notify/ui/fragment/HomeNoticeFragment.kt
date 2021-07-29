package com.dms.pmsandroid.feature.notify.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dms.pmsandroid.R
import com.dms.pmsandroid.base.BaseFragment
import com.dms.pmsandroid.databinding.FragmentHomeNoticeBinding
import com.dms.pmsandroid.feature.notify.adapter.HomeAdapter
import com.dms.pmsandroid.feature.notify.viewmodel.NotifyViewModel
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.concurrent.TimeUnit

class HomeNoticeFragment : BaseFragment<FragmentHomeNoticeBinding>(R.layout.fragment_home_notice) {
    override val vm: NotifyViewModel by sharedViewModel()

    private val homeAdapter by lazy {
        HomeAdapter(vm)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeLayoutManager = LinearLayoutManager(binding.homeRv.context)
        homeLayoutManager.orientation = RecyclerView.VERTICAL
        binding.homeRv.run {
            adapter = homeAdapter
            layoutManager = homeLayoutManager
        }
        vm.getHomeNoticeList(0)
    }
    override fun observeEvent() {
        vm.homeList.observe(viewLifecycleOwner,{
            homeAdapter.setItems(it)
        })
        binding.homeEt.textChanges().debounce(500, TimeUnit.MILLISECONDS).map { it.toString() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isNotBlank()) {
                    vm.searchHome(it)
                    binding.homePageLl.visibility = View.GONE
                } else {
                    vm.resetHomePage()
                    vm.getHomeNoticeList(0)
                    binding.homePageLl.visibility = View.VISIBLE
                }
            }
    }
}