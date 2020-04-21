package com.thesherlockhomie.kmessage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.layout_chatitem_rec.view.*
import kotlinx.android.synthetic.main.layout_chatitem_sent.view.*

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val user = intent.getParcelableExtra<User>("USER_KEY")
        supportActionBar?.title = user.username.toString()

        val adapter = GroupAdapter<GroupieViewHolder>()
        adapter.add(ChatItemRec())
        adapter.add(ChatItemSent())
        adapter.add(ChatItemRec())
        adapter.add(ChatItemSent())
        adapter.add(ChatItemRec())
        adapter.add(ChatItemSent())
        adapter.add(ChatItemRec())
        adapter.add(ChatItemSent())
        adapter.add(ChatItemRec())
        adapter.add(ChatItemSent())
        adapter.add(ChatItemRec())
        adapter.add(ChatItemSent())

        recyclerview_view_chatlog.adapter = adapter
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