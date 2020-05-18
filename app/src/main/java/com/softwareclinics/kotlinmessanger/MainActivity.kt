package com.softwareclinics.kotlinmessanger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.getInstance
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sign_up_button.setOnClickListener(){
            performSignUp()
        }


        sign_up_doYouHaveAnCcount_text_view.setOnClickListener(){
            val signInIntent= Intent(this,LogInActivity::class.java);
            startActivity(signInIntent);

        }
    }

    private fun performSignUp(){
        val userName=sign_up_name_edit_text.text.toString()
        val email=sign_up_email_edit_text.text.toString();
        val password=sign_up_password_edit_text.text.toString();
        Log.d("MainActivity","\n User name: $userName \n E-mail: $email \n Password: $password")

        // Firebase Authintication to create user
        auth=Firebase.auth
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(){
            if (!it.isSuccessful){
                startActivity(Intent(this,LogInActivity::class.java))
                return@addOnCompleteListener}
        }.addOnFailureListener(){Log.d("Main","Faild to create user: ${it.message}")
        Toast.makeText(this,"Faild to create user: ${it.message}",Toast.LENGTH_LONG).show()
            return@addOnFailureListener
        }
    }
}
