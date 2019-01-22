package com.example.udev

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.udev.classes.Person
import java.lang.Exception

class PersonActivity : AppCompatActivity() {

    // Contrôles
    private lateinit var firstNameTextView: TextView
    private lateinit var lastNameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        // Appel des fonctions de chargement
        initControls()
        initActions()
        loadFromIntent()
    }

    /**
     * Chargement des valeurs depuis l'intent
     */
    private fun loadFromIntent() {
        try {
            // Récupération des personnes
            var person: Person = intent.extras.get("Person") as Person

            // Affichage des valeurs dans les champs
            firstNameTextView.text = person.firstName
            lastNameTextView.text = person.lastName
        } catch (e: Exception) {
            var toast: Toast = Toast.makeText(this, "Impossible de récupérer la personne", Toast.LENGTH_LONG)
            toast.show()
        }
    }

    /**
     * Chargement des contrôles
     */
    private fun initControls() {
        firstNameTextView = findViewById(R.id.firstNameTextView)
        lastNameTextView = findViewById(R.id.lastNameTextView)
    }

    /**
     * Chargement des actions des contrôles
     */
    private fun initActions() {

    }
}