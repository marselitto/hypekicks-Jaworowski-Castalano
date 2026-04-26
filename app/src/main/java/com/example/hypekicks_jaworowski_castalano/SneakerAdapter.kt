package com.example.hypekicks_jaworowski_castalano

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.example.hypekicks_jaworowski_castalano.databinding.ItemSneakerBinding
import java.util.Locale

class SneakerAdapter(
    private val context: Context,
    private var sneakersList: List<Sneaker>
) : BaseAdapter() {

    private var filteredList: List<Sneaker> = sneakersList

    override fun getCount(): Int = filteredList.size

    override fun getItem(position: Int): Any = filteredList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ItemSneakerBinding
        val view: View

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            binding = ItemSneakerBinding.inflate(inflater, parent, false)
            view = binding.root
            view.tag = binding
        } else {
            view = convertView
            binding = view.tag as ItemSneakerBinding
        }

        val sneaker = filteredList[position]

        binding.txtBrand.text = sneaker.brand
        binding.txtModelName.text = sneaker.modelName
        binding.txtPrice.text = String.format(Locale.getDefault(), "%.2f PLN", sneaker.resellPrice)

        Glide.with(context)
            .load(sneaker.imageUrl)
            .placeholder(android.R.drawable.ic_menu_report_image)
            .into(binding.imgSneaker)

        return view
    }

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            sneakersList
        } else {
            sneakersList.filter { 
                it.modelName.contains(query, ignoreCase = true) || 
                it.brand.contains(query, ignoreCase = true) 
            }
        }
        notifyDataSetChanged()
    }

    fun updateData(newList: List<Sneaker>) {
        this.sneakersList = newList
        this.filteredList = newList
        notifyDataSetChanged()
    }
}
