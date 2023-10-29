package com.example.cigarettequitter

import androidx.recyclerview.widget.DiffUtil

class TipDiffCallback : DiffUtil.ItemCallback<Tip>() {
    override fun areItemsTheSame(oldItem: Tip, newItem: Tip)
            = (oldItem.id == newItem.id)
    override fun areContentsTheSame(oldItem: Tip, newItem: Tip) = (oldItem == newItem)
}