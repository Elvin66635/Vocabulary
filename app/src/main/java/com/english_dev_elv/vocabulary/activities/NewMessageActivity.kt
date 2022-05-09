package com.english_dev_elv.vocabulary.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.english_dev_elv.vocabulary.R
import com.english_dev_elv.vocabulary.databinding.ActivityMainBinding
import com.english_dev_elv.vocabulary.databinding.ActivityNewMessageBinding
import com.english_dev_elv.vocabulary.databinding.UserRowNewMessageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

private const val TAG = "NewMessageActivity"
class NewMessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewMessageBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMessageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.title = "Выберите контакт"

        val adapter = GroupAdapter<ViewHolder>()

   //     adapter.add(UserItem())
        binding.recyclerviewNewMessage.adapter = adapter

        fetchUsers()


    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                snapshot.children.forEach {
                    Log.d(TAG, "onDataChange: $it.toString()")
                    val user = it.getValue(User::class.java)
                    if (user != null){
                        adapter.add(UserItem(user))
                    }

                }
                binding.recyclerviewNewMessage.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: ${error.message}")
            }

        })
    }

class UserItem(val user: User): Item<ViewHolder>() {


    override fun bind(viewHolder: ViewHolder, position: Int) {
     //   var usernameTxt = viewHolder.itemView.

    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}
}