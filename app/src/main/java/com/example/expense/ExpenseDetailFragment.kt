package com.example.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.expense.data.Expense
import com.example.expense.data.getFormattedPrice
import com.example.expense.databinding.ExpenseItemDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ExpenseDetailFragment : Fragment() {
    private val navigationArgs: ExpenseDetailFragmentArgs by navArgs()
    lateinit var expense: Expense

    private val viewModel: ExpenseDiaryViewModel by activityViewModels {
        ExpenseViewModelFactory(
            (activity?.application as ExpenseDiaryApplication).database.expenseDao()
        )
    }

    private var _binding: ExpenseItemDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ExpenseItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bind(expense: Expense) {
        binding.apply {
            expenseName.text = expense.name
            expenseSum.text = expense.getFormattedPrice()
            expenseDescription.text = expense.description
            deleteExpense.setOnClickListener { showConfirmationDialog() }
            editExpense.setOnClickListener { editItem() }
        }
    }

    private fun editItem() {
        val action = ExpenseDetailFragmentDirections.actionItemDetailFragmentToAddItemFragment(
            getString(R.string.edit_fragment_title),
            expense.id
        )
        this.findNavController().navigate(action)
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteItem()
            }
            .show()
    }

    private fun deleteItem() {
        viewModel.deleteItem(expense)
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
            expense = selectedItem
            bind(expense)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
