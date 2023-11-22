package com.dicoding.storyku.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyku.data.response.ListStoryResponse
import com.dicoding.storyku.databinding.ListStoryBinding
import com.dicoding.storyku.ui.detail.DetailstoryActivity

class MainAdapter :
    PagingDataAdapter<ListStoryResponse, MainAdapter.MyViewHolder>(DIFF_CALLBACK) {

    inner class MyViewHolder(private val binding: ListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemName: ListStoryResponse) {
            binding.tvItemName.text = itemName.name
            Glide.with(binding.root)
                .load(itemName.photoUrl)
                .into(binding.ivItemPhoto)
            binding.root.setOnClickListener {
                val intentDetail = Intent(binding.root.context, DetailstoryActivity::class.java)
                intentDetail.putExtra(DetailstoryActivity.ID, itemName.id)
                intentDetail.putExtra(DetailstoryActivity.NAME, itemName.name)
                intentDetail.putExtra(DetailstoryActivity.DESCRIPTION, itemName.description)
                intentDetail.putExtra(DetailstoryActivity.PICTURE, itemName.photoUrl)
                binding.root.context.startActivity(intentDetail)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryResponse>() {
            override fun areItemsTheSame(oldItem: ListStoryResponse, newItem: ListStoryResponse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryResponse,
                newItem: ListStoryResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

}