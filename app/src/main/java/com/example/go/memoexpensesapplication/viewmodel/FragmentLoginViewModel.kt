package com.example.go.memoexpensesapplication.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.go.memoexpensesapplication.model.User
import com.example.go.memoexpensesapplication.navigator.ListFragmentNavigator
import com.google.firebase.auth.FirebaseAuth

class FragmentLoginViewModel(app: Application) : AndroidViewModel(app) {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var navigator: ListFragmentNavigator? = null

    val mail: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()

    fun setNavigator(navigator: ListFragmentNavigator) {
        this.navigator = navigator
    }

    fun onCheckLogin() {
        val currentUser = mAuth.currentUser
        currentUser?.let {
            navigator?.onLoggedIn(User(currentUser))
        }
    }

    fun onCreateUser() {
        val strEmail = mail.value ?: run {
            return
        }
        val strPassword = password.value ?: run {
            return
        }

        mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) { // Sign in success, update UI with the signed-in user's information
                    mAuth.currentUser?.let { user ->
                        navigator?.onLoggedIn(User(user))
                    }
                }

                Toast.makeText(
                    getApplication(),
                    "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun onLogin() {
        val strEmail = mail.value ?: run {
            return
        }
        val strPassword = password.value ?: run {
            return
        }

        mAuth.signInWithEmailAndPassword(strEmail, strPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mAuth.currentUser?.let { user ->
                        navigator?.onLoggedIn(User(user))
                    }
                }

                Toast.makeText(
                    getApplication(),
                    "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}