package com.example.dailymanagementapp.authFragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.dailymanagementapp.R
import com.example.dailymanagementapp.Utils
import com.example.dailymanagementapp.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding:FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        binding.goToLoginPage.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        auth = FirebaseAuth.getInstance()

        val name = binding.registerName.text.toString().trim()

        binding.registerButton.setOnClickListener {
            val email = binding.registerEmail.text.toString().trim()
            val password = binding.registerPassword.text.toString().trim()

            if(email.isEmpty() || password.isEmpty()){
                Utils.showToast(requireContext() , "Please fill both filled!")
            }
            else{
                registerWithEmailAndPassword(email , password)
            }
        }
        return binding.root
    }

    private fun registerWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email , password).addOnCompleteListener { task->
            if(task.isSuccessful){
                auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                    Utils.showToast(requireContext(), "Please verify your email!")
                    //Save Data on firebaseDatabase
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }?.addOnFailureListener {
                    Utils.showToast(requireContext() , "Please enter valid email!")
                }
//                Utils.showToast(requireContext() , "user created successfully!")
            }
            else Utils.showToast(requireContext() , "failed to register")
        }
    }
}