package com.example.chattapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.chattapp.adapters.UserAdapter
import com.example.chattapp.databinding.FragmentChatsBinding
import com.example.chattapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class ChatsFragment : Fragment(R.layout.fragment_chats) {
    private val binding: FragmentChatsBinding by viewBinding(FragmentChatsBinding::bind)
    lateinit var reference: DatabaseReference
    lateinit var database: FirebaseDatabase
    lateinit var userAdapter: UserAdapter
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var list: ArrayList<User>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("users")
        binding.apply {

            list = ArrayList()
            userAdapter = UserAdapter(list, object : UserAdapter.OnItemClickListener {
                override fun onItemClick(itemUserUid: String, itemUserName: String, user: User) {
                    val bundle = Bundle()
                    bundle.putSerializable("user", user.photoUrl)
                    bundle.putSerializable("itemUserId", itemUserUid)
                    bundle.putSerializable("itemUserName", itemUserName)
                    bundle.putSerializable("isOnline", user.isOnline)
                    findNavController().navigate(R.id.messageFragment, bundle)
                }


            })
            rv.adapter = userAdapter

            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot.children
                    list.clear()
                    for (child in children) {
                        val value = child.getValue(User::class.java)
                        if (value != null && value.uid != firebaseAuth.uid) {
                            list.add(value)
                        }
                    }
                    userAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

}