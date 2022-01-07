package com.gelson.tqiloans

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gelson.tqiloans.databinding.ActivityDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityDetailsBinding.inflate(layoutInflater)



        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = Color.BLACK;
        supportActionBar?.hide();


    }

}