package com.example.chattapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.chattapp.adapters.MessageAdapter
import com.example.chattapp.databinding.FragmentMessageBinding
import com.example.chattapp.models.Message
import com.example.chattapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class MessageFragment : Fragment(R.layout.fragment_message) {
    private val binding: FragmentMessageBinding by viewBinding(FragmentMessageBinding::bind)
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var reference1: DatabaseReference
    lateinit var list: ArrayList<Message>
    lateinit var messageAdapter: MessageAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")
        reference1 = firebaseDatabase.getReference("users")
        firebaseAuth = FirebaseAuth.getInstance()
        binding.apply {

            val itemUserId = arguments?.getSerializable("itemUserId") as String
            val itemUserName = arguments?.getSerializable("itemUserName") as String
            val itemUserImage = arguments?.getSerializable("user") as String
            val isOnline = arguments?.getSerializable("isOnline") as Boolean

            if(isOnline){
                online.visibility = View.VISIBLE
                ofline.visibility = View.INVISIBLE
            }

            Picasso.get().load(itemUserImage).into(imageUser)
            name.text = itemUserName
            edit.addTextChangedListener {
                if (edit.text.isNotEmpty()) {
                    imgSend.visibility = View.VISIBLE
                } else {
                    imgSend.visibility = View.INVISIBLE
                }
            }

            iconBack.setOnClickListener {
                findNavController().popBackStack()
            }



            imgSend.setOnClickListener {
                val text = binding.edit.text.toString()

                val message = Message(firebaseAuth.uid ?: "", itemUserId, text)
                val key = reference.push().key
                reference.child(firebaseAuth.uid ?: "")
                    .child("messages")
                    .child(itemUserId ?: "")
                    .child(key ?: "")
                    .setValue(message)

                reference1.child(itemUserId ?: "")
                    .child("messages")
                    .child(firebaseAuth.uid ?: "")
                    .child(key ?: "")
                    .setValue(message)

                edit.setText("")
                rv.smoothScrollToPosition(list.size)


            }

            list = ArrayList()
            messageAdapter = MessageAdapter(list, firebaseAuth.uid ?: "")

            rv.adapter = messageAdapter

            reference.child(firebaseAuth.uid ?: "").child("messages").child(itemUserId ?: "")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        list.clear()
                        val children = snapshot.children
                        for (child in children) {
                            val value = child.getValue(Message::class.java)
                            if (value != null) {
                                list.add(value)
                            }
                        }
                        messageAdapter.notifyDataSetChanged()
                        rv.smoothScrollToPosition(list.size)
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
        }
    }
}