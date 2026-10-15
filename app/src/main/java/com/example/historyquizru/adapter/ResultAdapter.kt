package com.example.historyquizru.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.historyquizru.R
import com.example.historyquizru.data.QuizResultEntity
import com.example.historyquizru.databinding.ItemResultBinding
import java.text.DateFormat
import java.util.Date

class ResultAdapter : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    private val results = mutableListOf<QuizResultEntity>()
    private var epochNames: Map<Int, String> = emptyMap()

    fun setItems(items: List<QuizResultEntity>, names: Map<Int, String>) {
        results.clear()
        results.addAll(items)
        epochNames = names
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = results[position]
        val context = holder.binding.root.context

        holder.binding.tvEpoch.text = epochNames[result.epochId].orEmpty()
        holder.binding.tvDate.text = DateFormat.getDateTimeInstance()
            .format(Date(result.date))
        holder.binding.tvScore.text = context.getString(
            R.string.result_item_format,
            result.correctCount,
            result.correctCount + result.wrongCount,
            10
        )
    }

    override fun getItemCount(): Int = results.size

    class ResultViewHolder(
        val binding: ItemResultBinding
    ) : RecyclerView.ViewHolder(binding.root)
}