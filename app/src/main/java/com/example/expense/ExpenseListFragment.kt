package com.example.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense.databinding.ExpenseListFragmentBinding

class ExpenseListFragment : Fragment() {
    private val viewModel: ExpenseDiaryViewModel by activityViewModels {
        ExpenseViewModelFactory(
            (activity?.application as ExpenseDiaryApplication).database.expenseDao()
        )
    }
    private var currencySymbol: String = ""
    private var _binding: ExpenseListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ExpenseListFragmentBinding.inflate(inflater, container, false)
        currencySymbol = getString(R.string.currency_symbol)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ExpenseListAdapter {
            val action =
                ExpenseListFragmentDirections.actionItemListFragmentToItemDetailFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter
        viewModel.allExpenses.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }

        viewModel.daySum.observe(this.viewLifecycleOwner) { sum ->
            sum.let {
                binding.expenseTotalDay.text = getString(
                    R.string.total_sum_day, (currencySymbol + it.toString())
                )
            }
        }

        viewModel.monthSum.observe(this.viewLifecycleOwner) { sum ->
            sum.let {
                binding.expenseTotalMonth.text = getString(
                    R.string.total_sum_month, (currencySymbol + it.toString())
                )
            }
        }

        binding.floatingActionButton.setOnClickListener {
            val action = ExpenseListFragmentDirections
                .actionItemListFragmentToAddItemFragment(getString(R.string.add_fragment_title))
            this.findNavController().navigate(action)
        }
    }
}
