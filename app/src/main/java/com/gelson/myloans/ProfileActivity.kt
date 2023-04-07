package com.gelson.myloans

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gelson.myloans.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso



class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var dbReference : DatabaseReference? = null
    private var db: FirebaseDatabase? = null
    private lateinit var selectImg : Uri



    override fun onCreate(savedInstanceState: Bundle?) {


        binding  = ActivityProfileBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = Color.BLACK
        supportActionBar?.hide()


        binding.imageUser.setOnClickListener{
           setImage()
        }

        binding.btnCancelar.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbReference = db!!.reference.child("profile")

        checkUser()


    }

    private fun setImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 1)

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null){
            if (data.data != null){
                selectImg = data.data!!
                binding.imageUser.setImageURI(selectImg)

            }
        }
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
                    binding.userView.text = userGreet

                    val name = snapshot.child("name").value.toString()
                    val lastName = snapshot.child("lastName").value.toString()
                    binding.edtName.text = "$name $lastName"

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



