package com.maxadri.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxadri.login.LoginForm
import com.maxadri.todo.R
import kotlinx.android.synthetic.main.fragment_login.*

class Login : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private fun login() {
        val userLogin = LoginForm(email = emailLogin.text.toString(), password = passwordLogin.text.toString())
    }

}
