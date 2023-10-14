package com.example.typeapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.typeapp.Adopter.UserAdopter
import com.example.typeapp.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {

    //initialize all variables privately
    private lateinit var userrecyclerview : RecyclerView
    private lateinit var userlist : ArrayList<User>
    private lateinit var adopter: UserAdopter
    private lateinit var dbref : DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        firebaseAuth = FirebaseAuth.getInstance()   //initialize firebase

        val id = intent.getStringExtra("userid")    //getting user id from mainacticity

        //assigning values to variables which are initialized above
        userlist = ArrayList()         //array element
        adopter = UserAdopter(this,userlist)    //adopter reference

        userrecyclerview = findViewById(R.id.Recyclerview)  //recycler view
        userrecyclerview.layoutManager = LinearLayoutManager(this)
        userrecyclerview.setHasFixedSize(true)

        dbref = FirebaseDatabase.getInstance().getReference("Users")       //initialize database so that we can use it wisely
        dbref.addValueEventListener(object :ValueEventListener{     //declaring objects
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {        //check if exists
                    userlist.clear()    //we want to clear the previous list
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)   //user variable getting values from User class
                        if (id != user?.uid){
                            userlist.add(user!!)    //adding user to recyclerview
                        }
                    }
                    adopter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {    //if there is no data reference present with "Users"
                Toast.makeText(applicationContext,"Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        })
        userrecyclerview.adapter = adopter      //adopter value to recycler view
    }

}