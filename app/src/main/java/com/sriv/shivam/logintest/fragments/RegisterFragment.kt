package com.sriv.shivam.logintest.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sriv.shivam.logintest.MainActivity
import com.sriv.shivam.logintest.databinding.FragmentRegisterBinding
import com.sriv.shivam.logintest.models.LoginStatus
import com.sriv.shivam.logintest.viewModel.MainViewModel
import com.sriv.shivam.logintest.viewModel.MainViewModelFactory

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        // Initialize MainViewModel and passing context as an argument
        mainViewModel = ViewModelProvider(this,
            MainViewModelFactory(context as Activity)
        )[MainViewModel::class.java]

        // Getting firebase instance for further procedure
        mainViewModel.getFirebaseInstance()

        // Register user to Firebase when user clicks Register button
        binding.registerButton.setOnClickListener {
            // Store the value from edit texts into string variables
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            // If any field is blank, ask user to fill it
            when {
                TextUtils.isEmpty(email) -> {
                    Toast.makeText(
                        requireContext(),
                        "Please enter email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(password) -> {
                    Toast.makeText(
                        requireContext(),
                        "Please enter password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // If user filled every required field, register user in View Model
                else -> {
                    Log.d("test", "In else block")
                    mainViewModel.registerUser(email, password).observe(viewLifecycleOwner) {
                        // Follow respective instructions based on the Login Status
                        when(it) {
                            LoginStatus.LoggedIn -> {
                                binding.emailEditText.text?.clear()
                                binding.passwordEditText.text?.clear()
                                Toast.makeText(requireContext(), "Account Created", Toast.LENGTH_SHORT).show()
                                updateUI()
                            }

                            is LoginStatus.LoginFailed -> Toast.makeText(requireContext(), "Register Failed ${it.error}",
                                Toast.LENGTH_SHORT).show()

                            else -> Log.d("test", "Not registered. Please try again.")
                        }
                    }

                }

            }

        }

        return binding.root
    }

    // This function is used to navigate to MainActivity and update the UI accordingly.
    private fun updateUI() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

}