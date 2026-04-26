package com.example.hypekicks_jaworowski_castalano

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hypekicks_jaworowski_castalano.databinding.ActivityDetailsBinding
import java.util.Locale

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sneaker = intent.getSerializableExtra("SNEAKER_DATA") as? Sneaker

        sneaker?.let {
            binding.txtDetailsBrand.text = it.brand
            binding.txtDetailsModel.text = it.modelName
            binding.txtDetailsYear.text = "Rok wydania: ${it.releaseYear}"
            binding.txtDetailsPrice.text = String.format(Locale.getDefault(), "%.2f PLN", it.resellPrice)

            Glide.with(this)
                .load(it.imageUrl)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(binding.imgDetails)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
