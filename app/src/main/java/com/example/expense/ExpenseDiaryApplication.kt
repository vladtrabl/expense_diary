package com.example.expense

import android.app.Application
import com.example.expense.data.ItemRoomDatabase

class ExpenseDiaryApplication : Application() {
    val database: ItemRoomDatabase by lazy { ItemRoomDatabase.getDatabase(this) }
}
