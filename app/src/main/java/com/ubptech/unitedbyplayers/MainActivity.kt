package com.ubptech.unitedbyplayers

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_loading.*
import java.security.MessageDigest

/**
 * Created by Kylodroid on 26-05-2020.
 */
public class MainActivity : AppCompatActivity() {

    companion object {
        val RC_SIGN_IN = 0
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()

        FacebookSdk.sdkInitialize(applicationContext);
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("898532588623-0ej1hpm98ga9uiv57264vluuo05a1cnr.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        googlesignin.setOnClickListener {
            googleLoginClicked()
        }

        fbsignin.setOnClickListener {
            fbLoginClicked()
        }


        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.v("AAA", "FB Logged in $loginResult")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.v("AAA", "Sign in cancelled FB")
                    Toast.makeText(applicationContext, "Sign in Cancelled", Toast.LENGTH_LONG).show()
                    loading_view.visibility = View.GONE
                }

                override fun onError(error: FacebookException) {
                    Log.v("AAA", error.localizedMessage.toString())
                    Toast.makeText(applicationContext, "This email already exists with another login method, " +
                            "please login with that method", Toast.LENGTH_LONG).show()
                    loading_view.visibility = View.GONE
                    error.printStackTrace()
                }
            }
        )
    }

    private fun googleLoginClicked(){
        loading_view.visibility = View.VISIBLE
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    private fun fbLoginClicked() {
        loading_view.visibility = View.VISIBLE
        LoginManager.getInstance().logInWithReadPermissions(
            this,
            listOf("email", "public_profile")
        )
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //fb
        callbackManager.onActivityResult(requestCode, resultCode, data)

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
                    loading_view.visibility = View.GONE
                    Toast.makeText(applicationContext, "An error occurred", Toast.LENGTH_LONG).show()
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
                    Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_LONG).show()
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                }
                loading_view.visibility = View.GONE
            }
    }
    // [END auth_with_google]

    //fb
    fun handleFacebookAccessToken(token: AccessToken) {
        Log.v("AAA", "FB Access Token")
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(
                this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Successfully Logged in",
                            Toast.LENGTH_LONG).show()
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                    }
                    loading_view.visibility = View.GONE
                })
    }

    fun loginClicked(view: View){
        loading_view.visibility = View.VISIBLE
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
                    finish()
                }
                else
                    Toast.makeText(applicationContext, "Email and password don't match", Toast.LENGTH_LONG).show()
                loading_view.visibility = View.GONE
            }
    }

    fun signUpClicked(view: View){
        startActivity(Intent(applicationContext, RegistrationActivity::class.java))
    }


    fun forgotPassClicked(view: View){
        val intent = Intent(applicationContext, AuthActivity::class.java)
        intent.putExtra("type", "requestPass")
        startActivity(intent)
    }

}
