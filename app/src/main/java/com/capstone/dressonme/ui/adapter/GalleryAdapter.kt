package com.capstone.dressonme.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.dressonme.databinding.ItemGalleryBinding
import com.capstone.dressonme.model.Picture
import com.capstone.dressonme.model.remote.response.ProcessItem
import com.capstone.dressonme.ui.callback.ItemsCallback
import com.capstone.dressonme.ui.callback.RecomendCallback


class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    private val listItemsGallery = ArrayList<ProcessItem>()

    fun setListGallery(listItems : ArrayList<ProcessItem>) {
        val diffListCallback = RecomendCallback(this.listItemsGallery, listItems)
        val resultCallback = DiffUtil.calculateDiff(diffListCallback)
        this.listItemsGallery.clear()
        this.listItemsGallery.addAll(listItems)
        resultCallback.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(val binding: ItemGalleryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val dataPosition = listItemsGallery[position]
        Glide.with(context).load(dataPosition.linkResult).into(holder.binding.imageGallery)
    }

    override fun getItemCount(): Int = listItemsGallery.size
}