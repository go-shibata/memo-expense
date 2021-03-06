package com.example.go.memoexpensesapplication.network

import android.util.Log
import com.example.go.memoexpensesapplication.model.Expense
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Database @Inject constructor() {

    private fun getDatabase(): FirebaseFirestore = FirebaseFirestore.getInstance()

    fun addExpense(expense: Expense, exec: ((String) -> Unit)? = null) {
        val data = mapOf(
            "uid" to expense.uid,
            "tag" to expense.tag,
            "value" to expense.value,
            "note" to expense.note
        )
        getDatabase().collection(COLLECTION_EXPENSE)
            .add(data)
            .addOnSuccessListener {
                Log.d(LOG_TAG, "DocumentSnapshot added with ID: ${it.id}")
                exec?.invoke(it.id)
            }
            .addOnFailureListener {
                Log.w(LOG_TAG, "Error adding document", it)
            }
    }

    fun editExpense(expense: Expense, exec: (() -> Unit)? = null) {
        val data = mapOf(
            "uid" to expense.uid,
            "tag" to expense.tag,
            "value" to expense.value,
            "note" to expense.note
        )
        getDatabase().collection(COLLECTION_EXPENSE)
            .document(expense.id ?: throw RuntimeException("Document id not found"))
            .set(data)
            .addOnSuccessListener {
                Log.d(LOG_TAG, "Document edited with ID: ${expense.id}")
                exec?.invoke()
            }
            .addOnFailureListener {
                Log.w(LOG_TAG, "Error editing document", it)
            }
    }

    fun readExpenses(uid: String, exec: ((List<Expense>) -> Unit)? = null) {
        getDatabase().collection(COLLECTION_EXPENSE)
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener {
                val data = it.documents.map { item ->
                    Expense(
                        item.id,
                        item.get("uid") as String,
                        item.get("tag") as String,
                        (item.get("value") as Long).toInt(),
                        item.get("note") as String
                    )
                }
                Log.d(LOG_TAG, "Get documents, size = ${it.size()}")
                exec?.invoke(data)
            }
            .addOnFailureListener {
                Log.w(LOG_TAG, "Error getting documents", it)
            }
    }

    fun deleteExpenses(expense: Expense, exec: (() -> Unit)? = null) {
        getDatabase().collection(COLLECTION_EXPENSE)
            .document(expense.id ?: return)
            .delete()
            .addOnSuccessListener {
                Log.d(LOG_TAG, "Delete document, id = ${expense.id}")
                exec?.invoke()
            }
            .addOnFailureListener {
                Log.w(LOG_TAG, "Error deleting documents", it)
            }
    }

    companion object {
        private const val LOG_TAG = "MyDatabase"
        private const val COLLECTION_EXPENSE = "expenses"
    }
}