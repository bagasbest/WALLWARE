package com.application.wallware.catalogue

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.wallware.databinding.ItemCatalogueBinding
import com.bumptech.glide.Glide

import android.widget.Toast

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
        val binding =
            ItemCatalogueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(catalogueList[position])
    }

    override fun getItemCount(): Int = catalogueList.size

    inner class ViewHolder(private val binding: ItemCatalogueBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
        fun bind(model: CatalogueModel) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(model.image)
                    .into(image)
                name.text = model.name
                description.text = model.description
                if (model.category == "Sayuran") {
                    time.text = "Waktu Panen: ${model.time} hari setelah ditanam"
                } else {
                    if (model.time == 0) {
                        time.text = "Intensitas Penyiraman: Setiap Hari"
                    } else {
                        time.text = "Intensitas Penyiraman: ${model.time} hari sekali"
                    }
                }


                cv.setOnClickListener {

                    val options = arrayOf("Hidupkan alarm tanaman ini", "Hapus katalog tanaman ini")

                    AlertDialog.Builder(itemView.context)
                        .setTitle("Pilihan")
                        .setItems(options) { dialogInterface, which ->

                            if (which == 0) {

                                /// cek apakah alarm sudah menyala di page alarm
                                model.uid?.let { it1 ->
                                    Firebase
                                        .firestore
                                        .collection("alarm")
                                        .document(it1)
                                        .get()
                                        .addOnSuccessListener {
                                            if (it.exists()) {
                                                Toast.makeText(
                                                    itemView.context,
                                                    "Gagal menghidupkan alarm, karena alarm tanaman ini sudah anda hidupkan, silahkan cek pada halaman alarm",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                val data = hashMapOf(
                                                    "name" to model.name,
                                                    "time" to model.time,
                                                    "category" to model.category,
                                                    "uid" to model.uid,
                                                )

                                                Firebase
                                                    .firestore
                                                    .collection("alarm")
                                                    .document(model.uid!!)
                                                    .set(data)
                                                    .addOnCompleteListener { alarmAdd ->
                                                        if (alarmAdd.isSuccessful) {
                                                            Toast.makeText(
                                                                itemView.context,
                                                                "Sukses menghidupkan alarm, silahkan cek halaman alarm",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        } else {
                                                            Toast.makeText(
                                                                itemView.context,
                                                                "Ups, terdapat kendala ketika menghidupkan alarm, cek koneksi internet anda",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }

                                            }
                                        }
                                }


                            } else {
                                val mProgressDialog = ProgressDialog(itemView.context)
                                mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
                                mProgressDialog.setCanceledOnTouchOutside(false)
                                mProgressDialog.show()

                                model.uid?.let { it1 ->
                                    FirebaseFirestore
                                        .getInstance()
                                        .collection("catalogue")
                                        .document(it1)
                                        .delete()
                                        .addOnCompleteListener {


                                            if (it.isSuccessful) {
                                                val firebaseStorage = FirebaseStorage.getInstance()
                                                model.image?.let { it2 ->
                                                    firebaseStorage.getReferenceFromUrl(it2)
                                                        .delete()
                                                        .addOnCompleteListener { task ->


                                                            if (task.isSuccessful) {
                                                                mProgressDialog.dismiss()
                                                                catalogueList.remove(catalogueList[adapterPosition])
                                                                notifyDataSetChanged()
                                                                Toast.makeText(
                                                                    itemView.context,
                                                                    "Sukses menghapus katalog tanaman",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()

                                                            } else {
                                                                mProgressDialog.dismiss()
                                                                Toast.makeText(
                                                                    itemView.context,
                                                                    "Gagal menghapus katalog tanaman",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()

                                                            }


                                                        }
                                                }


                                            } else {
                                                mProgressDialog.dismiss()
                                                Toast.makeText(
                                                    itemView.context,
                                                    "Gagal menghapus katalog tanaman",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                }
                            }
                            dialogInterface.dismiss()
                        }
                        .create().show()
                }

            }
        }

    }
}