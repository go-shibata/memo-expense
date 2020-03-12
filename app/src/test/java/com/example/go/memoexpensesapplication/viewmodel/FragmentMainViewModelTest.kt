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
    fun occurredGetAllExpensesAction_confirmUpdateExpensesFlowPost() {
        val expenses = listOf(
            Expense("uid1", "tag1", 1, "note1"),
            Expense("uid2", "tag2", 2, "note2"),
            Expense("uid3", "tag3", 3, "note3")
        )
        val subscriber = viewModel.updateExpenses.test()
        dispatcher.dispatch(MainAction.GetAllExpenses(expenses))
        subscriber
            .assertNoErrors()
            .assertValue(expenses)
    }

    @Test
    fun occurredAddExpenseAction_confirmUpdateExpensesFlowPost() {
        val expense = Expense("uid1", "tag1", 1, "note1")
        val expected = listOf(expense)
        val subscriber = viewModel.updateExpenses.test()
        dispatcher.dispatch(MainAction.AddExpense(expense))
        subscriber
            .assertNoErrors()
            .assertValue(expected)
    }

    @Test
    fun occurredEditExpenseAction_confirmUpdateExpensesFlowPost() {
        val expenses = listOf(
            Expense("1", "uid1", "tag1", 1, "note1"),
            Expense("2", "uid2", "tag2", 2, "note2"),
            Expense("3", "uid3", "tag3", 3, "note3")
        )
        val expense = Expense("1", "uid4", "tag4", 4, "note4")
        val expected = expenses - expenses[0] + expense
        dispatcher.dispatch(MainAction.GetAllExpenses(expenses))

        val subscriber = viewModel.updateExpenses.test()
        dispatcher.dispatch(MainAction.EditExpense(expense))
        subscriber
            .assertNoErrors()
            .assertValue(expected)
    }

    @Test
    fun occurredDeleteExpenseAction_confirmUpdateExpensesFlowPost() {
        val expenses = listOf(
            Expense("uid1", "tag1", 1, "note1"),
            Expense("uid2", "tag2", 2, "note2"),
            Expense("uid3", "tag3", 3, "note3")
        )
        val expected = expenses - expenses[0]
        dispatcher.dispatch(MainAction.GetAllExpenses(expenses))

        val subscriber = viewModel.updateExpenses.test()
        dispatcher.dispatch(MainAction.DeleteExpense(expenses[0]))
        subscriber
            .assertNoErrors()
            .assertValue(expected)
    }

    @Test
    fun occurredMoveToTagListAction_confirmNavigatorCalled() {
        viewModel.setNavigator(navigator)
        dispatcher.dispatch(MainAction.MoveToTagList())
        verify(navigator, times(1)).onTransitionTagList()
    }
}