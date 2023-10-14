package com.example.typeapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.typeapp.Image.ImageUploaf
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class UserActivity : AppCompatActivity() {

    //declaring a variable and its type
    private lateinit var iminbtn : Button
    private lateinit var img : ImageView
    private lateinit var username : EditText
    private lateinit var imagebtn : FloatingActionButton
    private var selectedImageUri: Uri? = null

    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val imageRef = storage.reference.child("images")


    //getting image from user
    val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null && data.data != null) {
                    // Get the selected image URI
                    selectedImageUri = data.data
                    // Display the selected image in the ImageView
                    img.setImageURI(selectedImageUri)
                }
            }
        }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getContent.launch(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        //assign values to declared variables

        iminbtn = findViewById(R.id.button)
        imagebtn = findViewById(R.id.floatingActionButton)
        img = findViewById(R.id.ImageView)
        username = findViewById(R.id.User_edit_name)


        imagebtn.setOnClickListener {
            openImageChooser()  //inage picker function
        }


        iminbtn.setOnClickListener {

            val email = intent.getStringExtra("email")
            val pass = intent.getStringExtra("pass")

            if (email != null && pass != null) {
                newUser(email,pass) //registering new user function with email and pass
            }else{
                Toast.makeText(this, "null email and password", Toast.LENGTH_SHORT).show()
            }

        }

    }

    //registration function

    private fun newUser(emailaddress: String, pass: String) {
        val username = username.text.toString().trim()
        //create new user with email and pass

        firebaseAuth.createUserWithEmailAndPassword(emailaddress,pass)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    Toast.makeText(this,"New User Added",Toast.LENGTH_SHORT).show()
                    adddatatodatabase() //adding user name,inage adn uid to database both firestore and realtime
                    val i = Intent(this,MainActivity::class.java)
                    startActivity(i)
                }else{
                    Log.d("TAG","Creating New User is Failed Try Again")
                }
            }
    }

    //function to add data
    private fun adddatatodatabase() {

        val username = username.text.toString().trim()  //username
        val userid = firebaseAuth.currentUser?.uid.toString()   //uid

        if (username.isNotEmpty() && selectedImageUri != null){

            val imageRef = imageRef.child(selectedImageUri!!.lastPathSegment!!)
            val uploadTask = imageRef.putFile(selectedImageUri!!)   //upload image to storage

            uploadTask.addOnCompleteListener{task ->    //on complete uploading
                if (task.isSuccessful){

                    imageRef.downloadUrl.addOnSuccessListener { uri ->  //download
                        val imageupload = ImageUploaf(uri.toString(),username,userid)   //uploading uri,name,userid
                        firestore.collection("Users").add(imageupload)  //to firestore

                        val databaseRef = database.reference.child("Users") //database reference
                        val key = databaseRef.push().key    //add data

                        if(key != null){
                            databaseRef.child(key).setValue(imageupload)    //to database
                        }
                        Toast.makeText(this, "Upload successful", Toast.LENGTH_SHORT).show()    //complete upload
                    }
                }else{
                    Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show()    //failure upload
                }
            }
        }else{
            Toast.makeText(this, "Name and image selection required", Toast.LENGTH_SHORT).show()    //empty fields
        }
    }
}


