package com.example.go.memoexpensesapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.actioncreator.LoginActionCreator
import com.example.go.memoexpensesapplication.component.DaggerLoginComponent
import com.example.go.memoexpensesapplication.databinding.FragmentLoginBinding
import com.example.go.memoexpensesapplication.navigator.FragmentLoginNavigator
import com.example.go.memoexpensesapplication.viewmodel.FragmentLoginViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class LoginFragment : Fragment() {
    private lateinit var viewModel: FragmentLoginViewModel
    private lateinit var binding: FragmentLoginBinding
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var actionCreator: LoginActionCreator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginComponent = DaggerLoginComponent.create()
        loginComponent.inject(this)

        activity?.run {
            viewModel = ViewModelProviders.of(this)[FragmentLoginViewModel::class.java]
            viewModel.inject(loginComponent)
            if (this is FragmentLoginNavigator) {
                viewModel.setNavigator(this)
            } else {
                throw RuntimeException("$context must implement FragmentLoginNavigator")
            }
        } ?: throw RuntimeException("Invalid Activity")
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

        viewModel.authenticationFail
            .subscribe {
                Toast.makeText(
                    context,
                    getString(R.string.fragment_login_authentication_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }.addTo(compositeDisposable)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setTitle(R.string.fragment_login_title)
            setDisplayHomeAsUpEnabled(false)
        }
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
        fun newInstance() = LoginFragment()
    }
}
