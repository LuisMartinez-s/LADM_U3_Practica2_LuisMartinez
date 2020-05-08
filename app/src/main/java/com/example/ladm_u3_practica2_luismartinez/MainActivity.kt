package com.example.ladm_u3_practica2_luismartinez

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    var dataLista = ArrayList<String>()
    var listaID = ArrayList<String>()
    var baseDatos = FirebaseFirestore.getInstance()
    var existeCliente = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnGuardar.setOnClickListener {
            buscarPorTelefono()

        }//btnGuardar
        btnConsultar.setOnClickListener {
            var activity2 = Intent(this, Main2Activity::class.java)
            startActivity(activity2)
        }
    }

    private fun insertar() {
        var data = hashMapOf(
            "nombre" to editNombre.text.toString(),
            "domicilio" to editDomicilio.text.toString(),
            "celular" to editTelefono.text.toString(),
            "pedido" to hashMapOf(
                "descripcion" to editDescripcion.text.toString(),
                "precio" to editPrecio.text.toString().toDouble(),
                "cantidad" to editCantidad.text.toString().toInt(),
                "entregado" to checkEntregado.isChecked
            )//pedido
        )//data
        baseDatos.collection("Restaurant")
            .add(data as Any)
            .addOnSuccessListener {
                Toast.makeText(this, "Se insertó correctamente", Toast.LENGTH_LONG)
                    .show()
            }//success
            .addOnFailureListener {
                Toast.makeText(this, "No se insertó", Toast.LENGTH_LONG)
                    .show()
            }//fail
        limpiarCampos()
    }//insertar

    private fun limpiarCampos() {
        editCantidad.setText("")
        editDescripcion.setText("")
        editNombre.setText("")
        editDomicilio.setText("")
        editPrecio.setText("")
        editTelefono.setText("")
        checkEntregado.isChecked = false
    }

    private fun buscarPorTelefono() {

        baseDatos.collection("Restaurant")
            .whereEqualTo("celular", editTelefono.text.toString())
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    //si hay error
                    Toast.makeText(this, "Error no se puede acceder a consulta", Toast.LENGTH_LONG)
                        .show()
                    return@addSnapshotListener
                }//if
                dataLista.clear()
                listaID.clear()
                for (document in querySnapshot!!) {
                    var cadena = "Nombre: " + document.getString("nombre") + "\n" +
                            "Domicilio: " + document.getString("domicilio") + "\n" +
                            "Celular: " + document.getString("celular") + "\n" +
                            "Datos del pedido: \n" +
                            "Descripcion: " + document.getString("pedido.descripcion") + "\n" +
                            "Cantidad: " + document.get("pedido.cantidad") + "\n" +
                            "Entregado: " + document.get("pedido.entregado") + "\n" +
                            "Precio: " + document.get("pedido.precio") + "\n"
                    dataLista.add(cadena)
                    listaID.add(document.id)
                }//for
            }//addsnap
        if (dataLista.size > 0) {
            Toast.makeText(
                this,
                "El numero de telefono ya ha sido registrado anteriormente, si desea registrar un nuevo pedido modifique los pedidos del cliente, o bien, actualice los datos del cliente",
                Toast.LENGTH_LONG
            )
                .show()
        }//if
        else {
            insertar()
        }
    }//buscar por telefono

}
