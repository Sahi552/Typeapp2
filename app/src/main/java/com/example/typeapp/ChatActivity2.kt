package com.example.typeapp

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.typeapp.Adopter.MessageAdopter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity2 : AppCompatActivity() {

    //initialize all necessary variable with its type

    private lateinit var msgRecyclerView: RecyclerView
    private lateinit var msgbox :EditText
    private lateinit var sendbtn : ImageView
    private lateinit var messageAdopter: MessageAdopter
    private lateinit var messageList: ArrayList<com.example.typeapp.Models.Message>
    private lateinit var dbref : DatabaseReference

    private lateinit var  view: ImageView
    private lateinit var nickname : TextView

    //creating room so that others can't see our chat
    var receiverRoom: String? = null        //receiver room
    var senderRoom : String?= null          //sender room


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat2)

        //assigning values to variables

        view = findViewById(R.id.profileview)
        nickname = findViewById(R.id.Nickname)

        //getting name,uid,image from adopter   so we can see to whom we are chatting
        val name = intent.getStringExtra("name")
        val receiveruid = intent.getStringExtra("uid")
        val image = intent.getStringExtra("image")

        //Using Glide to work with image
        Glide.with(applicationContext)
            .load(image)
            .into(view)
        nickname.text = name.toString()

        messageList = ArrayList()       //array
        messageAdopter = MessageAdopter(this,messageList)
        msgRecyclerView = findViewById(R.id.Recyclerview2)
        msgRecyclerView.layoutManager = LinearLayoutManager(this)
        msgRecyclerView.adapter = messageAdopter

        val senderuid = FirebaseAuth.getInstance().currentUser?.uid
        dbref = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiveruid + senderuid
        receiverRoom = senderuid + receiveruid


        msgRecyclerView = findViewById(R.id.Recyclerview2)
        msgbox = findViewById(R.id.messageEditText)
        sendbtn = findViewById(R.id.sendButton)

        //adding data to recycler view
        dbref.child("Chats").child(senderRoom!!).child("message")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(com.example.typeapp.Models.Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdopter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        //adding a message to database while send button is pressed
        sendbtn.setOnClickListener {

            val message = msgbox.text.toString()

            val msgobject = com.example.typeapp.Models.Message(message,senderuid)

            //creating Chats reference in database which we can store both sender and receiver Room

            dbref.child("Chats").child(senderRoom!!).child("message").push()    //sender room
                .setValue(msgobject)
                .addOnSuccessListener {
                    dbref.child("Chats").child(receiverRoom!!).child("message").push()  //receiver room
                        .setValue(msgobject)
                }
            msgbox.setText("")  //once message send it clears the previous input

        }

    }

}

