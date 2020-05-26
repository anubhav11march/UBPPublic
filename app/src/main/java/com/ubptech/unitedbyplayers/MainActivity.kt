package com.ubptech.unitedbyplayers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

abstract class MainActivity : AppCompatActivity() {

    private var auth: FirebaseAuth?=null

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
        auth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful)
                    Toast.makeText(applicationContext, "Logged in Successfully", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(applicationContext, "Couldn't login", Toast.LENGTH_LONG).show()
            }
    }


}
