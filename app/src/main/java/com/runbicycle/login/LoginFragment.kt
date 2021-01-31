package com.runbicycle.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.runbicycle.BaseFragment
import com.runbicycle.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_sign_in.*

class LoginFragment: BaseFragment() {
    private val fbAuth = FirebaseAuth.getInstance()
    private val LOG_DEBUG = "LOG_DEBUG"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLoginClick()
        setupRegistrationClick()
    }

    private fun setupRegistrationClick() {
        signUpButton.setOnClickListener {
            findNavController()
                .navigate(LoginFragmentDirections.actionFragmentLoginToFragmentSignUp().actionId)
        }
    }

    private fun setupLoginClick() {
        signUpButtonRegistration.setOnClickListener {
            val email = email_login.text?.trim().toString()
            val password = pass_login.text?.trim().toString()
            fbAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {authRes ->
                    if(authRes.user != null) {
                        startApp()
                    }
                }
                .addOnFailureListener {exc ->
                    Snackbar.make(requireView(), "UPS, sth went wrong...", Snackbar.LENGTH_LONG)
                        .show()
                    Log.d(LOG_DEBUG, exc.message.toString())
                }
            clearFindViewByIdCache()
        }
    }
}