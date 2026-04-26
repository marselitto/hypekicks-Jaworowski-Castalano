package com.example.hypekicks_jaworowski_castalano

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hypekicks_jaworowski_castalano.databinding.ActivityAdminPanelBinding

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminPanelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Miejsce na logikę CRUD dla Twojego kolegi:
        // binding.btnAddSneaker.setOnClickListener { ... }
        // binding.lvSneakers.setOnItemLongClickListener { ... }
    }
}
