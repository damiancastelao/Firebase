package com.dam2.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    // para filtrar en el Logcat
    private val TAG = "RealTime"
    // referencia a BD
    private lateinit var database: DatabaseReference
    // textView para mostrar algun dato
    private lateinit var resultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializamos la BD
        // si la base de datos esta en una region diferente a uscentral1
        // tenemos que poner la URL
        database = Firebase.database("https://leafy-winter-331508-default-rtdb.europe-west1.firebasedatabase.app")
            // le damos la ruta
            .getReference("Profesionales")

        // creamos el listener
        // para leer los datos cuando cambien
        // defino listener
        val datoListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get dato object and use the values to update the UI
                val dato = dataSnapshot.getValue<HashMap<String,Profesional>>()
                Log.d(TAG,"cambio: " + dato.toString())
                // muestro datos en UI
                // comprueba que no es nulo
                if (dato != null) {
                    mostrarDatos(dato)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting dato failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        // añado listener a la ref de la bd
        database.addValueEventListener(datoListener)
        // text view para mostrar las variaciones
        resultado = findViewById<TextView>(R.id.resultado)

    }

    /**
     * muestra los datos en la UI
     * @param datos snapshot de toda la referencia
     */
    private fun mostrarDatos(datos: HashMap<String,Profesional>) {
        Log.d(TAG,"Modifico UI: " + datos.toString())
        // muestra solo la latitud del ProA
        resultado.text = "lt de ProA:" + datos["-MuAVfyEyi8of6nGNQyL"]?.lt
    }
}