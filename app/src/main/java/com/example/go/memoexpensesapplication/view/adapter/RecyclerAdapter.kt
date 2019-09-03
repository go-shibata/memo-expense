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
import kotlinx.android.synthetic.main.list_item_fragment_main_header.view.*
import kotlinx.android.synthetic.main.list_item_fragment_main_section.view.*

class RecyclerAdapter(
    private val context: Context?,
    private val data: ArrayList<Expense>,
    private val onRecyclerListener: OnRecyclerListener
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 表示するレイアウトを設定
        return when (RecyclerType.fromInt(viewType)) {
            RecyclerType.HEADER -> {
                HeaderViewHolder(layoutInflater.inflate(R.layout.list_item_fragment_main_header, parent, false))
            }
            RecyclerType.FOOTER -> {
                BodyViewHolder(layoutInflater.inflate(R.layout.list_item_fragment_main_body, parent, false))
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
        // データ表示
        when (holder) {
            is BodyViewHolder -> {
                holder.apply {
                    tagView.text = data[position].tag
                    valueView.text = data[position].value
                    noteView.text = data[position].note
                    itemView.setOnClickListener {
                        onRecyclerListener.onRecyclerClicked(it, position, data[position])
                    }
                }
            }
            is SectionViewHolder -> {
                holder.tagView.text = data[position].tag
            }
            is HeaderViewHolder -> {
                holder.apply {
                    tagView.text = data[position].tag
                    valueView.text = data[position].value
                    noteView.text = data[position].note
                }
            }
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = data[position].type.type

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class HeaderViewHolder(itemView: View) : ViewHolder(itemView) {
        val tagView: TextView = itemView.list_item_fragment_main_header_tag
        val valueView: TextView = itemView.list_item_fragment_main_header_value
        val noteView: TextView = itemView.list_item_fragment_main_header_note
    }

    class BodyViewHolder(itemView: View) : ViewHolder(itemView) {
        val tagView: TextView = itemView.list_item_fragment_main_body_tag
        val valueView: TextView = itemView.list_item_fragment_main_body_value
        val noteView: TextView = itemView.list_item_fragment_main_body_note
    }

    class SectionViewHolder(itemView: View) : ViewHolder(itemView) {
        val tagView: TextView = itemView.list_item_fragment_main_section_tag
    }
}