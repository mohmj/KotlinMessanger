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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.softwareclinics.kotlinmessanger.Handler.Handel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var firebaseDatebase: FirebaseDatabase
    private lateinit var database:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var Handel= Handel()

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

        }
    }
       //----------------------------------------------------------------------------------------------------------------------

    // the function of upload the data to the database.
    private fun performSignUp(){
        val username=sign_up_name_edit_text.text.toString()
        val email=sign_up_email_edit_text.text.toString();
        val password=sign_up_password_edit_text.text.toString();
        var Handel= Handel()
        Log.d("MainActivity","\n User name: $username \n E-mail: $email \n Password: $password")

        // Firebase authentication to create user
        if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please fill your datda",Toast.LENGTH_LONG).show()
            return
        }

        auth=Firebase.auth
        database=Firebase.database.reference
        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
            uploadImageToFirebaseStorage()
            Handel.email=email
            Handel.username=username
            return@addOnSuccessListener
        }.addOnFailureListener(){Log.d("Main","Faild to create user: ${it.message}")
        Toast.makeText(this,"Faild to create user: ${it.message}",Toast.LENGTH_LONG).show()
            return@addOnFailureListener
        }
    }
    //----------------------------------------------------------------------------------------------------------------------
    // Function for upload image to the database
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
                Log.d("one",profileImageUrl)
                saveUserToTheDatebase(profileImageUrl)
            }
            Toast.makeText(this,"Sign up successful !",Toast.LENGTH_SHORT).show()
            var LatestMessageActivityIntent=Intent(this,LatestMessageActivity::class.java)
            LatestMessageActivityIntent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(LatestMessageActivityIntent)
        }

    }
    //----------------------------------------------------------------------------------------------------------------------
    private fun saveUserToTheDatebase(profileImageLink:String){
        var uid=Firebase.auth.uid?:""
        var email=sign_up_email_edit_text.text.toString()
        var username=sign_up_name_edit_text.text.toString()
        var password=sign_up_password_edit_text.text.toString()
        var Handel= Handel()
        database=Firebase.database.reference
        val user=User(uid, username,email,password,profileImageLink)
//        database.child(username).setValue(uid)
        database.child(username).setValue(user)
    }
}

class User(val uid:String, val username:String?, val email:String?,val password:String, val imageLink:String?){
    constructor() :this("","","","","")
    }
