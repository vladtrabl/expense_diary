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
    val Name: String,
    @ColumnInfo(name = "sum")
    val Sum: Double,
    @ColumnInfo(name = "description")
    val Description: String,
)
fun Expense.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(Sum)