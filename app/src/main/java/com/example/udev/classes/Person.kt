package com.example.udev.classes

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Person(
    @SerializedName("prenom") var firstName: String?,
    @SerializedName("nom") var lastName: String?
) : Serializable