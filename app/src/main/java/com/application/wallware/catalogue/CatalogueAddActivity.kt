package com.application.wallware.catalogue

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.application.wallware.R
import com.application.wallware.databinding.ActivityCatalogueAddBinding
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class CatalogueAddActivity : AppCompatActivity() {

    private var binding: ActivityCatalogueAddBinding? = null
    private var image: String? = null
    private var status: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogueAddBinding.inflate(layoutInflater)
        setContentView(binding?.root)



        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.category, android.R.layout.simple_list_item_1
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.category?.setAdapter(adapter)
        binding?.category?.setOnItemClickListener { adapterView, view, i, l ->
            status = binding?.category?.text.toString()
        }


        binding?.imageHint?.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .start(REQUEST_FROM_CAMERA_TO_SELF_PHOTO)
        }

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }


        binding?.saveBtn?.setOnClickListener {
            formValidation()
        }

        binding?.info?.setOnClickListener {
            showAlertDialog()
        }

    }

    private fun showAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("Keterangan")
            .setMessage("Masukkan angka 0, jika intensitas penyiraman setiap hari")
            .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
            .setPositiveButton("OKE") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun formValidation() {
        val name = binding?.name?.text.toString().trim()
        val description = binding?.description?.text.toString().trim()
        val time = binding?.time?.text.toString().trim()

        when {
            name.isEmpty() -> {
                Toast.makeText(this, "Nama Tanaman tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
            description.isEmpty() -> {
                Toast.makeText(this, "Deskripsi Tanaman tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
            time.isEmpty() -> {
                Toast.makeText(this, "Waktu Panen tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
            image == null -> {
                Toast.makeText(this, "Gambar Tanaman tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
            status == null -> {
                Toast.makeText(this, "Kategori tanaman tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
        }


        binding?.progressBar?.visibility = View.VISIBLE

        val uid = System.currentTimeMillis().toString()

        val data = hashMapOf(
            "name" to name,
            "nameTemp" to name.lowercase(Locale.getDefault()),
            "description" to description,
            "time" to time.toInt(),
            "image" to image,
            "uid" to uid,
            "category" to status,
        )

        Firebase
            .firestore
            .collection("catalogue")
            .document(uid)
            .set(data)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    binding?.progressBar?.visibility = View.GONE
                    showSuccessDialog()
                } else {
                    binding?.progressBar?.visibility = View.GONE
                    showFailureDialog()
                }
            }


    }

    private fun showFailureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Mengunggah Tanaman")
            .setMessage("Terdapat kesalahan ketika mengunggah tenaman, silahkan periksa koneksi internet anda, dan coba lagi nanti")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Berhasil Mengunggah Tanaman")
            .setMessage("berhasil mengunggah katalog tanaman")
            .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
            .setPositiveButton("OKE") { dialogInterface, i ->
                dialogInterface.dismiss()
                onBackPressed()
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_FROM_CAMERA_TO_SELF_PHOTO -> {
                    uploadImageOption(data?.data!!)
                }
            }
        }
    }

    private fun uploadImageOption(data: Uri) {
        val mStorageRef = FirebaseStorage.getInstance().reference
        val TAG = CatalogueModel::class.java.simpleName

        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
        val imageFileName = "image_${System.currentTimeMillis()}.png"
        val uploadTask = mStorageRef.child(imageFileName).putFile(data)
        uploadTask.addOnSuccessListener {
            Log.e(TAG, "Image load successfully...")
            val downloadUriTask = mStorageRef.child(imageFileName).downloadUrl
            downloadUriTask.addOnSuccessListener {
                image = it.toString()
                Glide.with(this)
                    .load(image)
                    .into(binding!!.image)
                mProgressDialog.dismiss()

            }.addOnFailureListener {
                mProgressDialog.dismiss()
            }
        }.addOnFailureListener {
            Log.e(TAG, "Image upload failed ${it.printStackTrace()}")
            mProgressDialog.dismiss()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val REQUEST_FROM_CAMERA_TO_SELF_PHOTO = 1001
    }
}