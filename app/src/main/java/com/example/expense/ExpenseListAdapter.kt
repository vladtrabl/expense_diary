package com.example.expense

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expense.data.Expense
import com.example.expense.data.getFormattedPrice
import com.example.expense.databinding.ExpenseListItemBinding

class ExpenseListAdapter(private val onItemClicked: (Expense) -> Unit) :
    ListAdapter<Expense, ExpenseListAdapter.ExpenseViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        return ExpenseViewHolder(
            ExpenseListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class ExpenseViewHolder(private var binding: ExpenseListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(expense: Expense) {
            binding.expenseName.text = expense.name
            binding.expenseSum.text = expense.getFormattedPrice()
            binding.expenseDetails.text = expense.description
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Expense>() {
            override fun areItemsTheSame(oldExpense: Expense, newExpense: Expense): Boolean {
                return oldExpense === newExpense
            }

            override fun areContentsTheSame(oldExpense: Expense, newExpense: Expense): Boolean {
                return oldExpense.name == newExpense.name
            }
        }
    }
}
