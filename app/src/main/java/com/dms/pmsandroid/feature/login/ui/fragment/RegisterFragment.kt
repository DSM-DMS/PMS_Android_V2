package com.dms.pmsandroid.feature.login.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.dms.pmsandroid.R
import com.dms.pmsandroid.base.BaseFragment
import com.dms.pmsandroid.databinding.FragmentRegisterBinding
import com.dms.pmsandroid.feature.login.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(R.layout.fragment_register) {

    private val vm: RegisterViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.vm = vm
        observeInputData()
    }

    private fun observeInputData() {
        vm.userEmail.observe(viewLifecycleOwner, Observer {
            vm.nEmptyEmail.value = !it.isNullOrEmpty()
            checkDoneRegister()
        })

        vm.userName.observe(viewLifecycleOwner, Observer {
            vm.nEmptyName.value = !it.isNullOrEmpty()
            checkDoneRegister()
        })

        vm.userPassword.observe(viewLifecycleOwner, Observer {
            vm.nEmptyPassword.value = !it.isNullOrEmpty()&&it.length>8&&it.length<20
            checkDoneRegister()
        })

        vm.userPasswordCheck.observe(viewLifecycleOwner, Observer {
            if (vm.userPasswordCheck.value != null && vm.userPassword.value != null) {
                vm.samePassword.value = vm.userPassword == vm.userPasswordCheck
            }
            checkDoneRegister()
        })


    }

    private fun checkDoneRegister() {
        vm.doneRegister.value =
            vm.nEmptyEmail.value!! && vm.nEmptyName.value!! && vm.nEmptyPassword.value!! && vm.samePassword.value!!
    }

}