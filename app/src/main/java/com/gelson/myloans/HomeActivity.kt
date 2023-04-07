package com.gelson.myloans

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gelson.myloans.databinding.ActivityHomeBinding


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var dbReference : DatabaseReference? = null
    private var db: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding  = ActivityHomeBinding.inflate(layoutInflater)
        binding.exit.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        //Iniciar os Botoes do Menu

        binding.profile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))//acessar pagina de profile.
            finish()
        }

        binding.newLoan.setOnClickListener {
            startActivity(Intent(this, NewSolicitationActivity::class.java))
            finish()
        }

        binding.listContract.setOnClickListener {
            startActivity(Intent(this, ListContractsActivity::class.java))
        }

        binding.listSolicitations.setOnClickListener {
            startActivity(Intent(this, ListNewsSolicitationsActivity::class.java))
        }

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbReference = db!!.reference.child("profile")

        checkUser()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = Color.BLACK
        supportActionBar?.hide()
    }
    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        val userReference = dbReference?.child(firebaseUser?.uid!!)
        if (firebaseUser != null){


            userReference?.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userGreet = snapshot.child("name").value.toString()
                    binding.userView.text = userGreet

                    val userImg = snapshot.child("imgUrl").value.toString()
                    println(userImg)

                    Picasso.get().load(userImg).into(binding.imageUser)


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