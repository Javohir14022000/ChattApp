package com.example.chattapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chattapp.databinding.FragmentRegistritionBinding
import com.example.chattapp.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class RegistrationFragment : Fragment(R.layout.fragment_registrition) {
    lateinit var auth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference


    private val binding: FragmentRegistritionBinding by viewBinding(FragmentRegistritionBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        auth = FirebaseAuth.getInstance()

        binding.apply {
            btnSign.setOnClickListener {
                val intent = mGoogleSignInClient.signInIntent
                openResultFragment.launch(intent)
            }

            btnSignOut.setOnClickListener {
                mGoogleSignInClient.signOut()
            }
        }
    }

    private val openResultFragment =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            handleSignInResult(task)
        }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken ?: "")
        } catch (e: ApiException) {
            Toast.makeText(requireContext(), "e.message", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(
                        requireContext(),
                        "Success ${user?.displayName}",
                        Toast.LENGTH_SHORT
                    ).show()

                    reference.child(user?.uid.toString()).child("uid").get()
                        .addOnSuccessListener {
                            val value = it.value.toString()
                            if (value != "null") {
                                findNavController().navigate(R.id.homeFragment)
                            } else {
                                val myUser = User(
                                    user?.displayName ?: "",
                                    user?.photoUrl.toString(),
                                    user?.email ?: "",
                                    user?.uid ?: "",
                                    user?.isAnonymous ?: true
                                )
                                reference.child(myUser.uid).setValue(myUser)
                                    .addOnSuccessListener {
                                        findNavController().navigate(R.id.homeFragment)
                                    }
                            }
                        }

                } else {
                    Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }


}