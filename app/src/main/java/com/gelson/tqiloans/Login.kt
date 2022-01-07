package com.gelson.tqiloans

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.text.TextUtils
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.gelson.tqiloans.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding:ActivityLoginBinding
    //ActionBar
    private lateinit var actionBar: ActionBar
    //ProgressDialog
    private lateinit var progressDialog:ProgressDialog
    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionBar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor, Aguarde")
        progressDialog.setMessage("Loggin In...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.cadastro.setOnClickListener{
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        //handle click, begin login
        binding.btEntrar.setOnClickListener{
            validateData()
        }
        window.statusBarColor = Color.BLACK;
        supportActionBar?.hide();
    }

    private fun validateData() {
        email = binding.edtEmail.text.toString().trim()
        password = binding.edtPass.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.edtEmail.error = "Email inválido"
        }
        else if (TextUtils.isEmpty(password)){
            //no pass
            binding.edtPass.error = "Favor digitar a senha"
        }else{
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Logado como $email", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Login::class.java))
                finish()

            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this, "Falha de Login to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser(){
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser !=null){
            startActivity(Intent(this, HomeActivity::class.java))
             finish()
        }
    }

}