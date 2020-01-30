package com.example.go.memoexpensesapplication.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.go.memoexpensesapplication.databinding.ListItemTagListSpinnerBinding

class TagListSpinnerAdapter(
    context: Context,
    private val items: List<String>
) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ListItemTagListSpinnerBinding

        if (convertView == null) {
            binding = ListItemTagListSpinnerBinding.inflate(inflater, parent, false)
            val viewHolder = ViewHolder(binding)
            binding.root.tag = viewHolder
        } else {
            binding = (convertView.tag as ViewHolder).binding
        }
        binding.tag = getItem(position)

        return binding.root
    }

    override fun getItem(position: Int): String = items[position]

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = items.size


    class ViewHolder(val binding: ListItemTagListSpinnerBinding)
}