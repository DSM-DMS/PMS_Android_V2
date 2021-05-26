package com.dms.pmsandroid.di.register

import com.dms.pmsandroid.data.remote.login.LoginApiProvider
import com.dms.pmsandroid.feature.login.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val registerModule = module {
    single { LoginApiProvider() }

    viewModel { RegisterViewModel(get()) }
}