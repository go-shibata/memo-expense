package com.example.go.memoexpensesapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.go.memoexpensesapplication.actioncreator.LoginActionCreator
import com.example.go.memoexpensesapplication.component.DaggerLoginComponent
import com.example.go.memoexpensesapplication.databinding.FragmentLoginBinding
import com.example.go.memoexpensesapplication.module.LoginModule
import com.example.go.memoexpensesapplication.navigator.FragmentLoginNavigator
import com.example.go.memoexpensesapplication.viewmodel.FragmentLoginViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class LoginFragment : Fragment() {
    private lateinit var viewModel: FragmentLoginViewModel
    private lateinit var binding: FragmentLoginBinding
    private lateinit var navigator: FragmentLoginNavigator
    private val compositeDisposable = CompositeDisposable()
    @Inject
    lateinit var actionCreator: LoginActionCreator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginComponent = DaggerLoginComponent.builder()
            .loginModule(LoginModule())
            .build()
        loginComponent.inject(this)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FragmentLoginViewModel::class.java]
        viewModel.inject(loginComponent)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onStart() {
        super.onStart()
        actionCreator.checkLogin()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.loginFragment = this

        viewModel.login
            .subscribe { user -> navigator.onLoggedIn(user) }
            .addTo(compositeDisposable)
        viewModel.authenticationFail
            .subscribe {
                Toast.makeText(
                    context,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }.addTo(compositeDisposable)

        return binding.root
    }

    fun setNavigator(navigator: FragmentLoginNavigator) {
        this.navigator = navigator
    }

    fun onClickCreateUser() {
        val mail = viewModel.mail.value ?: return
        val password = viewModel.password.value ?: return
        actionCreator.createUser(mail, password)
    }

    fun onClickLogin() {
        val mail = viewModel.mail.value ?: return
        val password = viewModel.password.value ?: return
        actionCreator.login(mail, password)
    }

    companion object {
        @JvmStatic
        fun newInstance(navigator: FragmentLoginNavigator) =
            LoginFragment().apply {
                setNavigator(navigator)
            }
    }
}
