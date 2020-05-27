package com.ubptech.unitedbyplayers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Kylodroid on 26-05-2020.
 */
abstract class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

    }

    fun loginClicked(view: View){
        val email = email.text.toString()
        val password = pass.text.toString()
        if(!email.isEmpty() && !password.isEmpty())
            login(email, password)
    }

    fun login(email:String, password:String){
        auth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful) {
                    Toast.makeText(applicationContext, "Logged in Successfully",
                        Toast.LENGTH_LONG).show()
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                }
                else
                    Toast.makeText(applicationContext, "Couldn't login", Toast.LENGTH_LONG).show()
            }


    }

}
