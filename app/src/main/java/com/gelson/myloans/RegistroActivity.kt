package com.gelson.myloans

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gelson.myloans.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private var dbReference: DatabaseReference? = null
    private var db: FirebaseDatabase? = null


    private lateinit var selectImg: Uri


    private var email = ""
    private var password = ""
    private var name = ""
    private var lastName = ""
    private var income = ""
    private var address = ""
    private var cpf = ""
    private var rg = ""
    private var city = ""
    private var state = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityRegistroBinding.inflate(layoutInflater)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor, Aguarde")
        progressDialog.setMessage("Criando a conta...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbReference = db!!.reference.child("profile")
        storage = FirebaseStorage.getInstance()




        binding.btnCadastro1.setOnClickListener {
            validateData()
        }

        binding.btnCancelar.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        binding.imageUser.setOnClickListener {
            setImage()
        }

        setContentView(binding.root)
        window.statusBarColor = Color.BLACK
        supportActionBar?.hide()
    }

    private fun validateData() {
        email = binding.edtEmail.text.toString().trim()
        password = binding.edtSenha.text.toString().trim() //TODO: Armazenar senha cripto
        name = binding.edtName.text.toString().trim()
        lastName = binding.edtLastName.text.toString().trim()
        cpf = binding.edtCPF.text.toString().trim()
        rg = binding.edtRG.text.toString().trim()
        address = binding.edtAddress.text.toString().trim()
        income = binding.edtRenda.text.toString().trim()
        city = binding.edtCity.text.toString().trim()
        state = binding.edtState.text.toString().trim()


        firebaseSignUp()


    }

    private fun setImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 1)

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {
                selectImg = data.data!!
                binding.imageUser.setImageURI(selectImg)


            }
        }
    }

    private fun firebaseSignUp() {
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                startActivity(Intent(this, HomeActivity::class.java))
                finish()

            }
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val currentUser = firebaseAuth.currentUser
                    val currentUserDb = dbReference?.child((currentUser?.uid!!))
                    currentUserDb?.child("name")?.setValue(name)
                    currentUserDb?.child("lastName")?.setValue(lastName)
                    currentUserDb?.child("cpf")?.setValue(cpf)
                    currentUserDb?.child("rg")?.setValue(rg)
                    currentUserDb?.child("address")?.setValue(address)
                    currentUserDb?.child("income")?.setValue(income)
                    currentUserDb?.child("city")?.setValue(city)
                    currentUserDb?.child("state")?.setValue(state)
                    Toast.makeText(this, "Usuario cadastrado", Toast.LENGTH_SHORT).show()
                    uploadImageUser()
                    finish()

                } else {
                    Toast.makeText(
                        this,
                        "falha ao tentar cadastrar, tente novamente",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Falha ${e.message}", Toast.LENGTH_SHORT).show()

            }

    }

    private fun uploadImageUser() {
        val currentUser = firebaseAuth.currentUser
        val storageRef = storage.reference.child("profile").child(currentUser?.uid!!)
        storageRef.putFile(selectImg).addOnCompleteListener{
            if (it.isSuccessful){
               storageRef.downloadUrl.addOnSuccessListener { task ->
                   uploadUrl(task.toString())
               }

               }
                }
            }

    private fun uploadUrl(imgUrl: String) {

        val currentUser = firebaseAuth.currentUser
        val currentUserDb = dbReference?.child((currentUser?.uid!!))
        currentUserDb?.child("imgUrl")?.setValue(imgUrl)
    }


}

