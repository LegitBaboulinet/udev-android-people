package com.example.udev

import retrofit2.http.GET
import retrofit2.Callback

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.udev.classes.DatabaseHelper
import com.example.udev.classes.Person
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.NullPointerException

class MainActivity : AppCompatActivity() {

    // Connexion RetroFit
    private lateinit var retroFit: Retrofit

    // Contrôles
    private lateinit var uniqueButton: Button
    private lateinit var listButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Appel des fonctions de chargement
        init()
        initControls()
        initActions()
    }

    /**
     * Initialise les constantes de l'activité
     */
    private fun init() {

        // Création de la connexion avec RetroFit
        retroFit = Retrofit.Builder()
            .baseUrl("http://192.168.1.7:8080/WebServices/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Initialise les contrôles de l'activité
     */
    private fun initControls() {
        uniqueButton = findViewById(R.id.uniqueButton)
        listButton = findViewById(R.id.listButton)
    }

    /**
     * Initialise les actions des contrôles de l'activité
     */
    private fun initActions() {

        // Bouton liste
        listButton.setOnClickListener {
            callWebServiceForPeopleActivity()
        }

        // Bouton unique
        uniqueButton.setOnClickListener {
            callWebServiceForPersonActivity()
        }
    }

    /**
     * WebService pour une personne unique
     */
    private fun callWebServiceForPersonActivity() {

        // Désactivation du bouton 'Liste'
        uniqueButton.isEnabled = false

        // Appel du webservice à la route '/allPersonne'
        val callbackInterface: WebServiceCallbackInterface = retroFit.create(WebServiceCallbackInterface::class.java)

        // Récupération de l'appel
        val getPersonCall: Call<Person> = callbackInterface.getPerson()

        // Surchage des fonctions de succés et d'échec
        getPersonCall.enqueue(object : Callback<Person> {

            /**
             * En cas de réussite de la requête
             */
            override fun onResponse(call: Call<Person>, response: Response<Person>) {

                // Création du database helper
                var databaseHelper: DatabaseHelper = DatabaseHelper(listButton.context)

                // Récupération de la base de donneés
                var database: SQLiteDatabase = databaseHelper.writableDatabase

                try {
                    // Appel de l'activité de liste de personnes
                    startActivity(
                        Intent(uniqueButton.context, PersonActivity::class.java)
                            .putExtra(
                                "Person",
                                Person(
                                    response.body()?.firstName,
                                    response.body()?.lastName
                                )
                            )
                    )
                } catch (e: NullPointerException) {
                    var toast: Toast =
                        Toast.makeText(
                            listButton.context,
                            "Impossible d'obtenir une réponse correcte du serveur",
                            Toast.LENGTH_LONG
                        )
                    toast.show()
                }

                // Réactivation du bouton 'Unique'
                uniqueButton.isEnabled = true
            }

            /**
             * En cas d'échec de la requête
             */
            override fun onFailure(call: Call<Person>, t: Throwable) {
                var toast: Toast =
                    Toast.makeText(listButton.context, "Impossible de contacter le serveur", Toast.LENGTH_LONG)
                toast.show()
            }
        })
    }

    /**
     * WebService pour la liste de personnes
     */
    private fun callWebServiceForPeopleActivity() {

        // Désactivation du bouton 'Liste'
        listButton.isEnabled = false

        // Appel du webservice à la route '/personne'
        val callbackInterface: WebServiceCallbackInterface = retroFit.create(WebServiceCallbackInterface::class.java)

        // Récupération de l'appel
        val getPeopleCall: Call<List<Person>> = callbackInterface.getPeople()

        // Surchage des fonctions de succés et d'échec
        getPeopleCall.enqueue(object : Callback<List<Person>> {

            /**
             * En cas de réussite de la requête
             */
            override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {

                // Création du database helper
                var databaseHelper: DatabaseHelper = DatabaseHelper(listButton.context)

                // Récupération de la base de donneés
                var database: SQLiteDatabase = databaseHelper.writableDatabase

                try {

                    // Insertion des personnes dans la base de données
                    databaseHelper.insertPeople(ArrayList<Person>(response.body()), database)

                    // Appel de l'activité de liste de personnes
                    startActivity(
                        Intent(listButton.context, PeopleActivity::class.java)
                            .putExtra(
                                "People",
                                ArrayList<Person>(response.body())
                            )
                    )
                } catch (e: NullPointerException) {
                    var toast: Toast =
                        Toast.makeText(
                            listButton.context,
                            "Impossible d'obtenir une réponse correcte du serveur",
                            Toast.LENGTH_LONG
                        )
                    toast.show()
                }

                // Réactivation du bouton 'Unique'
                listButton.isEnabled = true
            }

            /**
             * En cas d'échec de la requête
             */
            override fun onFailure(call: Call<List<Person>>, t: Throwable) {
                var toast: Toast =
                    Toast.makeText(listButton.context, "Impossible de contacter le serveur", Toast.LENGTH_LONG)
                toast.show()

                // Réactivation du bouton 'Unique'
                listButton.isEnabled = true
            }
        })
    }

    /**
     * Interface définisant les routes du webservice
     */
    interface WebServiceCallbackInterface {
        @GET("personne")
        fun getPerson(): Call<Person>

        @GET("allPersonne")
        fun getPeople(): Call<List<Person>>
    }
}