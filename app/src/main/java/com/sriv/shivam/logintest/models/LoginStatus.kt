package com.sriv.shivam.logintest.models

/* Sealed class is a class which has fixed or restricted hierarchy.
   This sealed class is used to get the Login Status in the form of LiveData which is later
   observed by respective fragments and UI changes are done according to that.
 */
sealed class LoginStatus {
    object NotLoggedIn: LoginStatus()
    class LoginFailed(val error: String): LoginStatus()
    object LoggedIn: LoginStatus()
    object LoggingInProgress: LoginStatus()
}
