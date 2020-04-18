package com.thesherlockhomie.kmessage

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //login user
        loginUser()

        //in case user taps register
        register_text_login.setOnClickListener {
            finish()
        }
    }

    private fun loginUser() {
        login_btn_login.setOnClickListener {
            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()

            //if username/pass is empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email/password cannot be blank!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("LoginActivity", "Email is $email")
            Log.d("LoginActivity", "Password is $password")

            var auth: FirebaseAuth = FirebaseAuth.getInstance()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    //else if successful
                    Log.d(
                        "LoginActivity",
                        "Successfully signed in user with uid: ${it.result?.user?.uid}"
                    )
                }
                .addOnFailureListener {
                    //handle failure
                    Log.d("LoginActivity", "Failed to signin: ${it.message}")
                    Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}