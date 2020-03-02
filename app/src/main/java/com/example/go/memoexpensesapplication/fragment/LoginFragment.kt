package com.example.go.memoexpensesapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.go.memoexpensesapplication.databinding.FragmentLoginBinding
import com.example.go.memoexpensesapplication.viewmodel.FragmentLoginViewModel

class LoginFragment : Fragment() {
    private lateinit var viewModel: FragmentLoginViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        viewModel.onCheckLogin()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    fun setViewModel(viewModel: FragmentLoginViewModel) {
        this.viewModel = viewModel
    }

    companion object {
        @JvmStatic
        fun newInstance(viewModel: FragmentLoginViewModel) =
            LoginFragment().apply {
                setViewModel(viewModel)
            }
    }
}
