package com.gelson.tqiloans

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import com.gelson.tqiloans.databinding.ActivityListContractsBinding


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class ListContractsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListContractsBinding
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var contractList:  MutableList<Contract>
    var dbReference : DatabaseReference? = null
    var db: FirebaseDatabase? = null
    lateinit var listView : ListView



    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityListContractsBinding.inflate(layoutInflater)

        binding.btnVoltar2.setOnClickListener {
            startActivity(Intent(this, HomeActivity :: class.java))
        }


        setContentView(binding.root)
        db = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        contractList = mutableListOf()
        dbReference = db!!.reference.child("contratos")
        listView = findViewById(R.id.listContract1)

        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.BLACK;
        supportActionBar?.hide();

        checkUser()

    }

    private fun checkUser() {

        val firebaseUser = firebaseAuth.currentUser

        val email = firebaseUser?.email

        dbReference!!.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                System.out.println("uid")
                if(p0.exists()){

                    contractList.clear()
                    for (h in p0.children){

                        val contract = h.getValue(Contract::class.java)
                        val email1 = contract?.email
                        if (email1.equals(email)){
                            contractList.add(contract!!)
                        }

                    }
                    val adapter = ContractAdapter(this@ListContractsActivity, R.layout.contract_model, contractList)
                    listView.adapter = adapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}