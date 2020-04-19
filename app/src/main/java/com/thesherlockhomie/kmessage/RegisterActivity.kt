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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class User(val uid: String, val username: String, val profilePhotoUrl: String)

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
            val username = username_edittext_register.text.toString()

            //if username empty
            if (username.isEmpty()) {
                Toast.makeText(this, "Username cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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
                    uploadPhotoToFB() //which also adds user to db and launches messages overview activity

                }
                .addOnFailureListener {
                    //handle failure
                    Log.d("RegisterActivity", "Failed to create user: ${it.message}")
                    Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun uploadPhotoToFB(): Uri? {
        if (selectedPhotoUri == null)
            return null

        val filename = UUID.randomUUID().toString()
        var photoUrl: Uri? = null
        var storageRef = Firebase.storage.reference.child("/photos/$filename")
        storageRef.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Uploaded image with filename $filename")
                storageRef.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity", "Image location: $it")
                    photoUrl = it
                    saveUserToFb(photoUrl!!)
                }
            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Failed to upload image: ${it.message}")
                Toast.makeText(this, "Failed to upload image: ${it.message}", Toast.LENGTH_SHORT)
            }
        return photoUrl
    }

    private fun saveUserToFb(photoUrl: Uri) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val username = username_edittext_register.text.toString()
        val user = User(uid, username, photoUrl.toString())

        var dbRef = Firebase.database.getReference("/users/$uid")
        dbRef.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Saved user to database")

                //launch messages overview activity
                var intent = Intent(this, MessagesOverviewActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Could not save user to database :(")
            }
    }
}

