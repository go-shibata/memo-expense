package com.example.go.memoexpensesapplication.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.constant.RecyclerType
import com.example.go.memoexpensesapplication.model.Expense
import com.example.go.memoexpensesapplication.view.listener.OnRecyclerListener
import kotlinx.android.synthetic.main.list_item_fragment_main_body.view.*
import kotlinx.android.synthetic.main.list_item_fragment_main_section.view.*

class RecyclerAdapter(
    private val context: Context?,
    private val data: ArrayList<Expense>,
    private val onRecyclerListener: OnRecyclerListener
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var hasHeader = false
    private var hasFooter = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 表示するレイアウトを設定
        return when (RecyclerType.fromInt(viewType)) {
            RecyclerType.HEADER -> {
                HeaderViewHolder(layoutInflater.inflate(R.layout.list_item_fragment_main_header, parent, false))
            }
            RecyclerType.FOOTER -> {
                FooterViewHolder(layoutInflater.inflate(R.layout.list_item_fragment_main_footer, parent, false))
            }
            RecyclerType.SECTION -> {
                SectionViewHolder(layoutInflater.inflate(R.layout.list_item_fragment_main_section, parent, false))
            }
            RecyclerType.BODY -> {
                BodyViewHolder(layoutInflater.inflate(R.layout.list_item_fragment_main_body, parent, false))
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
                    tagView.text = data[dataPos].tag
                    valueView.text = data[dataPos].value
                    noteView.text = data[dataPos].note
                    itemView.setOnClickListener {
                        onRecyclerListener.onRecyclerClicked(it, dataPos, data[dataPos])
                    }
                }
            }
            is SectionViewHolder -> {
                holder.tagView.text = data[position].tag
            }
            is HeaderViewHolder -> {}
            is FooterViewHolder -> {}
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
                    RecyclerType.HEADER.type
                }
                hasFooter && position == itemCount - 1 -> {
                    RecyclerType.FOOTER.type
                }
                else -> {
                    data[position - 1].type.type
                }
            }
        } else {
            when {
                hasFooter && position == itemCount - 1 -> {
                    RecyclerType.FOOTER.type
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

    fun getDataWithSection(): ArrayList<Expense> {
        val sortedData = ArrayList(data.sortedBy { it.tag })
        val tagList = data.map { it.tag }.distinct()
        for (tag in tagList) {
            sortedData.add(sortedData.indexOfFirst { it.tag == tag }, Expense(RecyclerType.SECTION, tag, null, null))
        }
        return sortedData
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class HeaderViewHolder(itemView: View) : ViewHolder(itemView)

    class FooterViewHolder(itemView: View) : ViewHolder(itemView)

    class BodyViewHolder(itemView: View) : ViewHolder(itemView) {
        val tagView: TextView = itemView.list_item_fragment_main_body_tag
        val valueView: TextView = itemView.list_item_fragment_main_body_value
        val noteView: TextView = itemView.list_item_fragment_main_body_note
    }

    class SectionViewHolder(itemView: View) : ViewHolder(itemView) {
        val tagView: TextView = itemView.list_item_fragment_main_section_tag
    }
}