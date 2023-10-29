package com.example.cigarettequitter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cigarettequitter.databinding.ItemTipBinding

class TipAdapter : ListAdapter<Tip, TipAdapter.TipViewHolder>(TipDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : TipViewHolder = TipViewHolder.inflateFrom(parent)
    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    class TipViewHolder(val binding: ItemTipBinding)
        : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun inflateFrom(parent: ViewGroup): TipViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTipBinding.inflate(layoutInflater, parent, false)
                return TipViewHolder(binding)
            }
        }
        fun bind(item: Tip) {
            binding.tip = item
        }
    }
}
//class TipAdapter : ListAdapter<Tip, TipAdapter.TipViewHolder>(TipDiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tip, parent, false)
//        return TipViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
//        val tip = getItem(position)
//        holder.bind(tip)
//    }
//
//    inner class TipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
//        private val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
//
//        fun bind(tip: Tip) {
//            titleTextView.text = tip.title
//            contentTextView.text = tip.content
//        }
//    }
//
//    private class TipDiffCallback : DiffUtil.ItemCallback<Tip>() {
//        override fun areItemsTheSame(oldItem: Tip, newItem: Tip): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: Tip, newItem: Tip): Boolean {
//            return oldItem == newItem
//        }
//    }
//}
