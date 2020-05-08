package com.example.ladm_u3_practica2_luismartinez

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_main3.*

class Main3Activity : AppCompatActivity() {
    var id = ""
    var dataLista = ArrayList<String>()
    var listaID = ArrayList<String>()
    var baseDatos = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        var extras = intent.extras
        id = extras!!.getString("id")!!
        nombre.setText(extras.getString("nombre"))
        domicilio.setText(extras.getString("domicilio"))
        telefono.setText(extras.getString("celular"))
        descripcion.setText(extras.get("descripcion").toString())
        cantidad.setText(extras.get("cantidad").toString())
        precio.setText(extras.get("precio").toString())
        var estaEntregado = extras.getString("entregado")!!.toBoolean()
        entregado.isChecked=estaEntregado


        btnActualizar.setOnClickListener {
            baseDatos.collection("Restaurant")
                .document(id)
                .update(
                    "nombre", nombre.text.toString(),
                    "domicilio", domicilio.text.toString(),
                    "celular", telefono.text.toString(),
                    "pedido.descripcion", descripcion.text.toString(),
                    "pedido.cantidad", cantidad.text.toString().toInt(),
                    "pedido.precio", precio.text.toString().toDouble(),
                    "pedido.entregado", entregado.isChecked
                )
                .addOnSuccessListener {
                    Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_LONG)
                        .show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error no se pudo actualizar", Toast.LENGTH_LONG)
                        .show()
                }

        }//actualizar
        btnRegresar.setOnClickListener {
            finish()
        }
    }//onCreate


}
