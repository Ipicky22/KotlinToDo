package com.maxadri.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.maxadri.todo.R
import kotlinx.android.synthetic.main.fragment_authentication.*


class Authentication : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_authentication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logIn.setOnClickListener {
            findNavController().navigate(R.id.action_authentication_to_login)
        }

        signUp.setOnClickListener {
            findNavController().navigate(R.id.action_authentication_to_signup)
        }
    }

    companion object {
        const val LOGIN_REQUEST = 201
        const val SIGN_UP_REQUEST = 301
    }

}
