package com.example.hypekicks_jaworowski_castalano

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypekicks_jaworowski_castalano.databinding.ActivityAdminPanelBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminPanelBinding
    private val db = FirebaseFirestore.getInstance()
    private val sneakersList = mutableListOf<Sneaker>()
    private lateinit var adapter: ArrayAdapter<String>
    private val displayList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListView()
        fetchSneakers()

        // Przycisk powrotu
        binding.btnBackAdmin.setOnClickListener {
            finish()
        }

        // CREATE: Dodawanie buta
        binding.btnAddSneaker.setOnClickListener {
            addSneaker()
        }

        // DELETE: Usuwanie po przytrzymaniu
        binding.lvSneakers.setOnItemLongClickListener { _, _, position, _ ->
            deleteSneaker(position)
            true
        }
    }

    private fun setupListView() {
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayList)
        binding.lvSneakers.adapter = adapter
    }

    private fun fetchSneakers() {
        db.collection("sneakers").addSnapshotListener { snapshots, e ->
            if (e != null) {
                Toast.makeText(this, "Błąd pobierania danych", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            
            sneakersList.clear()
            displayList.clear()
            
            for (doc in snapshots!!) {
                val sneaker = doc.toObject(Sneaker::class.java)
                sneakersList.add(sneaker)
                displayList.add("${sneaker.brand} ${sneaker.modelName} - ${sneaker.resellPrice} PLN")
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun addSneaker() {
        val brand = binding.etBrand.text.toString()
        val model = binding.etModelName.text.toString()
        val year = binding.etReleaseYear.text.toString().toIntOrNull() ?: 0
        val price = binding.etResellPrice.text.toString().toDoubleOrNull() ?: 0.0
        val url = binding.etImageUrl.text.toString()

        if (brand.isNotEmpty() && model.isNotEmpty()) {
            val newSneaker = Sneaker("", brand, model, year, price, url)
            db.collection("sneakers").add(newSneaker)
                .addOnSuccessListener {
                    Toast.makeText(this, "Dodano pomyślnie!", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Błąd dodawania", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Wypełnij markę i model!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteSneaker(position: Int) {
        val sneakerId = sneakersList[position].id
        db.collection("sneakers").document(sneakerId).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Usunięto buta", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Błąd usuwania", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        binding.etBrand.text.clear()
        binding.etModelName.text.clear()
        binding.etReleaseYear.text.clear()
        binding.etResellPrice.text.clear()
        binding.etImageUrl.text.clear()
    }
}
