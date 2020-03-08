package com.example.go.memoexpensesapplication.actioncreator

import com.example.go.memoexpensesapplication.action.MainAction
import com.example.go.memoexpensesapplication.dispatcher.MainDispatcher
import com.example.go.memoexpensesapplication.model.Expense
import com.example.go.memoexpensesapplication.network.Database
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainActionCreatorTest {

    @Mock
    lateinit var database: Database

    @Mock
    lateinit var dispatcher: MainDispatcher
    lateinit var actionCreator: MainActionCreator

    @Before
    fun setUp() {
        actionCreator = MainActionCreator(database, dispatcher)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getAllExpenses_confirmDispatchGetAllExpensesAction() {
        val uid = "uid1"
        val expenses = listOf(
            Expense("uid1", "tag1", 1, "note1"),
            Expense("uid2", "tag2", 2, "note2"),
            Expense("uid3", "tag3", 3, "note3")
        )
        whenever(database.readExpenses(eq(uid), any()))
            .thenAnswer { invocation ->
                @Suppress("UNCHECKED_CAST")
                (invocation.arguments[1] as ((List<Expense>) -> Unit)?)?.invoke(expenses)
            }

        actionCreator.getAllExpenses(uid)
        verify(database, times(1)).readExpenses(eq(uid), any())
        argumentCaptor<MainAction.GetAllExpenses>().apply {
            verify(dispatcher, times(1)).dispatch(capture())
            assertThat(firstValue)
                .extracting("data")
                .isEqualTo(expenses)
        }
    }

    @Test
    fun addExpense_confirmDispatchAddExpenseAction() {
        val expense = Expense("uid1", "tag1", 1, "note1")
        val id = "id1"
        val expectedExpense = expense.copy(id = id)
        whenever(database.addExpense(eq(expense), any()))
            .thenAnswer { invocation ->
                @Suppress("UNCHECKED_CAST")
                (invocation.arguments[1] as ((String) -> Unit)?)?.invoke(id)
            }

        actionCreator.addExpense(expense)
        verify(database, times(1)).addExpense(eq(expense), any())
        argumentCaptor<MainAction.AddExpense>().apply {
            verify(dispatcher, times(1)).dispatch(capture())
            assertThat(firstValue)
                .extracting("data")
                .isEqualTo(expectedExpense)
        }
    }

    @Test
    fun deleteExpense_confirmDispatchDeleteExpenseAction() {
        val expense = Expense("uid1", "tag1", 1, "note1")
        whenever(database.deleteExpenses(eq(expense), any()))
            .thenAnswer { invocation ->
                @Suppress("UNCHECKED_CAST")
                (invocation.arguments[1] as (() -> Unit)?)?.invoke()
            }

        actionCreator.deleteExpense(expense)
        verify(database, times(1)).deleteExpenses(eq(expense), any())
        argumentCaptor<MainAction.DeleteExpense>().apply {
            verify(dispatcher, times(1)).dispatch(capture())
            assertThat(firstValue)
                .extracting("data")
                .isEqualTo(expense)
        }
    }

    @Test
    fun moveToTagList_confirmDispatchMoveToTagListAction() {
        actionCreator.moveToTagList()
        verify(dispatcher, times(1)).dispatch(any<MainAction.MoveToTagList>())
    }
}