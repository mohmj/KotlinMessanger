package com.softwareclinics.kotlinmessanger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.activity_user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title="Select user";
//        val adapter=GroupAdapter<ViewHolder>()
//
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//
//
//        recycler_view_new_message.adapter=adapter
        fetchUsers()
    }

    private fun fetchUsers(){
        val ref=Firebase.database.reference
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adapter=GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    Log.d("newMessgaae",it.toString())
                    val user=it.getValue(User::class.java)
                    if(user != null){
                        adapter.add(UserItem(user))

                    }
                }
                recycler_view_new_message.adapter=adapter
            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }



        })
    }


    class UserItem(var user:User): Item<ViewHolder>(){
        override fun getLayout(): Int {
            return R.layout.activity_user_row_new_message
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            // We get it later .. .. .. .. .. .. ..
            viewHolder.itemView.user_new_message_username_text_view.text=user.username
            Picasso.get().load(user.imageLink).into(viewHolder.itemView.user_new_message_image_view)
        }

    }
}
