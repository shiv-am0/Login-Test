package com.sriv.shivam.logintest.models

sealed class LoginStatus {
    object NotLoggedIn: LoginStatus()
    class LoginFailed(val error: String): LoginStatus()
    object LoggedIn: LoginStatus()
    object LoggingInProgress: LoginStatus()
}
