package com.example.expense

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expense.data.Expense
import com.example.expense.data.getFormattedPrice
import com.example.expense.databinding.ItemListItemBinding

class ItemListAdapter(private val onItemClicked: (Expense) -> Unit) :
    ListAdapter<Expense, ItemListAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class ItemViewHolder(private var binding: ItemListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(expense: Expense) {
            binding.itemName.text = expense.itemName
            binding.itemSum.text = expense.getFormattedPrice()
            binding.itemDetails.text = expense.itemDescription
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Expense>() {
            override fun areItemsTheSame(oldExpense: Expense, newExpense: Expense): Boolean {
                return oldExpense === newExpense
            }

            override fun areContentsTheSame(oldExpense: Expense, newExpense: Expense): Boolean {
                return oldExpense.itemName == newExpense.itemName
            }
        }
    }
}
