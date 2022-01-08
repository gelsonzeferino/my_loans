package com.gelson.tqiloans

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils

import android.widget.Toast
import com.gelson.tqiloans.databinding.ActivityNewSolicitationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

class NewSolicitationActivity : AppCompatActivity() {
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    private lateinit var binding: ActivityNewSolicitationBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    var dbReference : DatabaseReference? = null
    var db: FirebaseDatabase? = null

    var valor = ""
    private var qtdParcelas = ""
    private var primeiraParcela = ""
    var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityNewSolicitationBinding.inflate(layoutInflater)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor, Aguarde")
        progressDialog.setMessage("Criando a conta...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbReference = db!!.reference.child("solicitacoesEmAnalise")

        binding.btnCancelar.setOnClickListener {
            startActivity(Intent(this, HomeActivity :: class.java))
            finish()
        }

        binding.btnCadastro1.setOnClickListener{
            validateData()
        }
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = Color.BLACK;
        supportActionBar?.hide();



    }

    private fun validateData() {

        valor = binding.edtValor.text.toString().trim()
        qtdParcelas = binding.edtQtdParcelas.text.toString().trim() //TODO: Armazenar senha cripto
        primeiraParcela = binding.edtDataParcela.text.toString().trim()


        if (TextUtils.isEmpty(valor)){

            binding.edtValor.error = "Favor digitar um valor"

        }else if(TextUtils.isEmpty(qtdParcelas)){

            binding.edtQtdParcelas.error = "Favor digitar quantidade de parcelas"

        }else if(TextUtils.isEmpty(primeiraParcela)){

            binding.edtDataParcela.error = "Favor digitar a data da primeira parcela"

        }else{

            firebaseSignUp()
        }

    }
    private fun firebaseSignUp() {



                    val currentUser = firebaseAuth.currentUser
                    val email = currentUser?.email
                    val currentUserDb = dbReference?.child((currentUser?.uid!!+ Random.nextInt(1, 100)))
                    val uid = currentUser?.uid
                    var cid = Random.nextInt(100)



                    currentUserDb?.child("uid")?.setValue(uid)
                    currentUserDb?.child("cid")?.setValue(cid)
                    currentUserDb?.child("email")?.setValue(email)
                    currentUserDb?.child("valor")?.setValue(valor)
                    currentUserDb?.child("qtdParcelas")?.setValue(qtdParcelas)
                    currentUserDb?.child("primeiraParcela")?.setValue(primeiraParcela) //todo: Formatar para data.
                    currentUserDb?.child("status")?.setValue("Em analise")
                    Toast.makeText(this, "Nova Solicitação enviada", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()


    }



}