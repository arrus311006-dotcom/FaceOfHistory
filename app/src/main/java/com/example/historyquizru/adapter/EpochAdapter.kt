package com.example.historyquizru.adapter

import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.historyquizru.data.EpochEntity

class EpochAdapter(
    private val onEpochClick: (EpochEntity) -> Unit
) : RecyclerView.Adapter<EpochAdapter.EpochViewHolder>() {

    private val epochs = mutableListOf<EpochEntity>()

    fun setItems(items: List<EpochEntity>) {
        epochs.clear()
        epochs.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpochViewHolder {
        val textView = TextView(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            gravity = Gravity.CENTER_VERTICAL
            textSize = 18f
            setPadding(
                (20 * resources.displayMetrics.density).toInt(),
                (18 * resources.displayMetrics.density).toInt(),
                (20 * resources.displayMetrics.density).toInt(),
                (18 * resources.displayMetrics.density).toInt()
            )
            isClickable = true
            isFocusable = true
//            setBackgroundResource(
//                com.google.android.material.R.drawable.mtrl_list_item_background
//            )
        }
        return EpochViewHolder(textView)
    }

    override fun onBindViewHolder(holder: EpochViewHolder, position: Int) {
        val epoch = epochs[position]
        holder.title.text = epoch.name
        holder.itemView.setOnClickListener { onEpochClick(epoch) }
    }

    override fun getItemCount(): Int = epochs.size

    class EpochViewHolder(val title: TextView) : RecyclerView.ViewHolder(title)
}