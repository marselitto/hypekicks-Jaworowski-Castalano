package com.example.hypekicks_jaworowski_castalano

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.hypekicks_jaworowski_castalano.databinding.ActivityStorefrontBinding
import com.google.firebase.firestore.FirebaseFirestore

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
            Toast.makeText(this, "Model: ${sneaker.modelName}", Toast.LENGTH_SHORT).show()
        }

        binding.btnAdminPanel.setOnClickListener {

            Toast.makeText(this, "Otwieranie Panelu Administratora...", Toast.LENGTH_SHORT).show()
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
