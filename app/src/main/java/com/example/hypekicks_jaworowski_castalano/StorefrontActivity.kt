package com.example.hypekicks_jaworowski_castalano

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.hypekicks_jaworowski_castalano.databinding.ActivityStorefrontBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import java.util.Locale

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

        // seeDataBase() // Odkomentuj, aby dodać startowe dane

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
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("SNEAKER_DATA", sneaker)
            startActivity(intent)
        }

        binding.btnAdminPanel.setOnClickListener {
            val intent = Intent(this, AdminPanelActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.searchView.setQuery("", false)
        binding.searchView.clearFocus()
    }

    private fun seeDataBase() {
        val sneakerList = listOf(
            Sneaker("Nike", "Air Jordan 1 Chicago", 2022, 2500.0, "https://i.postimg.cc/PqJJLGX3/jordan1.png"),
            Sneaker("Adidas", "Yeezy Boost 350 V2 Steel Gray", 2022, 1100.0, "https://i.postimg.cc/9f0043mx/yeezy350steelgrey2.webp"),
            Sneaker("Nike", "Dunk Low Panda", 2021, 650.0, "https://i.postimg.cc/MGHHck65/675010-full-product.jpg"),
            Sneaker("Jordan", "Air Jordan 4 Pine Green", 2023, 1900.0, "https://i.postimg.cc/bYxywY0G/image.png")
        )

        for (sneaker in sneakerList) {
            db.collection("sneakers")
                .add(sneaker)
                .addOnSuccessListener {
                    Log.d("FIREBASE_TEST", "Sukces! Dodano buta: ${sneaker.modelName}")
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
            .addOnSuccessListener { documents: QuerySnapshot ->
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
