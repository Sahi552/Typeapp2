package com.example.typeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    //declaring a variable and its type

    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var Register : TextView
    private lateinit var loginbtn : Button
    private lateinit var firebaseAuth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //assign values to declared variables

        email = findViewById(R.id.User_edit_email)
        password = findViewById(R.id.User_edit_pass)
        Register = findViewById(R.id.Regiter)
        loginbtn = findViewById(R.id.login_btn)

        firebaseAuth = FirebaseAuth.getInstance()

        //when register is clicked

        Register.setOnClickListener {

            //passing input values to variable email and pass as string
            val emailaddress = email.text.toString().trim()
            val pass = password.text.toString().trim()
            register(emailaddress, pass)    //creating function for user registration and passing email address and password
        }

        //when login button is clicked
        loginbtn.setOnClickListener {

            //passing input values to variable email and pass as string

            val emailaddress = email.text.toString().trim()
            val pass = password.text.toString().trim()
            login(emailaddress, pass)       //creating function for user login and passing email address and password
        }

    }

    //regiter function
    private fun register(emailaddress: String, pass: String) {
        if(emailaddress == "" || pass == "") {
            Toast.makeText(this,"email and password needed",Toast.LENGTH_SHORT).show() //email and password should not be empty
        }
        else{
            //passing values email and pass to UserActivity so that we can create new user with name and photo

            val i = Intent(this,UserActivity::class.java)
            i.putExtra("email",emailaddress)
            i.putExtra("pass",pass)
            Toast.makeText(this,"New User",Toast.LENGTH_SHORT).show()
            startActivity(i)
        }
    }

    //login function

    private fun login(emailaddress: String, pass: String) {

        //if user already present login with user otherwise not
        firebaseAuth.signInWithEmailAndPassword(emailaddress,pass)
            .addOnCompleteListener(this){task->
                if(task.isSuccessful){
                    val intent = Intent(this,ChatActivity::class.java)
                    finish()
                    intent.putExtra("userid",firebaseAuth.currentUser?.uid.toString().trim())  //passing uid to Chat activity so that we can find current user
                    startActivity(intent)
                }
                else{ //exception
                    Log.d("TAG","Login Failed User Does Not Exist")
                }
            }
    }
}
