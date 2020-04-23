package com.thesherlockhomie.kmessage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessagesOverviewActivity : AppCompatActivity() {

    companion object {
        var user: User? = null
        val LOGGING_KEY = "MessagesOverviewActivit"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages_overview)

        verifyUserLoggedIn()

        getCurrentUser()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_messages_overview, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.newmessage_menu_item -> {
                var intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }

            R.id.logout_menu_item -> {
                logout()
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun verifyUserLoggedIn() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun getCurrentUser() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            var ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {
                    user = p0.getValue(User::class.java)
                    if (user == null) {
                        Log.e(LOGGING_KEY, "User found was null!")
                        Toast.makeText(
                            this@MessagesOverviewActivity,
                            "USer found was null!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.e(LOGGING_KEY, "Could not get current user: ${p0.message}")
                    Toast.makeText(
                        this@MessagesOverviewActivity,
                        "Could not get current user: ${p0.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
