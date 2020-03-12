package com.example.go.memoexpensesapplication.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.constant.ExpenseViewType
import com.example.go.memoexpensesapplication.databinding.ListItemFragmentMainBodyBinding
import com.example.go.memoexpensesapplication.databinding.ListItemFragmentMainSectionBinding
import com.example.go.memoexpensesapplication.fragment.MainFragment
import com.example.go.memoexpensesapplication.model.Expense
import com.example.go.memoexpensesapplication.viewmodel.FragmentMainViewModel
import kotlinx.android.synthetic.main.list_item_fragment_main_body.view.*
import java.util.*
import kotlin.collections.ArrayList

class ExpenseListAdapter(
    fragment: MainFragment,
    private val viewModel: FragmentMainViewModel,
    private val onClickExpenseListener: OnClickExpenseListener
) : RecyclerView.Adapter<ExpenseListAdapter.ViewHolder>() {

    private var groupedData: SortedMap<String, ArrayList<CheckableExpense>> = sortedMapOf()
    private var groupedItemCount: SortedMap<String, Int> = sortedMapOf()
    private var hasHeader = false
    private var hasFooter = false

    init {
        viewModel.expenses
            .observe(fragment, Observer { setData(it) })
        viewModel.isCheckable
            .observe(fragment, Observer { isCheckable ->
                if (!isCheckable) {
                    groupedData.values
                        .forEach { checkableExpenses ->
                            checkableExpenses.forEach { it.isChecked = false }
                        }
                }
                notifyDataSetChanged()
            })
    }

    private fun setData(data: List<Expense>) {
        groupedData = data.groupBy { it.tag }
            .mapValues {
                it.value
                    .map { expense -> CheckableExpense(expense) }
                    .toCollection(ArrayList())
            }.toSortedMap()
        groupedItemCount = groupedData
            .mapValues { it.value.count() + 1 }
            .toSortedMap()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        // 表示するレイアウトを設定
        return when (ExpenseViewType.fromInt(viewType)) {
            ExpenseViewType.HEADER -> {
                HeaderViewHolder(
                    layoutInflater.inflate(
                        R.layout.list_item_fragment_main_header,
                        parent,
                        false
                    )
                )
            }
            ExpenseViewType.FOOTER -> {
                FooterViewHolder(
                    layoutInflater.inflate(
                        R.layout.list_item_fragment_main_footer,
                        parent,
                        false
                    )
                )
            }
            ExpenseViewType.SECTION -> {
                val binding =
                    ListItemFragmentMainSectionBinding.inflate(layoutInflater, parent, false)
                SectionViewHolder(binding)
            }
            ExpenseViewType.BODY -> {
                val binding = ListItemFragmentMainBodyBinding.inflate(layoutInflater, parent, false)
                BodyViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (sectionKey, localPosition) = getLocalPosition(position)
        when (holder) {
            is BodyViewHolder -> {
                val data = groupedData[sectionKey]?.get(localPosition)
                    ?: throw RuntimeException("Invalid data")
                holder.apply {
                    binding.isCheckable = viewModel.isCheckable.value
                    binding.checkableExpense = data
                    itemView.setOnClickListener {
                        if (viewModel.isCheckable.value == true) {
                            binding.root.checkbox.isChecked = !binding.root.checkbox.isChecked
                        } else {
                            onClickExpenseListener.onClickExpense(data.expense)
                        }
                    }
                }
            }
            is SectionViewHolder -> {
                val header = groupedData[sectionKey]?.let { list ->
                    Header(sectionKey, list.sumBy { it.expense.value })
                } ?: throw RuntimeException("Invalid data")
                holder.binding.header = header
            }
            is HeaderViewHolder -> {
            }
            is FooterViewHolder -> {
            }
        }
    }

    override fun getItemCount(): Int {
        var count = groupedItemCount.values.sum()
        count += if (hasHeader) 1 else 0
        count += if (hasFooter) 1 else 0
        return count
    }

    override fun getItemViewType(position: Int): Int {
        val (sectionKey, localPosition) = getLocalPosition(position)
        if (sectionKey == HEADER) return ExpenseViewType.HEADER.type
        if (sectionKey == FOOTER) return ExpenseViewType.FOOTER.type
        if (localPosition == SECTION) return ExpenseViewType.SECTION.type
        return ExpenseViewType.BODY.type
    }

    private fun getLocalPosition(position: Int): Pair<String, Int> {
        if (hasHeader && position == 0) return HEADER to -1
        if (hasFooter && position == itemCount - 1) return FOOTER to -1

        var relativePosition = if (hasHeader) position - 1 else position
        groupedItemCount.forEach { (key, count) ->
            if (relativePosition < count) {
                val localPosition = if (relativePosition == 0) SECTION else relativePosition - 1
                return key to localPosition
            }
            relativePosition -= count
        }

        throw RuntimeException("invalid position")
    }

    fun setHeader() {
        hasHeader = true
    }

    fun setFooter() {
        hasFooter = true
    }

    fun getCheckedExpenses(): List<Expense> {
        return groupedData.values
            .map { checkableExpenses ->
                checkableExpenses.filter { it.isChecked }
                    .map { it.expense }
            }
            .fold(listOf()) { acc, list -> acc + list }
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class HeaderViewHolder(itemView: View) : ViewHolder(itemView)

    class FooterViewHolder(itemView: View) : ViewHolder(itemView)

    class BodyViewHolder(val binding: ListItemFragmentMainBodyBinding) : ViewHolder(binding.root)

    class SectionViewHolder(val binding: ListItemFragmentMainSectionBinding) :
        ViewHolder(binding.root)

    interface OnClickExpenseListener {
        fun onClickExpense(expense: Expense)
    }

    data class CheckableExpense(
        val expense: Expense,
        var isChecked: Boolean = false
    )

    data class Header(
        val tag: String,
        val value: Int
    )

    companion object {
        private const val HEADER = "HEADER"
        private const val FOOTER = "FOOTER"
        private const val SECTION = -1
    }
}