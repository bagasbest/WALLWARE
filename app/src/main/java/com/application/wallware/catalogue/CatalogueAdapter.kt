package com.application.wallware.catalogue

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.wallware.databinding.ItemCatalogueBinding
import com.bumptech.glide.Glide

import android.widget.Toast

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class CatalogueAdapter : RecyclerView.Adapter<CatalogueAdapter.ViewHolder>() {

    private val catalogueList = ArrayList<CatalogueModel>()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<CatalogueModel>) {
        catalogueList.clear()
        catalogueList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCatalogueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(catalogueList[position])
    }

    override fun getItemCount(): Int = catalogueList.size

    inner class ViewHolder(private val binding: ItemCatalogueBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(model: CatalogueModel) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(model.image)
                    .into(image)
                name.text = model.name
                description.text = model.description
                time.text = "Waktu Panen: $model"


                cv.setOnClickListener {
                    AlertDialog.Builder(itemView.context)
                        .setTitle("Konfirmasi Menghapus Katalog Tanaman")
                        .setMessage("Apakah anada yakin ingin menghapus katalog tanaman ini ?")
                        .setPositiveButton("YA") { dialogInterface, _ ->
                            model.uid?.let { it1 ->
                                FirebaseFirestore
                                    .getInstance()
                                    .collection("catalogue")
                                    .document(it1)
                                    .delete()
                                    .addOnCompleteListener {


                                        if(it.isSuccessful) {
                                            val firebaseStorage = FirebaseStorage.getInstance()
                                            model.image?.let { it2 ->
                                                firebaseStorage.getReferenceFromUrl(it2).delete()
                                                    .addOnCompleteListener { task ->


                                                            if(task.isSuccessful) {
                                                                catalogueList.removeAt(
                                                                    adapterPosition
                                                                )
                                                                Toast.makeText(itemView.context, "Sukses menghapus katalog tanaman", Toast.LENGTH_SHORT).show()

                                                            } else {
                                                                Toast.makeText(itemView.context, "Gagal menghapus katalog tanaman", Toast.LENGTH_SHORT).show()

                                                            }


                                                    }
                                            }


                                        } else {
                                            Toast.makeText(itemView.context, "Gagal menghapus katalog tanaman", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }
                            dialogInterface.dismiss()
                        }
                        .setNegativeButton("TIDAK", null)
                        .show()
                }

            }
        }

    }
}