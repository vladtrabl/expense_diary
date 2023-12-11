package com.example.expense.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val itemName: String,
    @ColumnInfo(name = "sum")
    val itemSum: Double,
    @ColumnInfo(name = "description")
    val itemDescription: String,
)
fun Item.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(itemSum)