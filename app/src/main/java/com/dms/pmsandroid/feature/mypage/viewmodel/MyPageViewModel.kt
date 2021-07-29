package com.dms.pmsandroid.feature.mypage.viewmodel

import android.app.Dialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dms.pmsandroid.base.BaseDiaog
import com.dms.pmsandroid.data.local.SharedPreferenceStorage
import com.dms.pmsandroid.data.remote.mypage.MyPageApiImpl

class MyPageViewModel(
    private val myPageApiImpl: MyPageApiImpl,
    private val sharedPreferenceStorage: SharedPreferenceStorage
) : ViewModel() {
    val startDialog = MutableLiveData<Boolean>(false)
    val toastLiveData = MutableLiveData<String>()


}