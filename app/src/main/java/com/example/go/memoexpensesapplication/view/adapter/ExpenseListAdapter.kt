package com.example.go.memoexpensesapplication.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.constant.ExpenseViewType
import com.example.go.memoexpensesapplication.databinding.ListItemFragmentMainBodyBinding
import com.example.go.memoexpensesapplication.databinding.ListItemFragmentMainSectionBinding
import com.example.go.memoexpensesapplication.model.Expense
import com.example.go.memoexpensesapplication.view.listener.OnRecyclerListener

class ExpenseListAdapter(
    private var data: List<Expense>,
    private val onRecyclerListener: OnRecyclerListener
) : RecyclerView.Adapter<ExpenseListAdapter.ViewHolder>() {

    private var hasHeader = false
    private var hasFooter = false

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
        val data = getDataWithSection()
        // データ表示
        when (holder) {
            is BodyViewHolder -> {
                val dataPos = if (hasHeader) position - 1 else position
                holder.apply {
                    binding.expense = data[dataPos]
                    itemView.setOnClickListener {
                        onRecyclerListener.onRecyclerClicked(it, dataPos, data[dataPos])
                    }
                }
            }
            is SectionViewHolder -> {
                val dataPos = if (hasHeader) position - 1 else position
                holder.binding.expense = data[dataPos]
            }
            is HeaderViewHolder -> {
            }
            is FooterViewHolder -> {
            }
        }
    }

    override fun getItemCount(): Int {
        val data = getDataWithSection()
        var count = data.size
        count += if (hasHeader) 1 else 0
        count += if (hasFooter) 1 else 0
        return count
    }

    override fun getItemViewType(position: Int): Int {
        val data = getDataWithSection()
        return if (hasHeader) {
            when {
                position == 0 -> {
                    ExpenseViewType.HEADER.type
                }
                hasFooter && position == itemCount - 1 -> {
                    ExpenseViewType.FOOTER.type
                }
                else -> {
                    data[position - 1].type.type
                }
            }
        } else {
            when {
                hasFooter && position == itemCount - 1 -> {
                    ExpenseViewType.FOOTER.type
                }
                else -> {
                    data[position].type.type
                }
            }
        }
    }

    fun setHeader() {
        hasHeader = true
    }

    fun setFooter() {
        hasFooter = true
    }

    fun update(data: List<Expense>) {
        this.data = data
        notifyDataSetChanged()
    }

    private fun getDataWithSection(): ArrayList<Expense> {
        val sortedData = ArrayList(data.sortedBy { it.tag })
        val tagList = data.map { it.tag }.distinct()
        for (tag in tagList) {
            val sum = data.filter { it.tag == tag }.sumBy { it.value ?: 0 }
            sortedData.add(
                sortedData.indexOfFirst { it.tag == tag },
                Expense(null, ExpenseViewType.SECTION, tag, sum, null)
            )
        }
        return sortedData
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class HeaderViewHolder(itemView: View) : ViewHolder(itemView)

    class FooterViewHolder(itemView: View) : ViewHolder(itemView)

    class BodyViewHolder(val binding: ListItemFragmentMainBodyBinding) : ViewHolder(binding.root)

    class SectionViewHolder(val binding: ListItemFragmentMainSectionBinding) :
        ViewHolder(binding.root)
}