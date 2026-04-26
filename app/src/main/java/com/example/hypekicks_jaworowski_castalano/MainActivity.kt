package com.example.hypekicks_jaworowski_castalano

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hypekicks_jaworowski_castalano.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Odkomentuj poniższą linię RAZ, aby wysłać te buty do bazy danych:
        //seeDataBase()
    }

    private fun seeDataBase() {
        val sneakerList = listOf(
            Sneaker("Nike", "Air Jordan 1 Chicago", 2022, 2500.0, "https://i.postimg.cc/PqJJLGX3/jordan1.png"),
            Sneaker("Adidas", "Yeezy Boost 350 V2 Steel Gray", 2022, 1100.0, "https://i.postimg.cc/9f0043mx/yeezy350steelgrey2.webp"),
            Sneaker("Nike", "Dunk Low Panda", 2021, 650.0, "https://i.postimg.cc/MGHHck65/675010-full-product.jpg"),
            Sneaker("Jordan", "Air Jordan 4 Pine Green", 2023, 1900.0, "https://i.postimg.cc/bYxywY0G/image.png"),
        )

        val db = Firebase.firestore

        for (sneaker in sneakerList) {
            db.collection("sneakers")
                .add(sneaker)
                .addOnSuccessListener {
                    Log.d("FIREBASE_TEST", "Sukces! Dodano buta: ${sneaker.modelName}")
                }
                .addOnFailureListener { e ->
                    Log.e("FIREBASE_TEST", "Błąd podczas dodawania: ", e)
                }
        }
    }
}
