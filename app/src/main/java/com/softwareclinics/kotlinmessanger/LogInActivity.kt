package com.softwareclinics.kotlinmessanger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {
        lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        sign_in_button.setOnClickListener(){
            val emailSignIn=sign_in_email_edit_text.text.toString();
            val passwordSignIn=sign_in_password_edit_text.text.toString();
            Log.d("LogInActivity","\n Email: $emailSignIn \n Password: $passwordSignIn");
            auth= Firebase.auth
            auth.signInWithEmailAndPassword(emailSignIn,passwordSignIn).addOnCompleteListener(){
                if(it.isSuccessful){
                    Toast.makeText(this,"Your log in Sucsessfully!",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this,"Your information is wrong",Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener(){
                Toast.makeText(this,"${it.message}",Toast.LENGTH_LONG).show()
            }
        }

        sign_in_backToRegistration_text_view.setOnClickListener(){
            finish()
        }
    }
}