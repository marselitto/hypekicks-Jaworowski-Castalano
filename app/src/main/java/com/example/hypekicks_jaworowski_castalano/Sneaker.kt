package com.example.hypekicks_jaworowski_castalano

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class Sneaker(
    @DocumentId
    val id: String = "",
    val brand: String = "",
    val modelName: String = "",
    val releaseYear: Int = 0,
    val resellPrice: Double = 0.0,
    val imageUrl: String = ""
) : Serializable
