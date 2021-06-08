package com.dms.pmsandroid.feature.introduce.ui.fragment

import android.os.Bundle
import android.view.View
import com.dms.pmsandroid.R
import com.dms.pmsandroid.base.BaseFragment
import com.dms.pmsandroid.databinding.FragmentIntroduceBinding
import com.dms.pmsandroid.feature.introduce.viewmodel.MainIntroViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class IntroduceFragment : BaseFragment<FragmentIntroduceBinding>(R.layout.fragment_introduce) {

    private val vm : MainIntroViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.vm = vm
    }
}
