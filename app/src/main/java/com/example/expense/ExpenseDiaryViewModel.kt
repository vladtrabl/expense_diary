package com.example.expense

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.expense.data.Expense
import com.example.expense.data.ExpenseDao
import kotlinx.coroutines.launch

class ExpenseDiaryViewModel(private val expenseDao: ExpenseDao) : ViewModel() {

    val allItems: LiveData<List<Expense>> = expenseDao.getItems().asLiveData()

    fun updateItem(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItem)
    }

    private fun updateItem(expense: Expense) {
        viewModelScope.launch {
            expenseDao.update(expense)
        }
    }

    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }

    private fun insertItem(expense: Expense) {
        viewModelScope.launch {
            expenseDao.insert(expense)
        }
    }

    fun deleteItem(expense: Expense) {
        viewModelScope.launch {
            expenseDao.delete(expense)
        }
    }

    fun retrieveItem(id: Int): LiveData<Expense> {
        return expenseDao.getExpense(id).asLiveData()
    }

    fun isEntryValid(expenseName: String, expensePrice: String, expenseCount: String): Boolean {
        if (expenseName.isBlank() || expensePrice.isBlank() || expenseCount.isBlank()) {
            return false
        }
        return true
    }

    private fun getNewItemEntry(expenseName: String, expensePrice: String, expenseCount: String): Expense {
        return Expense(
            name = expenseName,
            sum = expensePrice.toDouble(),
            description = expenseCount
        )
    }

    private fun getUpdatedItemEntry(
        expenseId: Int,
        expenseName: String,
        expensePrice: String,
        expenseCount: String
    ): Expense {
        return Expense(
            id = expenseId,
            name = expenseName,
            sum = expensePrice.toDouble(),
            description = expenseCount
        )
    }
}

class ExpenseViewModelFactory(private val expenseDao: ExpenseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseDiaryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseDiaryViewModel(expenseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

