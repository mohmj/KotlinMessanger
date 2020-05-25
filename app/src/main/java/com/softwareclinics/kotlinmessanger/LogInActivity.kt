package com.softwareclinics.kotlinmessanger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.softwareclinics.kotlinmessanger.Handler.Handel
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {
        lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        sign_in_button.setOnClickListener(){
            signIn()
        }

        sign_in_backToRegistration_text_view.setOnClickListener(){
            finish()
        }
    }

    private fun signIn(){
        val emailSignIn=sign_in_email_edit_text.text.toString();
        val passwordSignIn=sign_in_password_edit_text.text.toString();
        var Handel= Handel()
        Log.d("LogInActivity","\n Email: $emailSignIn \n Password: $passwordSignIn");

        if(passwordSignIn.isEmpty() || emailSignIn.isEmpty()){
            Toast.makeText(this,"Please fill your datda",Toast.LENGTH_LONG).show()
            return
        }


        auth= Firebase.auth
        auth.signInWithEmailAndPassword(emailSignIn,passwordSignIn).addOnSuccessListener(){
                Toast.makeText(this,"Your log in Sucsessfully!",Toast.LENGTH_LONG).show()
            var LatestMessageActivityIntent=Intent(this,LatestMessageActivity::class.java)
            LatestMessageActivityIntent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(LatestMessageActivityIntent)
        }.addOnFailureListener(){
            Toast.makeText(this,"${it.message}",Toast.LENGTH_LONG).show()
        }
    }
}