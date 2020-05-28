package com.ubptech.unitedbyplayers

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Kylodroid on 26-05-2020.
 */
public class MainActivity : AppCompatActivity() {

    companion object {
        val RC_SIGN_IN = 0
    }

    private lateinit var auth: FirebaseAuth
    var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("898532588623-0ej1hpm98ga9uiv57264vluuo05a1cnr.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        googlesignin.setOnClickListener {
            googleLoginClicked()
        }
    }

    private fun googleLoginClicked(){
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
        //progressbar.visibility = View.VISIBLE
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_CANCELED) {
                //progressbar.visibility = View.GONE
            }
            if (resultCode == Activity.RESULT_OK) {
                Log.d("SignUp: ", "Result ok returned")
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignUp: ", "Google sign in failed", e)
                }
            }
        }
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                    val email = auth.currentUser?.email
                    val uid = auth.uid

                    // start new activity after this

                    Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_LONG).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG ", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                   // progressbar.visibility = View.GONE
                }
            }
    }
    // [END auth_with_google]

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

    fun signUpClicked(view: View){
        startActivity(Intent(applicationContext, RegistrationActivity::class.java))
    }


    fun forgotPassClicked(view: View){
        val intent = Intent(applicationContext, AuthActivity::class.java)
        intent.putExtra("requestPass", "true")
        startActivity(intent)
    }

}
