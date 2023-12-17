package com.example.expense

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.expense.data.Expense
import com.example.expense.data.ExpenseDao
import kotlinx.coroutines.launch
import java.util.Calendar

class ExpenseDiaryViewModel(private val expenseDao: ExpenseDao) : ViewModel() {

    private val calender: Calendar = Calendar.getInstance()
    private val year = calender.get(Calendar.YEAR)
    private val month = calender.get(Calendar.MONTH) + 1
    private val day = calender.get(Calendar.DAY_OF_MONTH)

    val allExpenses: LiveData<List<Expense>> = expenseDao.getItems().asLiveData()
    val daySum: LiveData<Double> = expenseDao.getByDate("%$day.$month.$year%").asLiveData()
    val monthSum: LiveData<Double> = expenseDao.getByDate("%$month.$year%").asLiveData()

    fun updateItem(
        id: Int,
        name: String,
        price: String,
        count: String,
        date: String
    ) {
        val updatedItem = getUpdatedExpenseEntry(id, name, price, count, date)
        updateItem(updatedItem)
    }

    private fun updateItem(expense: Expense) {
        viewModelScope.launch {
            expenseDao.update(expense)
        }
    }

    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        val newExpense = getNewExpenseEntry(itemName, itemPrice, itemCount)
        insertExpense(newExpense)
    }

    private fun insertExpense(expense: Expense) {
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

    private fun getNewExpenseEntry(expenseName: String, expensePrice: String, expenseCount: String): Expense {
        val calender: Calendar = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH) + 1
        val day = calender.get(Calendar.DAY_OF_MONTH)
        val hour = calender.get(Calendar.HOUR_OF_DAY)
        val minute = calender.get(Calendar.MINUTE)
        return Expense(
            name = expenseName,
            sum = expensePrice.toDouble(),
            description = expenseCount,
            created = "$day.$month.$year $hour:$minute"
        )
    }

    private fun getUpdatedExpenseEntry(
        expenseId: Int,
        expenseName: String,
        expensePrice: String,
        expenseCount: String,
        expenseDate: String
    ): Expense {


        return Expense(
            id = expenseId,
            name = expenseName,
            sum = expensePrice.toDouble(),
            description = expenseCount,
            created = expenseDate
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

