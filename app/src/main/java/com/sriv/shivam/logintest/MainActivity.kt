package com.sriv.shivam.logintest

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sriv.shivam.logintest.databinding.ActivityMainBinding
import com.sriv.shivam.logintest.viewModel.MainViewModel
import com.sriv.shivam.logintest.viewModel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize MainViewModel and passing context as an argument
        mainViewModel = ViewModelProvider(this,
            MainViewModelFactory(this)
        )[MainViewModel::class.java]

        binding.signOut.setOnClickListener {
            showDialog()
        }
    }

    // Function to invoke Dialog Box for logging out
    private fun showDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("YES", object: DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    mainViewModel.signOut()
                    updateUI()
                }
            })
            .setNegativeButton("NO", object: DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    Toast.makeText(this@MainActivity, "Sign Out cancelled",
                        Toast.LENGTH_SHORT).show()
                }
            })
            .show()
    }

    private fun updateUI() {
        val intent = Intent(this, StartupActivity::class.java)
        startActivity(intent)
        finish()
    }
}