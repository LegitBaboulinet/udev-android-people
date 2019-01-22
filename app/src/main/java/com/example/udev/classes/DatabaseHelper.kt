package com.example.udev.classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File

class DatabaseHelper(
    private val context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    init {
        doDbCheck()
    }

    // Objet companion permettant de stocker des attributs et méthodes statiques
    companion object {
        private const val DATABASE_NAME: String = "database.db"
        private const val DATABASE_VERSION: Int = 1
        private const val DATABASE_CREATE_TABLE_PERSON =
            "CREATE TABLE Person (id INTEGER PRIMARY KEY AUTOINCREMENT, firstName TEXT, lastName TEXT)"
    }

    // --------------------------------------------------
    // Surchages
    // --------------------------------------------------
    /**
     * Surchage de la fonction onCreate venant de SQLiteOpenHelper
     */
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DATABASE_CREATE_TABLE_PERSON)
    }

    /**
     * Surchage de la fonction onUpgrade, on ne change rien car Android veut absolument qu'on la surchage
     * alors que nous n'avons rien à changer
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    // --------------------------------------------------
    // Méthodes
    // --------------------------------------------------
    /**
     * Vérifie l'existence de la base de données
     */
    private fun doDbCheck() {
        try {

            // Initialisation du fichier de base de données
            var file: File = File("data/data/com.example.udev/databases/$DATABASE_NAME")

            // Suppression du fichier de base de données
            file.delete()
        } catch (e: Exception) {
            Log.e("TRACE", "La base de données n'existe pas")
        }
    }

    /**
     * Ajoute une liste de personnes dans la base de données
     */
    public fun insertPeople(people: List<Person>, db: SQLiteDatabase) {

        // Parcours des personnes
        for (person in people) {

            // Définition des valeurs
            var values: ContentValues = ContentValues()
            values.put("firstName", person.firstName)
            values.put("lastName", person.lastName)

            // Insertion de la personne actuelle
            db.insert("Person", null, values)
        }
    }
}