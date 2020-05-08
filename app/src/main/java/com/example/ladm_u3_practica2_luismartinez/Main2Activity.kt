package com.example.ladm_u3_practica2_luismartinez

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {
    var dataLista = ArrayList<String>()
    var listaID = ArrayList<String>()
    var baseDatos = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        buscarTodos()
        buscar.setOnClickListener {
            buscarPorTelefono()
        }
        mostrarTodo.setOnClickListener {
            buscarTodos()
        }
        lista.setOnItemClickListener { parent, view, position, id ->
            if (listaID.size == 0) {
                return@setOnItemClickListener
            }//if
            alertaEliminarActualizar(position)
        }//lista


    }

    private fun buscarPorTelefono() {
        baseDatos.collection("Restaurant")
            .whereEqualTo("celular", buscarTelefono.text.toString())
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
                            "Precio: $" + document.get("pedido.precio") + "\n"
                    dataLista.add(cadena)
                    listaID.add(document.id)
                }//for
                if (dataLista.size == 0) {
                    dataLista.add("No hay data")
                }//if
                var adaptador =
                    ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataLista)
                lista.adapter = adaptador
            }//addsnap
    }//buscar por telefono

    private fun buscarTodos() {
        baseDatos.collection("Restaurant")
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
                            "Precio: $" + document.get("pedido.precio") + "\n"

                    dataLista.add(cadena)
                    listaID.add(document.id)
                }//for
                if (dataLista.size == 0) {
                    dataLista.add("No hay data")
                }//if
                var adaptador =
                    ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataLista)
                lista.adapter = adaptador
            }//addsnap
    }//buscar por todos


    private fun alertaEliminarActualizar(position: Int) {
        AlertDialog.Builder(this).setTitle("Atención")
            .setMessage("¿Qué desea hacer con: \n${dataLista[position]}?")
            .setPositiveButton("Eliminar") { d, w ->
                eliminar(listaID[position])
            }
            .setNegativeButton("Actualizar") { d, w ->
                llamarVentanaActualizar(listaID[position])
            }
            .setNeutralButton("Cancelar") { d, w -> }
            .show()

    }//alertaEliminarActualizar


    private fun llamarVentanaActualizar(idActualizar: String) {
        baseDatos.collection("Restaurant")
            .document(idActualizar)
            .get()
            .addOnSuccessListener {
                var v = Intent(this, Main3Activity::class.java)
                v.putExtra("id", idActualizar)
                v.putExtra("nombre", it.getString("nombre"))
                v.putExtra("domicilio", it.getString("domicilio"))
                v.putExtra("celular", it.getString("celular"))
                v.putExtra("descripcion", it.get("pedido.descripcion").toString())
                v.putExtra("precio", it.get("pedido.precio").toString())
                v.putExtra("cantidad", it.get("pedido.cantidad").toString())
                v.putExtra("entregado", it.get("pedido.entregado").toString())
                startActivity(v)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error no hay conexión de red", Toast.LENGTH_LONG)
                    .show()
            }
    }//llamarventana actualizar


    private fun eliminar(idEliminar: String) {
        baseDatos.collection("Restaurant")
            .document(idEliminar)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Se eliminó con exito", Toast.LENGTH_LONG)
                    .show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "No se pudo eliminar", Toast.LENGTH_LONG)
                    .show()
            }
    }
}
