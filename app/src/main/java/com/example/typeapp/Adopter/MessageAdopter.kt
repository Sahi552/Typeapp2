package com.example.typeapp.Adopter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.typeapp.Models.Message
import com.example.typeapp.R
import com.google.firebase.auth.FirebaseAuth

class MessageAdopter(
    val context: Context,
    val messageList: ArrayList<Message>
):RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    val Item_Receive = 1;
    val Item_Sent = 2;



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType ==1){
            //receive item
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            return ReceiveViewHolder(view)
        }else{
            //sent item
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return SentViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentmessage = messageList[position]

        if(holder.javaClass == SentViewHolder::class.java){
            //sent view holder
            val viewHolder = holder as SentViewHolder
            holder.sentmessage.text = currentmessage.message
        }
        else{
            //receive view holder
            val viewHolder = holder as ReceiveViewHolder
            holder.receivemessage.text = currentmessage.message
        }

    }

    override fun getItemViewType(position: Int): Int {

        val currentmessage = messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentmessage.senderid)){
            return Item_Sent
        }else{
            return Item_Receive
        }
    }

    class SentViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val sentmessage = itemView.findViewById<TextView>(R.id.sentmessage) //store sender message to sender room
    }
    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val receivemessage = itemView.findViewById<TextView>(R.id.receivemessage)   //store receiver message to receiver roo,
    }
}

