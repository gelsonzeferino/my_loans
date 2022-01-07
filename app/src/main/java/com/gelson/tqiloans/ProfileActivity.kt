package com.gelson.tqiloans

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gelson.tqiloans.databinding.ActivityProfileBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    var dbReference : DatabaseReference? = null
    var db: FirebaseDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {


        binding  = ActivityProfileBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = Color.BLACK;
        supportActionBar?.hide();

        binding.btnCancelar.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbReference = db!!.reference.child("profile")

        checkUser()


    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        val userReference = dbReference?.child(firebaseUser?.uid!!)
        if (firebaseUser != null){
            val email = firebaseUser.email
            binding.edtEmail.text = email

            userReference?.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userGreet = snapshot.child("name").value.toString()
                    println(userGreet)
                    binding.userView.text = userGreet

                    val name = snapshot.child("name").value.toString()
                    binding.edtName.text = name

                    val cpf = snapshot.child("cpf").value.toString()
                    binding.edtCPF.text = cpf

                    val rg = snapshot.child("rg").value.toString()
                    binding.edtRG.text = rg

                    val address = snapshot.child("address").value.toString()
                    binding.edtAddress.text = address

                    val income = snapshot.child("income").value.toString()
                    binding.edtRenda.text = income

                    val city = snapshot.child("city").value.toString()
                    binding.edtCity.text = city

                    val state = snapshot.child("state").value.toString()
                    binding.edtState.text = state
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }else{
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}