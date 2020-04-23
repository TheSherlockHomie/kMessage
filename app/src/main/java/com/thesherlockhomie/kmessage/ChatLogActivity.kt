package com.thesherlockhomie.kmessage

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.layout_chatitem_rec.view.*
import kotlinx.android.synthetic.main.layout_chatitem_sent.view.*
import java.lang.System.currentTimeMillis

class ChatLogActivity : AppCompatActivity() {
    companion object {
        const val LOGGING_KEY = "ChatLogActivity"
    }

    val adapter = GroupAdapter<GroupieViewHolder>()
    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        toUser = intent.getParcelableExtra<User>("USER_KEY")
        supportActionBar?.title = toUser?.username.toString()

        recyclerview_view_chatlog.adapter = adapter

        sendMessages()

        fetchMessages()

    }

    private fun fetchMessages() {
        val to = toUser?.uid.toString()
        val from = MessagesOverviewActivity.user?.uid.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$to/$from")
        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chat = p0.getValue(Message::class.java)
                val currentUser = MessagesOverviewActivity.user ?: return
                val me = currentUser.uid
                if (chat != null) {
                    if (chat.from == me) {
                        adapter.add(ChatItemSent(chat, currentUser))
                    } else {
                        adapter.add(ChatItemRec(chat, toUser ?: return))
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e(LOGGING_KEY, "Couldn\'t get messages: ${p0.message}")
                Toast.makeText(
                    this@ChatLogActivity,
                    "Couldn\'t get messages: ${p0.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })
    }

    private fun sendMessages() {
        send_btn_chatlog.setOnClickListener {
            val from = FirebaseAuth.getInstance().uid.toString()
            val text = message_edittext_chatlog.text.toString()
            val to = intent.getParcelableExtra<User>("USER_KEY").uid.toString()
            if (text != null && from != null) {
                val ref =
                    FirebaseDatabase.getInstance().getReference("/user-messages/$from/$to").push()
                val ref2 =
                    FirebaseDatabase.getInstance().getReference("/user-messages/$to/$from").push()
                val id = ref.key.toString()
                val chat = Message(id, text, from, to, currentTimeMillis())
                ref.setValue(chat)
                    .addOnCompleteListener {
                        Log.d(LOGGING_KEY, "Sent message with id $id")
                        message_edittext_chatlog.text.clear()
                        recyclerview_view_chatlog.scrollToPosition(adapter.itemCount - 1)
                    }
                    .addOnFailureListener {
                        Log.e(LOGGING_KEY, "Failed to send message: ${it.message.toString()}")
                    }
                ref2.setValue(chat)
                    .addOnCompleteListener {
                        Log.d(LOGGING_KEY, "Sent message2 with id $id")
                    }
                    .addOnFailureListener {
                        Log.e(
                            LOGGING_KEY,
                            "Partially failed to send message: ${it.message.toString()}"
                        )
                    }
            }
        }
    }
}

class ChatItemSent(private val chat: Message, val user: User) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.layout_chatitem_sent
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_text_chatitem_sent.text = chat.text.toString()
        val imageUrl = user.profilePhotoUrl.toString()
        val target = viewHolder.itemView.profilephoto_image_chatitem_sent
        Picasso.get().load(imageUrl).into(target)
    }

}

class ChatItemRec(private val chat: Message, val user: User) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.layout_chatitem_rec
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_text_chatitem_rec.text = chat.text.toString()
        val imageUrl = user.profilePhotoUrl.toString()
        val target = viewHolder.itemView.profilephoto_image_chatitem_rec
        Picasso.get().load(imageUrl).into(target)
    }

}