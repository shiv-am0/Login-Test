package com.sriv.shivam.logintest.viewModel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.sriv.shivam.logintest.StartupActivity
import com.sriv.shivam.logintest.models.LoginStatus
import com.sriv.shivam.logintest.models.User

class MainViewModel(context: Context): ViewModel() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private val loginStatusLiveData: MutableLiveData<LoginStatus> = MutableLiveData<LoginStatus>()
        .apply {
        postValue(LoginStatus.NotLoggedIn)
    }

    fun getFirebaseInstance() {
        auth = Firebase.auth
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
//        currentUser = auth.currentUser!!
    }

    /* Below function is used to register a new user in database. It takes email and password as
       parameters and creates a record using Firebase User ID as primary key. If registration is
       successful, it will return live data with Login Status as LoggedIn, else it will return
       live data with Login Status as LoggedFailed along with the error. Based on the returned value,
       UI is updated and respective toast is showed to user.
     */
    fun registerUser(email: String, password: String): LiveData<LoginStatus> {
        loginStatusLiveData.postValue(LoginStatus.LoggingInProgress)
        Log.d("test", "In registerUser()")

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            Log.d("test", "Inside onComplete")
            if(task.isSuccessful) {
                Log.d("test", "create user completed")
                val userID = auth.currentUser!!.uid
                Log.d("test", "UserId: $userID")
                val user = User(userID, email, password)

                databaseReference.child(userID.toString()).setValue(user).addOnSuccessListener {
                    loginStatusLiveData.postValue(LoginStatus.LoggedIn)
                }.addOnFailureListener {
                    loginStatusLiveData.postValue(LoginStatus.LoginFailed(it.localizedMessage))
                }
            }
            else {
                Log.d("test", "User not created: ${task.exception.toString()}")
            }
        }
        Log.d("test", "LiveData: $loginStatusLiveData")
        return loginStatusLiveData
    }

    /* Below function is used to sign in user using Firebase Auth. It takes email and password as
       parameters and sign in using them. If sign in is successful, it will return live data with
       Login Status as LoggedIn, else it will return live data with Login Status as LoggedFailed
       along with the error. Based on the returned value, UI is updated and respective toast
       is showed to user.
     */
    fun signInUser(email: String, password: String): LiveData<LoginStatus> {
        Log.d("test", "Inside signInUser()")
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            loginStatusLiveData.postValue(LoginStatus.LoggedIn)
            Log.d("test", "Sign In successful!\nLiveData: $loginStatusLiveData")
        }.addOnFailureListener {
            loginStatusLiveData.postValue(LoginStatus.LoginFailed(it.localizedMessage))
            Log.d("test", "Sign In failed!\nLiveData: $loginStatusLiveData")
        }
        return loginStatusLiveData
    }

    // Function to signOut from current auth
    fun signOut() {
        Firebase.auth.signOut()
    }
}