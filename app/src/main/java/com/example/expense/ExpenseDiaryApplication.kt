package com.example.expense

import android.app.Application
import com.example.expense.data.ExpenseRoomDatabase

class ExpenseDiaryApplication : Application() {
    val database: ExpenseRoomDatabase by lazy { ExpenseRoomDatabase.getDatabase(this) }
}
