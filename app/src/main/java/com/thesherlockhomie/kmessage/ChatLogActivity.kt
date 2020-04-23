package com.thesherlockhomie.kmessage

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.layout_chatitem_rec.view.*
import kotlinx.android.synthetic.main.layout_chatitem_sent.view.*
import java.lang.System.currentTimeMillis

class ChatLogActivity : AppCompatActivity() {
    val LOGGING_KEY = "ChatLogActivity"
    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val user = intent.getParcelableExtra<User>("USER_KEY")
        supportActionBar?.title = user.username.toString()

        recyclerview_view_chatlog.adapter = adapter

        sendMessages()

        fetchMessages()

    }

    private fun fetchMessages() {
        //TODO
    }

    private fun sendMessages() {
        send_btn_chatlog.setOnClickListener {
            val from = FirebaseAuth.getInstance().uid.toString()
            val text = message_edittext_chatlog.text.toString()
            val to = intent.getParcelableExtra<User>("USER_KEY").uid.toString()
            if (text != null && from != null) {
                val ref = FirebaseDatabase.getInstance().getReference("/messages/").push()
                val id = ref.key.toString()
                val chat = Message(id, text, from, to, currentTimeMillis())
                ref.setValue(chat)
                    .addOnCompleteListener {
                        Log.d(LOGGING_KEY, "Sent message with id $id")
                    }
                    .addOnFailureListener {
                        Log.e(LOGGING_KEY, "Failed to send message: ${it.message.toString()}")
                    }
            }
        }
    }
}

class ChatItemSent : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.layout_chatitem_sent
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_text_chatitem_sent.text =
            "This is a really long message just to see if this works fine, does it? It does not.hvsdhvhhjsgchhhdsbhcvshhcvshvh"
    }

}

class ChatItemRec : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.layout_chatitem_rec
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_text_chatitem_rec.text =
            "This is a really long message just to see if this works fine, does it? It does not.hvsdhvhhjsgchhhdsbhcvshhcvshvh"
    }

}