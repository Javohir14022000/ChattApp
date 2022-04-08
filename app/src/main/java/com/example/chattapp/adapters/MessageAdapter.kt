package com.example.chattapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chattapp.databinding.ItemFromBinding
import com.example.chattapp.databinding.ItemToBinding
import com.example.chattapp.models.Message

class MessageAdapter(val list: List<Message>, val myUid: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val FROM = 0
    val TO = 0

    inner class FromVh(var itemFromBinding: ItemFromBinding) :
        RecyclerView.ViewHolder(itemFromBinding.root) {
        fun onBind(m: Message) {
            itemFromBinding.messageFrom.text = m.message
        }
    }

    inner class ToVh(var itemToBinding: ItemToBinding) :
        RecyclerView.ViewHolder(itemToBinding.root) {
        fun onBind(m: Message) {
            itemToBinding.messageTo.text = m.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (myUid == list[position].from) {
            return 0
        } else {
            return 1

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == FROM) {
            return FromVh(
                ItemFromBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return ToVh(ItemToBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == FROM) {
            val fromVh = holder as FromVh
            fromVh.onBind(list[position])
        } else {
            val toVh = holder as ToVh
            toVh.onBind(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}