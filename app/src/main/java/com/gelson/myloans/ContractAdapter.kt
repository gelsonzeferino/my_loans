package com.gelson.myloans


import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ContractAdapter (private val mCtx: Context, private val layoutResId: Int, private val contractList: List<Contract>)
    :ArrayAdapter <Contract> (mCtx, layoutResId, contractList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view : View  = layoutInflater.inflate(layoutResId, null)

        val textViewValor = view.findViewById<TextView>(R.id.valorEmprestimo)
        val textViewCid = view.findViewById<TextView>(R.id.codContrato)
        val textViewQtdParcelas = view.findViewById<TextView>(R.id.qtdParcelas1)
        val textViewStatus = view.findViewById<TextView>(R.id.status2)

        val btnDetails = view.findViewById<Button>(R.id.btnDetalhes)
        val contract = contractList[position]

        textViewValor.text = contract.valor
        textViewCid.text = contract.cid.toString()
        textViewQtdParcelas.text = contract.qtdParcelas
        textViewStatus.text = contract.status

        val uid = contract.uid
        btnDetails.setOnClickListener{
             contractDetails(contract)
        }

        return view

    }

    private fun contractDetails(contract: Contract) {

        val builder = AlertDialog.Builder(mCtx)
        val inflater = LayoutInflater.from(mCtx)

        val view1 = inflater.inflate(R.layout.activity_details, null)

        val textViewCid = view1.findViewById<TextView>(R.id.codContrato)
        val textViewQtdParcelas = view1.findViewById<TextView>(R.id.viewQtdParcelas)
        val textViewStatus = view1.findViewById<TextView>(R.id.status)
        val textViewPrimeiraParcela = view1.findViewById<TextView>(R.id.viewPrimeiraParcela)
        val textViewValorEmprestimo = view1.findViewById<TextView>(R.id.viewValorEmprestimo)

        textViewStatus.text = contract.status
        textViewQtdParcelas.text = contract.qtdParcelas
        textViewCid.text = contract.cid.toString()
        textViewPrimeiraParcela.text = contract.primeiraParcela
        textViewValorEmprestimo.text = contract.valor


        var dbReference: DatabaseReference? = null
        var db: FirebaseDatabase? = null
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbReference = db.reference.child("profile")


        val firebaseUser = firebaseAuth.currentUser
        val userReference = dbReference.child(firebaseUser?.uid!!)

        val email = firebaseUser.email
        val textViewEmail = view1.findViewById<TextView>(R.id.viewEmail)

        textViewEmail.text = email

        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val income = snapshot.child("income").value.toString()
                val textViewIncome = view1.findViewById<TextView>(R.id.viewIncome)

                textViewIncome.text = income

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        builder.setView(view1)



        builder.setNegativeButton(
            "Voltar"
        ) { p0, p1 -> }


    }

}