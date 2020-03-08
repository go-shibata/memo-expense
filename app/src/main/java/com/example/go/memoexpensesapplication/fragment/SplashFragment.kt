package com.example.go.memoexpensesapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.go.memoexpensesapplication.actioncreator.LoginActionCreator
import com.example.go.memoexpensesapplication.activity.MainActivity
import com.example.go.memoexpensesapplication.databinding.FragmentSplashBinding
import com.example.go.memoexpensesapplication.di.ViewModelFactory
import com.example.go.memoexpensesapplication.viewmodel.FragmentLoginViewModel
import javax.inject.Inject

class SplashFragment : Fragment() {

    @Inject
    lateinit var actionCreator: LoginActionCreator

    @Inject
    lateinit var factory: ViewModelFactory<FragmentLoginViewModel>

    private val viewModel: FragmentLoginViewModel by activityViewModels { factory }
    private lateinit var binding: FragmentSplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            (this as AppCompatActivity).supportActionBar?.hide()
            if (this is MainActivity) {
                loginComponent.inject(this@SplashFragment)
                viewModel.setNavigator(this)
            } else throw RuntimeException("$this must be MainActivity")
        } ?: throw RuntimeException("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        actionCreator.checkLogin()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SplashFragment()
    }
}
