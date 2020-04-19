package com.thesherlockhomie.kmessage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //allow user to select photo
        setPhoto()

        //register account
        registerAccount()

        //user taps on log in text
        login_text_register.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RegisterActivity", "User selected photo")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            addphoto_btn_register.alpha = 0f
            photo_image_register.setImageBitmap(bitmap)
        }
    }

    private fun setPhoto() {
        addphoto_btn_register.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    private fun registerAccount() {
        register_btn_register.setOnClickListener {
            val email = email_edittext_register.text.toString()
            val password = password_edittext_register.text.toString()

            //if username/pass is empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email/password cannot be blank!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("RegisterActivity", "Email is $email")
            Log.d("RegisterActivity", "Password is $password")

            var auth: FirebaseAuth = FirebaseAuth.getInstance()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    //else if successful
                    Log.d(
                        "RegisterActivity",
                        "Successfuly created user with uid: ${it.result?.user?.uid}"
                    )
                }
                .addOnFailureListener {
                    //handle failure
                    Log.d("RegisterActivity", "Failed to create user: ${it.message}")
                    Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
