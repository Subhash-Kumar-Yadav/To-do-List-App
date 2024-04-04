package com.example.dailymanagementapp.authFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.dailymanagementapp.HomeActivity
import com.example.dailymanagementapp.R
import com.example.dailymanagementapp.Utils
import com.example.dailymanagementapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()

        binding.createAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.googleButton.setOnClickListener {
            Utils.showToast(requireContext(), "Not available!")
        }



        binding.loginButton.setOnClickListener {
            val email = binding.registerEmail.text.toString()
            val password = binding.registerPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Utils.showToast(requireContext(), "Please enter valid data$email or $password")
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userVerification = auth.currentUser?.isEmailVerified
                        if(userVerification == true){
                            Utils.showToast(requireContext(), "login successfully!")
                            val intent = Intent(requireContext(), HomeActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                        else{
                            Utils.showToast(requireContext() , "Please verify your email!")
                        }
                    }
                    else{
                        Utils.showToast(requireContext() , "Please enter correct details")
                    }
                }
            }
        }

        return binding.root
    }
}