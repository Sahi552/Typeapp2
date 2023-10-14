package com.example.typeapp.Adopter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.typeapp.ChatActivity
import com.example.typeapp.ChatActivity2
import com.example.typeapp.Models.User
import com.example.typeapp.R

class UserAdopter(
    val context: ChatActivity,
    private var userList: ArrayList<User>
) :RecyclerView.Adapter<UserAdopter.UserViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.chatrecyclerview,parent,false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val currentUser = userList[position]

        holder.name.text = currentUser.name
        Glide.with(holder.itemView)
            .load(currentUser.imageurl)
            .into(holder.Image)
        holder.itemView.setOnClickListener {

            val i = Intent(context,ChatActivity2::class.java)
            i.putExtra("name",currentUser.name)
            i.putExtra("image",currentUser.imageurl)
            i.putExtra("uid",currentUser.uid)
            context.startActivity(i)
        }

    }

    class UserViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val name : TextView =  itemView.findViewById(R.id.Nickname)
        val Image : ImageView = itemView.findViewById(R.id.profileview)
    }
}