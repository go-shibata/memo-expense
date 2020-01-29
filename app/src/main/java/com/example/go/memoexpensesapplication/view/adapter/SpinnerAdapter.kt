package com.example.go.memoexpensesapplication.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.go.memoexpensesapplication.R
import kotlinx.android.synthetic.main.spinner_item.view.*

class SpinnerAdapter(
    context: Context,
    private val items: List<String>
) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.spinner_item, parent, false)
            viewHolder = ViewHolder(
                view.tag_name
            )
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.bindView(getItem(position))
        return view
    }

    override fun getItem(position: Int): String = items[position]

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = items.size


    class ViewHolder(private val textView: TextView) {
        fun bindView(tag: String?) {
            textView.text = tag
        }
    }
}