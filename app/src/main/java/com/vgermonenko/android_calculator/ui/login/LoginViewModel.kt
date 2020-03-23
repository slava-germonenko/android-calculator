package com.vgermonenko.android_calculator.ui.login

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.vgermonenko.android_calculator.MainActivity
import com.vgermonenko.android_calculator.data.LoginRepository

import com.vgermonenko.android_calculator.R

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val loginSucceeded = loginRepository.login(username, password)

        if (loginSucceeded) {
            _loginResult.value =
                LoginResult(success = true)
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
    }
}
