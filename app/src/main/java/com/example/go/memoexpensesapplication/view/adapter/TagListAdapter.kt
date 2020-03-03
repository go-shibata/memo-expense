package com.example.go.memoexpensesapplication.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.go.memoexpensesapplication.databinding.ListItemTagListBinding

class TagListAdapter(
    private var data: List<String>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<TagListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTagListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            binding.tag = data[position]
            itemView.setOnClickListener {
                onClickListener.onRecyclerClicked(it, position, data[position])
            }
        }
    }

    override fun getItemCount(): Int = data.size

    fun update(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun add(tag: String) {
        this.data = data + tag
        notifyDataSetChanged()
    }

    fun delete(tag: String) {
        this.data = data - tag
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ListItemTagListBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnClickListener {
        fun onRecyclerClicked(v: View, position: Int, item: String)
    }

}