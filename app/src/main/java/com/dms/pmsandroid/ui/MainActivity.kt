package com.dms.pmsandroid.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dms.pmsandroid.R
import com.dms.pmsandroid.base.BaseActivity
import com.dms.pmsandroid.databinding.ActivityMainBinding
import com.dms.pmsandroid.feature.calendar.ui.CalendarFragment
import com.dms.pmsandroid.feature.introduce.ui.activity.IntroduceClubActivity
import com.dms.pmsandroid.feature.introduce.ui.activity.IntroduceCompanyActivity
import com.dms.pmsandroid.feature.introduce.ui.activity.IntroduceDeveloperActivity
import com.dms.pmsandroid.feature.introduce.ui.fragment.IntroduceFragment
import com.dms.pmsandroid.feature.login.ui.activity.LoginActivity
import com.dms.pmsandroid.feature.meal.fragment.MealFragment
import com.dms.pmsandroid.feature.notify.ui.activity.GalleryDetailActivity
import com.dms.pmsandroid.feature.notify.ui.activity.NoticeDetailActivity
import com.dms.pmsandroid.feature.notify.ui.fragment.NotifyFragment
import com.dms.pmsandroid.feature.mypage.ui.fragment.MyPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override val vm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_PmsAndroid)
        super.onCreate(savedInstanceState)
        binding.mainBottomNavigation.setOnNavigationItemSelectedListener(itemSelectedListener)
    }

    override fun onStart() {
        super.onStart()
        initFragment()
    }

    override fun onResume() {
        super.onResume()
        vm.checkLogin()
    }

    private fun startLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }


    fun startDeveloper() {
        val devintent = Intent(this, IntroduceDeveloperActivity::class.java)
        startActivity(devintent)
    }

    fun startCompany() {
        Toast.makeText(this,"아직 준비중 입니다",Toast.LENGTH_SHORT).show()
        //todo 취업처 api 나오면 startActivity로 넘어가기
        //val workintent = Intent(this, IntroduceCompanyActivity::class.java)
        //startActivity(workintent)
    }

    fun startClub() {
        val clubintent = Intent(this, IntroduceClubActivity::class.java)
        startActivity(clubintent)
    }

    fun startGalleryDetail(id: Int) {
        val galleryIntent = Intent(this, GalleryDetailActivity::class.java)
        galleryIntent.putExtra("id", id)
        startActivity(galleryIntent)
    }

    fun startNoticeDetail(id: Int, title: String) {
        val noticeIntent = Intent(this, NoticeDetailActivity::class.java)
        noticeIntent.putExtra("id", id)
        noticeIntent.putExtra("title", title)
        startActivity(noticeIntent)
    }

    private val itemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            vm.tabSelectedItem.value = item.itemId
            true
        }
    private val calendarFragment = CalendarFragment()
    private val introduceFragment = IntroduceFragment()
    private val mealFragment = MealFragment()
    private val notifyFragment = NotifyFragment()
    private val mypageFragment = MyPageFragment()

    private fun initFragment() {
        supportFragmentManager.beginTransaction().run {
            add(R.id.main_container, calendarFragment)
            add(R.id.main_container, introduceFragment)
            add(R.id.main_container, mealFragment)
            add(R.id.main_container, notifyFragment)
            add(R.id.main_container, mypageFragment)
        }.commit()
        resetFragment()
    }

    private fun resetFragment() {
        supportFragmentManager.beginTransaction().run {
            hide(calendarFragment)
            hide(introduceFragment)
            hide(mealFragment)
            hide(notifyFragment)
            hide(mypageFragment)
        }.commit()
        changeFragment(vm.activeFragment?:calendarFragment)
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(vm.activeFragment?:calendarFragment).show(fragment).commit()
        vm.activeFragment = fragment
    }

    override fun observeEvent() {
        vm.run {
            tabSelectedItem.observe(this@MainActivity, { id ->
                when (id) {
                    R.id.menu_calendar_it -> {
                        changeFragment(calendarFragment)
                    }
                    R.id.menu_info_it -> {
                        changeFragment(introduceFragment)
                    }
                    R.id.menu_meal_it -> {
                        changeFragment(mealFragment)
                    }
                    R.id.menu_mypage_it -> {
                        changeFragment(mypageFragment)
                    }
                    R.id.menu_notify_it -> {
                        changeFragment(notifyFragment)
                    }
                }
            })
            needToLogin.observe(this@MainActivity, {
                if (it) {
                    startLogin()
                    vm.needToLogin.value = false
                }
            })
        }
    }

    override fun onStop() {
        supportFragmentManager.beginTransaction().run {
            remove(calendarFragment)
            remove(introduceFragment)
            remove(mealFragment)
            remove(notifyFragment)
            remove(mypageFragment)
        }.commit()
        super.onStop()
    }


}