package com.example.go.memoexpensesapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.actioncreator.LoginActionCreator
import com.example.go.memoexpensesapplication.activity.MainActivity
import com.example.go.memoexpensesapplication.databinding.FragmentLoginBinding
import com.example.go.memoexpensesapplication.di.ViewModelFactory
import com.example.go.memoexpensesapplication.viewmodel.FragmentLoginViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class LoginFragment : Fragment() {
    @Inject
    lateinit var actionCreator: LoginActionCreator

    @Inject
    lateinit var factory: ViewModelFactory<FragmentLoginViewModel>

    private val viewModel: FragmentLoginViewModel by activityViewModels { factory }
    private lateinit var binding: FragmentLoginBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            (this as AppCompatActivity).supportActionBar?.show()
            if (this is MainActivity) {
                loginComponent.inject(this@LoginFragment)
                viewModel.setNavigator(this)
            } else throw RuntimeException("$this must be MainActivity")
        } ?: throw RuntimeException("Invalid Activity")

        viewModel.createUserFail
            .subscribe {
                Toast.makeText(
                    context,
                    getString(R.string.fragment_login_create_user_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }.addTo(compositeDisposable)
        viewModel.authenticationFail
            .subscribe {
                Toast.makeText(
                    context,
                    getString(R.string.fragment_login_authentication_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }.addTo(compositeDisposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.loginFragment = this
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
