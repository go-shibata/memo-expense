package com.example.go.memoexpensesapplication.viewmodel

import com.example.go.memoexpensesapplication.RxImmediateSchedulerRule
import com.example.go.memoexpensesapplication.action.MainAction
import com.example.go.memoexpensesapplication.dispatcher.MainDispatcher
import com.example.go.memoexpensesapplication.model.Expense
import com.example.go.memoexpensesapplication.navigator.FragmentMainNavigator
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FragmentMainViewModelTest {

    @get:Rule
    val rule = RxImmediateSchedulerRule()

    @Spy
    val dispatcher = MainDispatcher()

    @Mock
    lateinit var navigator: FragmentMainNavigator
    lateinit var viewModel: FragmentMainViewModel

    @Before
    fun setUp() {
        viewModel = FragmentMainViewModel(dispatcher)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun occurredGetAllExpensesAction_confirmExpensesFlowPost() {
        val expenses = listOf(
            Expense("uid1", "tag1", 1, "note1"),
            Expense("uid2", "tag2", 2, "note2"),
            Expense("uid3", "tag3", 3, "note3")
        )
        val subscriber = viewModel.expenses.test()
        dispatcher.dispatch(MainAction.GetAllExpenses(expenses))
        subscriber
            .assertNoErrors()
            .assertValue(expenses)
    }

    @Test
    fun occurredAddExpenseAction_confirmAddExpenseFlowPost() {
        val expense = Expense("uid1", "tag1", 1, "note1")
        val subscriber = viewModel.addExpense.test()
        dispatcher.dispatch(MainAction.AddExpense(expense))
        subscriber
            .assertNoErrors()
            .assertValue(expense)
    }

    @Test
    fun occurredDeleteExpenseAction_confirmDeleteExpenseFlowPost() {
        val expense = Expense("uid1", "tag1", 1, "note1")
        val subscriber = viewModel.deleteExpense.test()
        dispatcher.dispatch(MainAction.DeleteExpense(expense))
        subscriber
            .assertNoErrors()
            .assertValue(expense)
    }

    @Test
    fun occurredMoveToTagListAction_confirmNavigatorCalled() {
        viewModel.setNavigator(navigator)
        dispatcher.dispatch(MainAction.MoveToTagList())
        verify(navigator, times(1)).onTransitionTagList()
    }
}