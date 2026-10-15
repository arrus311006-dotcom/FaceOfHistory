package com.example.historyquizru.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.historyquizru.data.PersonalityEntity
import com.example.historyquizru.databinding.ItemPersonalityBinding
import com.example.historyquizru.util.ImageLoader

class PersonalityAdapter(
    private val onPersonalityClick: (PersonalityEntity) -> Unit
) : RecyclerView.Adapter<PersonalityAdapter.PersonalityViewHolder>() {

    private val personalities = mutableListOf<PersonalityEntity>()

    fun setItems(items: List<PersonalityEntity>) {
        personalities.clear()
        personalities.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalityViewHolder {
        val binding = ItemPersonalityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PersonalityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonalityViewHolder, position: Int) {
        val personality = personalities[position]
        holder.binding.tvName.text = personality.name
        holder.binding.tvYearsOfLife.text = personality.yearsOfLife
        ImageLoader.loadPersonalityImage(holder.binding.ivAvatar, personality.imageName)
        holder.binding.root.setOnClickListener {
            onPersonalityClick(personality)
        }
    }

    override fun getItemCount(): Int = personalities.size

    class PersonalityViewHolder(
        val binding: ItemPersonalityBinding
    ) : RecyclerView.ViewHolder(binding.root)
}