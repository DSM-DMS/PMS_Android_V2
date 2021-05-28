package com.dms.pmsandroid.feature.login.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.dms.pmsandroid.R
import com.dms.pmsandroid.base.BaseActivity
import com.dms.pmsandroid.databinding.ActivityLoginBinding
import com.dms.pmsandroid.feature.login.ui.fragment.RegisterFragment
import com.dms.pmsandroid.feature.login.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val vm :LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.vm = vm
        observeInput()
        observeToast()
        observeRegister()
    }

    private fun observeInput(){
        vm.userEmail.observe(this, Observer {
            vm.emailDone.value = !it.isNullOrBlank()
        })
        vm.userPassword.observe(this, Observer{
            vm.passwordDone.value = !it.isNullOrBlank()
        })
        vm.doneInput.value = vm.emailDone.value!!&&vm.passwordDone.value!!
    }

    private fun observeToast(){
        vm.toastMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun observeRegister(){
        vm.needRegister.observe(this, Observer {
            if(it){
                startRegister()
            }
        })
    }

    private fun startRegister(){
        binding.loginPage.visibility = View.VISIBLE
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(R.anim.silde_in_up,R.anim.slide_out_up)
        fragmentManager.add(R.id.login_page,RegisterFragment()).commit()
        vm.needRegister.value = false
    }
}