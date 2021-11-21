package com.application.wallware.alarm

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.wallware.databinding.ItemAlarmBinding
import com.google.firebase.firestore.FirebaseFirestore

class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    private val alarmList = ArrayList<AlarmModel>()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<AlarmModel>) {
        alarmList.clear()
        alarmList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(alarmList[position])
    }

    override fun getItemCount(): Int = alarmList.size

    inner class ViewHolder(private val binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
        fun bind(model: AlarmModel) {
            with(binding) {
                name.text = model.name
                if(model.time == 0) {
                    time.text = "Setiap Hari"
                } else {
                    time.text = "HARI KE - 1"
                }


                view3.setOnClickListener {
                    AlertDialog.Builder(itemView.context)
                        .setTitle("Konfirmasi Menghapus Alarm")
                        .setMessage("Apakah anda yakin ingin menghapus alarm ini ?")
                        .setPositiveButton("YA") { dialogInterface, _ ->


                            val mProgressDialog = ProgressDialog(itemView.context)
                            mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
                            mProgressDialog.setCanceledOnTouchOutside(false)
                            mProgressDialog.show()

                            model.uid?.let { it1 ->
                                FirebaseFirestore
                                    .getInstance()
                                    .collection("alarm")
                                    .document(it1)
                                    .delete()
                                    .addOnCompleteListener {


                                        if(it.isSuccessful) {
                                            dialogInterface.dismiss()
                                            mProgressDialog.dismiss()
                                            alarmList.remove(alarmList[layoutPosition])
                                            notifyDataSetChanged()
                                            Toast.makeText(itemView.context, "Sukses menghapus alarm", Toast.LENGTH_SHORT).show()

                                        } else {
                                            mProgressDialog.dismiss()
                                            Toast.makeText(itemView.context, "Gagal menghapus alarm", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }

                        }
                        .setNegativeButton("TIDAK", null)
                        .show()
                }
            }
        }

    }
}