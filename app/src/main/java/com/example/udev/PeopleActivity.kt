package com.example.udev

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.udev.classes.Person

class PeopleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people)

        var people: ArrayList<Person> = intent.extras.get("People") as ArrayList<Person>
        for (person in people) {
            Log.d("TRACE", person.firstName)
        }
    }
}