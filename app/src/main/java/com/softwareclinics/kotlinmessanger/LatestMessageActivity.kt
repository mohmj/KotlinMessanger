package com.softwareclinics.kotlinmessanger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.softwareclinics.kotlinmessanger.Handler.Handel
import com.softwareclinics.kotlinmessanger.R.*
class LatestMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_latest_message)
            verifyLogin()

    }



private fun verifyLogin(){
    var uid=Firebase.auth.uid;
    if(uid==null){
        var registrationIntent=Intent(this,MainActivity::class.java);
        registrationIntent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registrationIntent)
    }
}

//-----------------------------------------------------------------------------------------------------------------------
    // Menu in the bar of the application
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId){
            R.id.new_message ->{
                startActivity(Intent(this,NewMessageActivity::class.java))
            }
            R.id.Log_out ->{
                Firebase.auth.signOut()
                var registrationIntent=Intent(this,MainActivity::class.java);
                registrationIntent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(registrationIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    }