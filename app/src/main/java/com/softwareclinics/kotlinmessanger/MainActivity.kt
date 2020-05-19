package com.softwareclinics.kotlinmessanger

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.getInstance
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var firebaseDatebase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Function to upload the rigestration information the database (picture, name, email and the password).
        sign_up_button.setOnClickListener(){
            performSignUp()
        }

            // Go to the Login page.
        sign_up_doYouHaveAnCcount_text_view.setOnClickListener(){
            val signInIntent= Intent(this,LogInActivity::class.java);
            startActivity(signInIntent);
        }
        //----------------------------------------------------------------------------------------------------------------------
            // select photo from the gallery and show it in the device

        sign_up_select_photo.setOnClickListener(){
            var photoIntent=Intent(Intent.ACTION_PICK);
            photoIntent.type="image/*"
            startActivityForResult(photoIntent,0)
        }
    }

    var selectPhotoUri: Uri?=null;

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && resultCode==Activity.RESULT_OK && data != null){

            selectPhotoUri =data.data
            var bitmap=MediaStore.Images.Media.getBitmap(contentResolver,selectPhotoUri);
            var bitmapDrawable=BitmapDrawable(bitmap)
            sign_up_select_photo.setBackgroundDrawable(bitmapDrawable)
            sign_up_select_photo.text=""
        }
    }
       //----------------------------------------------------------------------------------------------------------------------

    // the function of upload the data to the database.
    private fun performSignUp(){
        val userName=sign_up_name_edit_text.text.toString()
        val email=sign_up_email_edit_text.text.toString();
        val password=sign_up_password_edit_text.text.toString();
        Log.d("MainActivity","\n User name: $userName \n E-mail: $email \n Password: $password")

        // Firebase authentication to create user
        if(userName.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please fill your datda",Toast.LENGTH_LONG).show()
            return
        }

        auth=Firebase.auth
        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
            uploadImageToFirebaseStorage()
            return@addOnSuccessListener
        }.addOnFailureListener(){Log.d("Main","Faild to create user: ${it.message}")
        Toast.makeText(this,"Faild to create user: ${it.message}",Toast.LENGTH_LONG).show()
            return@addOnFailureListener
        }
    }
    //----------------------------------------------------------------------------------------------------------------------
    private fun uploadImageToFirebaseStorage(){
        storage=Firebase.storage
        val storageRefrecne=storage.reference
        var email=sign_up_email_edit_text.text.toString()
//        var randomNumber=UUID.randomUUID().toString() //mountainsRef
//        var pictureName="$email \t $randomNumber"
        val imageRefrence=storageRefrecne.child("personal_image/$email") //mountainImagesRef
        imageRefrence.putFile(selectPhotoUri!!).addOnSuccessListener {
            imageRefrence.downloadUrl.addOnSuccessListener {
                var profileImageUrl =it.toString()
                saveUserToTheDatebase(profileImageUrl)
            }
            Toast.makeText(this,"Sign up successful !",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,LogInActivity::class.java))

        }

    }
    private fun saveUserToTheDatebase(profileImageLink:String){
        var email=sign_up_email_edit_text.text.toString()
        var userName=sign_up_name_edit_text.text.toString()
        firebaseDatebase=Firebase.database
        var databaseRefrence=firebaseDatebase.reference.child("users/$email");
        val user=User(userName,email,profileImageLink)
        databaseRefrence.setValue(user)
    }
}

class User(val userName:String, val email:String, val imageLink:String)
