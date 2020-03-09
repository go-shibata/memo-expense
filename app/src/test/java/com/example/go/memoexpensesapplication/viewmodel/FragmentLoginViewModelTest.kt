package com.example.go.memoexpensesapplication.viewmodel

import com.example.go.memoexpensesapplication.RxImmediateSchedulerRule
import com.example.go.memoexpensesapplication.action.LoginAction
import com.example.go.memoexpensesapplication.dispatcher.LoginDispatcher
import com.example.go.memoexpensesapplication.model.User
import com.example.go.memoexpensesapplication.navigator.FragmentLoginNavigator
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FragmentLoginViewModelTest {

    @get:Rule
    val rule = RxImmediateSchedulerRule()

    @Spy
    val dispatcher = LoginDispatcher()

    @Mock
    lateinit var navigator: FragmentLoginNavigator
    lateinit var viewModel: FragmentLoginViewModel

    @Before
    fun setUp() {
        viewModel = FragmentLoginViewModel(dispatcher)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun occurredLoginAction_confirmNavigatorCalled() {
        val user = User("user1", "mail2", "uid3")
        viewModel.setNavigator(navigator)

        dispatcher.dispatch(LoginAction.Login(user))
        argumentCaptor<User>().apply {
            verify(navigator, times(1)).onLoggedIn(capture())
            assertThat(firstValue)
                .extracting("name", "mail", "uid")
                .containsExactly("user1", "mail2", "uid3")
        }
    }

    @Test
    fun occurredAuthenticationFailAction_confirmPostFlow() {
        val exception = Exception()
        val subscriber = viewModel.authenticationFail.test()
        dispatcher.dispatch(LoginAction.AuthenticationFail(exception))
        subscriber
            .assertNoErrors()
            .assertValue(exception)
    }

    @Test
    fun occurredAutoLoginFaailedAction_confirmNavigatorCalled() {
        viewModel.setNavigator(navigator)

        dispatcher.dispatch(LoginAction.AutoLoginFail())
        verify(navigator, times(1)).onAutoLoginFailed()
    }
}