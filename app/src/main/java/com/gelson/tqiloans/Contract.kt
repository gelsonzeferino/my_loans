package com.gelson.tqiloans

class Contract ( val valor : String, val qtdParcelas : String, val cid : Long, val status : String,
                    val primeiraParcela : String, val uid : String, val email : String){

    constructor() : this("", "", 0, "", "", "", "" ){

    }
}