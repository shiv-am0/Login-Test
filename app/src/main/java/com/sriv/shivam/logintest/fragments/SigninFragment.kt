package com.sriv.shivam.logintest.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sriv.shivam.logintest.MainActivity
import com.sriv.shivam.logintest.R
import com.sriv.shivam.logintest.databinding.FragmentSigninBinding
import com.sriv.shivam.logintest.models.LoginStatus
import com.sriv.shivam.logintest.viewModel.MainViewModel
import com.sriv.shivam.logintest.viewModel.MainViewModelFactory

class SigninFragment : Fragment() {
    private lateinit var binding: FragmentSigninBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(layoutInflater)

        // Initialize MainViewModel and passing context as an argument
        mainViewModel = ViewModelProvider(this,
            MainViewModelFactory(context as Activity)
        )[MainViewModel::class.java]

        // Getting firebase instance for further procedure
        mainViewModel.getFirebaseInstance()

        // Navigate to RegisterFragment when user clicks on Register
        binding.registerClick.setOnClickListener {
            findNavController().navigate(R.id.action_signinFragment_to_registerFragment)
        }

        // Sign in user using FirebaseAuth when user clicks Sign In button
        binding.signinButton.setOnClickListener {
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
                    mainViewModel.signInUser(email, password).observe(viewLifecycleOwner) {
                        // Follow respective instructions based on the Login Status
                        when(it) {
                            LoginStatus.LoggedIn -> {
                                binding.emailEditText.text?.clear()
                                binding.passwordEditText.text?.clear()
                                Toast.makeText(requireContext(), "Sign In successful", Toast.LENGTH_SHORT).show()
                                updateUI()
                            }

                            is LoginStatus.LoginFailed -> Toast.makeText(requireContext(), "Sign In Failed ${it.error}",
                                Toast.LENGTH_SHORT).show()

                            LoginStatus.LoggingInProgress -> Toast.makeText(requireContext(), "Logging in\nPlease wait...",
                                Toast.LENGTH_SHORT).show()

                            else -> Log.d("test", "Not Logged in. Please try again.")
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