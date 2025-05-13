package com.android.gpslocation

import android.content.Intent
import android.content.pm.PackageManager // Importa PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat // Importa ActivityCompat
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var textViewLocation: TextView
    private lateinit var buttonUpdateLocation: Button
    private lateinit var buttonStartSharing: Button
    private lateinit var buttonStopSharing: Button
    private lateinit var buttonShareSocial: Button

    private var isSharingLocation = false
    private var database: DatabaseReference? = null
    private var currentUserId: String? = null
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inizializzazione dei componenti
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        textViewLocation = findViewById(R.id.textViewLocation)
        buttonUpdateLocation = findViewById(R.id.buttonUpdateLocation)
        buttonStartSharing = findViewById(R.id.buttonStartSharing)
        buttonStopSharing = findViewById(R.id.buttonStopSharing)
        buttonShareSocial = findViewById(R.id.buttonShareSocial)

        // Recupero dell'ID utente da Firebase
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        database = FirebaseDatabase.getInstance().reference.child("Users").child(currentUserId!!)

        // Aggiungi il listener per aggiornare la posizione
        buttonUpdateLocation.setOnClickListener {
            getCurrentLocation()
        }

        // Avvio della condivisione della posizione su Firebase
        buttonStartSharing.setOnClickListener {
            if (currentLocation != null) {
                startSharingLocation()
            }
        }

        // Fermare la condivisione della posizione su Firebase
        buttonStopSharing.setOnClickListener {
            stopSharingLocation()
        }

        // Condividi la posizione sui social
        buttonShareSocial.setOnClickListener {
            shareLocation()
        }
    }

    // Ottieni la posizione corrente
    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        currentLocation = location
                        textViewLocation.text = "Posizione: Lat: ${location.latitude}, Lon: ${location.longitude}"
                    } else {
                        textViewLocation.text = "Posizione non disponibile"
                    }
                }
        }
    }

    // Avvia la condivisione della posizione su Firebase
    private fun startSharingLocation() {
        isSharingLocation = true
        database?.child("location")?.setValue(currentLocation)
    }

    // Ferma la condivisione della posizione su Firebase
    private fun stopSharingLocation() {
        isSharingLocation = false
        database?.child("location")?.removeValue()
    }

    // Condividi la posizione sui social
    private fun shareLocation() {
        if (currentLocation != null) {
            val locationText = "La mia posizione è: \nLat: ${currentLocation!!.latitude}, Lon: ${currentLocation!!.longitude}"

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, locationText)
                type = "text/plain"
            }

            // Verifica se ci sono app che possono gestire l'intent
            startActivity(Intent.createChooser(shareIntent, "Condividi con"))
        } else {
            textViewLocation.text = "Posizione non disponibile"
        }
    }
}
