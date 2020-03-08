package com.example.go.memoexpensesapplication.actioncreator

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.go.memoexpensesapplication.Application
import com.example.go.memoexpensesapplication.action.LoginAction
import com.example.go.memoexpensesapplication.dispatcher.LoginDispatcher
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nhaarman.mockitokotlin2.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, sdk = [Build.VERSION_CODES.P])
class LoginActionCreatorTest {

    private val mAuth: FirebaseAuth = mock()
    private val dispatcher: LoginDispatcher = mock()
    lateinit var actionCreator: LoginActionCreator

    @Before
    fun setUp() {
        actionCreator = LoginActionCreator(mAuth, dispatcher)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun checkLogin_currentUserIsNotNull_confirmDispatchLoginAction() {
        val uid = "uid1"
        val user = mock<FirebaseUser>()
        whenever(user.uid).thenReturn(uid)
        whenever(mAuth.currentUser).thenReturn(user)

        actionCreator.checkLogin()
        verify(dispatcher, times(1)).dispatch(any<LoginAction.Login>())
        verify(dispatcher, never()).dispatch(any<LoginAction.AutoLoginFail>())
    }

    @Test
    fun checkLogin_currentUserIsNull_confirmNotDispatchLoginAction() {
        whenever(mAuth.currentUser).thenReturn(null)

        actionCreator.checkLogin()
        verify(dispatcher, never()).dispatch(any<LoginAction.Login>())
        verify(dispatcher, times(1)).dispatch(any<LoginAction.AutoLoginFail>())
    }

    @Test
    fun createUser_whenSuccess_confirmDispatchLoginAction() {
        val mail = "mail1"
        val password = "password1"
        val uid = "uid1"
        val user = mock<FirebaseUser>()
        val task = Tasks.forResult(mock<AuthResult>())
        whenever(user.uid).thenReturn(uid)
        whenever(mAuth.currentUser).thenReturn(user)
        whenever(mAuth.createUserWithEmailAndPassword(mail, password)).thenReturn(task)

        actionCreator.createUser(mail, password)
        verify(dispatcher, times(1)).dispatch(any<LoginAction.Login>())
        verify(dispatcher, never()).dispatch(any<LoginAction.AuthenticationFail>())
    }

    @Test
    fun createUser_whenSuccess_withCurrentUserNull_confirmDispatchAuthenticationFailAction() {
        val mail = "mail1"
        val password = "password1"
        val task = Tasks.forResult(mock<AuthResult>())
        whenever(mAuth.currentUser).thenReturn(null)
        whenever(mAuth.createUserWithEmailAndPassword(mail, password)).thenReturn(task)

        actionCreator.createUser(mail, password)
        verify(dispatcher, never()).dispatch(any<LoginAction.Login>())
        verify(dispatcher, times(1)).dispatch(any<LoginAction.AuthenticationFail>())
    }

    @Test
    fun createUser_whenFailure_confirmDispatchAuthenticationFailAction() {
        val mail = "mail1"
        val password = "password1"
        val task = Tasks.forException<AuthResult>(Exception())
        whenever(mAuth.createUserWithEmailAndPassword(mail, password)).thenReturn(task)

        actionCreator.createUser(mail, password)
        verify(dispatcher, never()).dispatch(any<LoginAction.Login>())
        verify(dispatcher, times(1)).dispatch(any<LoginAction.AuthenticationFail>())
    }

    @Test
    fun login_whenSuccess_confirmDispatchLoginAction() {
        val mail = "mail1"
        val password = "password1"
        val uid = "uid1"
        val user = mock<FirebaseUser>()
        val task = Tasks.forResult(mock<AuthResult>())
        whenever(user.uid).thenReturn(uid)
        whenever(mAuth.currentUser).thenReturn(user)
        whenever(mAuth.signInWithEmailAndPassword(mail, password)).thenReturn(task)

        actionCreator.login(mail, password)
        verify(dispatcher, times(1)).dispatch(any<LoginAction.Login>())
        verify(dispatcher, never()).dispatch(any<LoginAction.AuthenticationFail>())
    }

    @Test
    fun login_whenSuccess_withCurrentUserNull_confirmDispatchAuthenticationFailAction() {
        val mail = "mail1"
        val password = "password1"
        val task = Tasks.forResult(mock<AuthResult>())
        whenever(mAuth.currentUser).thenReturn(null)
        whenever(mAuth.signInWithEmailAndPassword(mail, password)).thenReturn(task)

        actionCreator.login(mail, password)
        verify(dispatcher, never()).dispatch(any<LoginAction.Login>())
        verify(dispatcher, times(1)).dispatch(any<LoginAction.AuthenticationFail>())
    }

    @Test
    fun login_whenFailure_confirmDispatchAuthenticationFailAction() {
        val mail = "mail1"
        val password = "password1"
        val task = Tasks.forException<AuthResult>(Exception())
        whenever(mAuth.signInWithEmailAndPassword(mail, password)).thenReturn(task)

        actionCreator.login(mail, password)
        verify(dispatcher, never()).dispatch(any<LoginAction.Login>())
        verify(dispatcher, times(1)).dispatch(any<LoginAction.AuthenticationFail>())
    }
}