package com.example.go.memoexpensesapplication.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.go.memoexpensesapplication.RxImmediateSchedulerRule
import com.example.go.memoexpensesapplication.action.MainAction
import com.example.go.memoexpensesapplication.dispatcher.MainDispatcher
import com.example.go.memoexpensesapplication.model.Expense
import com.example.go.memoexpensesapplication.navigator.FragmentMainNavigator
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
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
    val instantTaskExecutorRule = InstantTaskExecutorRule() // for LiveData

    @get:Rule
    val rxImmediateSchedulerRule = RxImmediateSchedulerRule()

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
    fun occurredGetAllExpensesAction_confirmExpensesChanged() {
        val expenses = listOf(
            Expense("uid1", "tag1", 1, "note1"),
            Expense("uid2", "tag2", 2, "note2"),
            Expense("uid3", "tag3", 3, "note3")
        )
        val observer = mock<Observer<List<Expense>>>()
        viewModel.expenses.observeForever(observer)
        dispatcher.dispatch(MainAction.GetAllExpenses(expenses))

        verify(observer, times(1)).onChanged(expenses)
    }

    @Test
    fun occurredAddExpenseAction_confirmExpensesChanged() {
        val expense = Expense("uid1", "tag1", 1, "note1")
        val expected = listOf(expense)
        val observer = mock<Observer<List<Expense>>>()
        viewModel.expenses.observeForever(observer)
        dispatcher.dispatch(MainAction.AddExpense(expense))

        verify(observer, times(1)).onChanged(expected)
    }

    @Test
    fun occurredEditExpenseAction_confirmExpensesChanged() {
        val expenses = listOf(
            Expense("1", "uid1", "tag1", 1, "note1"),
            Expense("2", "uid2", "tag2", 2, "note2"),
            Expense("3", "uid3", "tag3", 3, "note3")
        )
        val expense = Expense("1", "uid4", "tag4", 4, "note4")
        val expected = expenses - expenses[0] + expense
        val observer = mock<Observer<List<Expense>>>()
        viewModel.expenses.observeForever(observer)
        dispatcher.dispatch(MainAction.GetAllExpenses(expenses))
        dispatcher.dispatch(MainAction.EditExpense(expense))

        verify(observer, times(1)).onChanged(expected)
    }

    @Test
    fun occurredDeleteExpenseAction_confirmExpensesChanged() {
        val expenses = listOf(
            Expense("uid1", "tag1", 1, "note1"),
            Expense("uid2", "tag2", 2, "note2"),
            Expense("uid3", "tag3", 3, "note3")
        )
        val expected = expenses - expenses[0]
        val observer = mock<Observer<List<Expense>>>()
        viewModel.expenses.observeForever(observer)
        dispatcher.dispatch(MainAction.GetAllExpenses(expenses))
        dispatcher.dispatch(MainAction.DeleteExpense(expenses[0]))

        verify(observer, times(1)).onChanged(expected)
    }

    @Test
    fun occurredMoveToTagListAction_confirmNavigatorCalled() {
        viewModel.setNavigator(navigator)
        dispatcher.dispatch(MainAction.MoveToTagList())
        verify(navigator, times(1)).onTransitionTagList()
    }

    @Test
    fun occurredToggleCheckableAction_confirmIsCheckableChanged() {
        val observer = mock<Observer<Boolean>>()
        viewModel.isCheckable.observeForever(observer)
        dispatcher.dispatch(MainAction.ToggleCheckable())
        dispatcher.dispatch(MainAction.ToggleCheckable())

        inOrder(observer) {
            verify(observer, times(1)).onChanged(true)
            verify(observer, times(1)).onChanged(false)
        }
    }
}