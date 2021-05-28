package com.dms.pmsandroid.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dms.pmsandroid.data.local.SharedPreferenceStorage
import com.dms.pmsandroid.data.remote.login.LoginApiProvider
import com.dms.pmsandroid.feature.login.model.LoginRequest

class MainViewModel(
    private val loginApiProvider: LoginApiProvider,
    private val sharedPreferenceStorage: SharedPreferenceStorage
) : ViewModel() {

    val tabSelectedItem = MutableLiveData<Int>()

    val needToLogin = MutableLiveData<Boolean>()

    fun checkLogin() {
        val email = sharedPreferenceStorage.getInfo("user_email")
        val password = sharedPreferenceStorage.getInfo("user_password")

        if (email.isNotBlank() && password.isNotBlank()) {
            doLogin(email, password)
        } else {
            needToLogin.value = true
        }
    }

    private fun doLogin(email: String, password: String) {
        val request = LoginRequest(email, password)
        loginApiProvider.loginApi(request).subscribe { response ->
            when (response.code()) {
                200 -> {
                    sharedPreferenceStorage.saveInfo(response.body()!!.accessToken,"access_token")
                }
                else -> {
                    needToLogin.value = true
                }
            }
        }
    }

}