package com.gelson.tqiloans

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.gelson.tqiloans.databinding.ActivityRegistroBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase




class RegistroActivity : AppCompatActivity() {
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    var dbReference : DatabaseReference? = null
    var db: FirebaseDatabase? = null


    private var email = ""
    private var password = ""
    private var name = ""
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


        binding.btnCadastro1.setOnClickListener{
            validateData()
        }

        binding.btnCancelar.setOnClickListener {
            startActivity(Intent(this, Login :: class.java))
            finish()
        }

        setContentView(binding.root)
        window.statusBarColor = Color.BLACK;
        supportActionBar?.hide();
    }
    private fun validateData() {
        email = binding.edtEmail.text.toString().trim()
        password = binding.edtSenha.text.toString().trim() //TODO: Armazenar senha cripto
        name = binding.edtName.text.toString().trim()
        cpf = binding.edtCPF.text.toString().trim()
        rg = binding.edtRG.text.toString().trim()
        address = binding.edtAddress.text.toString().trim()
        income = binding.edtRenda.text.toString().trim()
        city = binding.edtCity.text.toString().trim()
        state = binding.edtState.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.edtEmail.error = "Email invalido"
        }else if(TextUtils.isEmpty(password)){

            binding.edtSenha.error = "Erro"

        }else if(password.length < 8){

            binding.edtSenha.error = "Senha curta"

        }else if(TextUtils.isEmpty(name)){

            binding.edtName.error = "Favor digitar o nome"

        }else if(TextUtils.isEmpty(cpf)){

            binding.edtCPF.error = "Favor digitar o cpf"

        }else if(TextUtils.isEmpty(rg)){

            binding.edtRG.error = "Favor digitar o rg"

        }else if(TextUtils.isEmpty(address)){

            binding.edtAddress.error = "Favor digitar o Endereço"

        }else if(TextUtils.isEmpty(income)){

            binding.edtRenda.error = "Favor digitar o Renda"

        }else if(TextUtils.isEmpty(city)){

            binding.edtCity.error = "Favor digitar o Cidade"

        }else if(TextUtils.isEmpty(state)){

            binding.edtState.error = "Favor digitar o Estado"

        }else{

            firebaseSignUp()
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
                if (it.isSuccessful){
                    val currentUser = firebaseAuth.currentUser
                    val currentUserDb = dbReference?.child((currentUser?.uid!!))
                    currentUserDb?.child("name")?.setValue(name)
                    currentUserDb?.child("cpf")?.setValue(cpf)
                    currentUserDb?.child("rg")?.setValue(rg)
                    currentUserDb?.child("address")?.setValue(address)
                    currentUserDb?.child("income")?.setValue(income)
                    currentUserDb?.child("city")?.setValue(city)
                    currentUserDb?.child("state")?.setValue(state)
                    Toast.makeText(this, "Usuario cadastrado", Toast.LENGTH_SHORT).show()
                    finish()

                }else{
                    Toast.makeText(this, "falha ao tentar cadastrar, tente novamente", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Falha ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }
}

