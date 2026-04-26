package com.example.hypekicks_jaworowski_castalano

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.hypekicks_jaworowski_castalano.databinding.ActivityStorefrontBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StorefrontActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStorefrontBinding
    private lateinit var sneakersList: MutableList<Sneaker>
    private lateinit var adapter: SneakerAdapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorefrontBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sneakersList = mutableListOf()
        adapter = SneakerAdapter(this, sneakersList)
        binding.gridView.adapter = adapter


         //seeDataBase()

        fetchDataFromCloud()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText ?: "")
                return true
            }
        })

        binding.gridView.setOnItemClickListener { _, _, position, _ ->
            val sneaker = adapter.getItem(position) as Sneaker
            Toast.makeText(this, "Model: ${sneaker.modelName}", Toast.LENGTH_SHORT).show()
        }

        binding.btnAdminPanel.setOnClickListener {
            Toast.makeText(this, "Otwieranie Panelu Administratora...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun seeDataBase() {
        val sneakerList = listOf(
            Sneaker("Nike", "Air Jordan 1 Chicago", 2022, 2500.0, "https://i.postimg.cc/PqJJLGX3/jordan1.png"),
            Sneaker("Adidas", "Yeezy Boost 350 V2 Steel Gray", 2022, 1100.0, "https://i.postimg.cc/9f0043mx/yeezy350steelgrey2.webp"),
            Sneaker("Nike", "Dunk Low Panda", 2021, 650.0, "https://i.postimg.cc/MGHHck65/675010-full-product.jpg"),
            Sneaker("Jordan", "Air Jordan 4 Pine Green", 2023, 1900.0, "https://i.postimg.cc/bYxywY0G/image.png")
        )

        val firestoreDb = Firebase.firestore

        for (sneaker in sneakerList) {
            firestoreDb.collection("sneakers")
                .add(sneaker)
                .addOnSuccessListener {
                    Log.d("FIREBASE_TEST", "Sukces! Dodano buta: ${sneaker.modelName}")
                    // Po dodaniu wszystkich, odśwież listę
                    fetchDataFromCloud()
                }
                .addOnFailureListener { e ->
                    Log.e("FIREBASE_TEST", "Błąd podczas dodawania: ", e)
                }
        }
    }

    private fun fetchDataFromCloud() {
        db.collection("sneakers")
            .get()
            .addOnSuccessListener { documents ->
                sneakersList.clear()
                for (document in documents) {
                    val sneaker = document.toObject(Sneaker::class.java)
                    sneakersList.add(sneaker)
                }
                adapter.updateData(sneakersList)
            }
            .addOnFailureListener { exception ->
                Log.e("FIREBASE_ERROR", "Błąd pobierania danych: ", exception)
                Toast.makeText(this, "Błąd pobierania danych z Firestore!", Toast.LENGTH_LONG).show()
            }
    }
}
