package com.example.expense

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.expense.data.Expense
import com.example.expense.databinding.FragmentAddItemBinding
import java.util.Calendar

class AddExpenseFragment : Fragment() {

    private val viewModel: ExpenseDiaryViewModel by activityViewModels {
        ExpenseViewModelFactory(
            (activity?.application as ExpenseDiaryApplication).database
                .expenseDao()
        )
    }
    private val navigationArgs: ExpenseDetailFragmentArgs by navArgs()

    lateinit var expense: Expense

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.expenseName.text.toString(),
            binding.expenseSum.text.toString(),
            binding.expenseDescription.text.toString(),
        )
    }

    private fun bind(expense: Expense) {
        val sum = "%.2f".format(expense.sum)
        binding.apply {
            expenseName.setText(expense.name, TextView.BufferType.SPANNABLE)
            expenseSum.setText(sum, TextView.BufferType.SPANNABLE)
            expenseDescription.setText(expense.description, TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener { updateItem() }
        }
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                binding.expenseName.text.toString(),
                binding.expenseSum.text.toString(),
                binding.expenseDescription.text.toString(),
            )
            val action = AddExpenseFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)
        }
    }

    private fun updateItem() {
        if (isEntryValid()) {
            val calender: Calendar = Calendar.getInstance()
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH) + 1
            val day = calender.get(Calendar.DAY_OF_MONTH)
            val hour = calender.get(Calendar.HOUR_OF_DAY)
            val minute = calender.get(Calendar.MINUTE)

            viewModel.updateItem(
                this.navigationArgs.itemId,
                this.binding.expenseName.text.toString(),
                this.binding.expenseSum.text.toString(),
                this.binding.expenseDescription.text.toString(),
                "$day.$month.$year $hour:$minute"
            )
            val action = AddExpenseFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.itemId
        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                expense = selectedItem
                bind(expense)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewItem()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}
