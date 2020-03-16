package com.example.go.memoexpensesapplication.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.go.memoexpensesapplication.actioncreator.LoginActionCreator
import com.example.go.memoexpensesapplication.databinding.FragmentSplashBinding
import com.example.go.memoexpensesapplication.di.ViewModelFactory
import com.example.go.memoexpensesapplication.model.User
import com.example.go.memoexpensesapplication.view.activity.MainActivity
import com.example.go.memoexpensesapplication.viewmodel.FragmentSplashViewModel
import javax.inject.Inject

class SplashFragment : Fragment(), FragmentSplashViewModel.FragmentSplashNavigator {

    @Inject
    lateinit var actionCreator: LoginActionCreator

    @Inject
    lateinit var factory: ViewModelFactory<FragmentSplashViewModel>

    private val viewModel: FragmentSplashViewModel by viewModels { factory }
    private lateinit var binding: FragmentSplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            (this as AppCompatActivity).supportActionBar?.hide()
            if (this is MainActivity) {
                loginComponent.inject(this@SplashFragment)
            } else throw RuntimeException("$this must be MainActivity")
        } ?: throw RuntimeException("Invalid Activity")

        viewModel.setSplashNavigator(this)
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

    override fun onAutoLoginFailed() {
        view?.findNavController()?.navigate(
            SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        )
    }

    override fun onLoggedIn(user: User) {
        view?.findNavController()?.navigate(
            SplashFragmentDirections.actionSplashFragmentToMainFragment(user)
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = SplashFragment()
    }
}
