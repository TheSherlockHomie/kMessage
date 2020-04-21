package com.thesherlockhomie.kmessage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val user = intent.getParcelableExtra<User>("USER_KEY")
        supportActionBar?.title = user.username.toString()
    }
}
