package com.example.hypekicks_jaworowski_castalano

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.hypekicks_jaworowski_castalano.databinding.ActivityStorefrontBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

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
        // Wymaganie: Czysty powrót - wyczyszczenie wyszukiwarki
        binding.searchView.setQuery("", false)
        binding.searchView.clearFocus()
    }

    private fun fetchDataFromCloud() {
        db.collection("sneakers")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("FIREBASE_ERROR", "Błąd pobierania danych: ", e)
                    return@addSnapshotListener
                }
                
                sneakersList.clear()
                for (document in snapshots!!) {
                    val sneaker = document.toObject(Sneaker::class.java)
                    sneakersList.add(sneaker)
                }
                adapter.updateData(sneakersList)
            }
    }
    
    // Funkcja pomocnicza do wypełnienia bazy (użyj jeśli potrzebujesz)
    private fun seeDataBase() {
        val list = listOf(
            Sneaker("", "Nike", "Air Jordan 1 Chicago", 2022, 2500.0, "https://i.postimg.cc/PqJJLGX3/jordan1.png"),
            Sneaker("", "Adidas", "Yeezy 350", 2022, 1100.0, "https://i.postimg.cc/9f0043mx/yeezy350steelgrey2.webp")
        )
        for (s in list) db.collection("sneakers").add(s)
    }
}
