package com.example.chattapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chattapp.databinding.ItemUsersBinding
import com.example.chattapp.models.User
import com.squareup.picasso.Picasso

class UserAdapter(var list: List<User>, val listener: OnItemClickListener) :
    RecyclerView.Adapter<UserAdapter.Vh>() {

    inner class Vh(private val itemUsersBinding: ItemUsersBinding) :
        RecyclerView.ViewHolder(itemUsersBinding.root) {

        fun onBind(user: User) {
            Picasso.get().load(user.photoUrl).into(itemUsersBinding.imageUser)
            itemUsersBinding.apply {
                tvName.text = user.displayName
//                messageNumber.text = list.size.toString()
                itemView.setOnClickListener {
                    listener.onItemClick(user.uid, user.displayName, user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener {
        fun onItemClick(itemUserUid: String, itemUserName: String, user: User)
    }
}