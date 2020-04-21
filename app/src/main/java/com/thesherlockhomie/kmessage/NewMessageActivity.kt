package com.thesherlockhomie.kmessage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.layout_userrow_newmessage.view.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        val adapter = GroupAdapter<GroupieViewHolder>()
        recyclerview_view_newmessage.adapter = adapter
        fetchUsers()
    }

    private fun fetchUsers() {
        var ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var adapter = GroupAdapter<GroupieViewHolder>()
                p0.children.forEach {
                    Log.d("NewMessageActivity", "$it")
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    var intent = Intent(view.context, ChatLogActivity::class.java)
                    intent.putExtra("USER_KEY", userItem.user)
                    startActivity(intent)
                    finish()
                }
                recyclerview_view_newmessage.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e("NewMessageActivity", "Failed to fetch users: ${p0.message}")
            }
        })
    }
}

class UserItem(val user: User) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.layout_userrow_newmessage
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_text_newmessage.text = user.username.toString()
        Picasso.get().load(user.profilePhotoUrl.toString())
            .into(viewHolder.itemView.userphoto_image_newmessage)
    }
}