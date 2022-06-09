package com.capstone.dressonme.ui.callback

import androidx.recyclerview.widget.DiffUtil
import com.capstone.dressonme.model.remote.response.ProcessItem

class RecomendCallback(private val oldPictureList: List<ProcessItem>, private val newPictureList : List<ProcessItem>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldPictureList.size
    }

    override fun getNewListSize(): Int {
        return newPictureList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldPictureList[oldItemPosition] == newPictureList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItems = oldPictureList[oldItemPosition]
        val newItems = newPictureList[newItemPosition]
        return oldItems.id == newItems.id && oldItems.linkResult == newItems.linkResult
    }
}