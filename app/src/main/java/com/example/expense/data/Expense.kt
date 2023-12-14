package com.example.expense.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

@Entity
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "sum")
    val sum: Double,
    @ColumnInfo(name = "description")
    val description: String,
)
fun Expense.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(sum)