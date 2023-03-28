package com.example.staffmanager.adapter

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.staffmanager.R
import com.example.staffmanager.database.Staff

class StaffRVAdapter(
    private val staffClickInterface: StaffClickInterface,
    private val staffClickDeleteInterface: StaffClickDeleteInterface,
    private val staffFocusInterface: StaffFocusInterface,
): RecyclerView.Adapter<StaffRVAdapter.ViewHolder>() {

    private val allStaffs = ArrayList<Staff>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val staffName = itemView.findViewById<TextView>(R.id.textName)!!
        val staffAvatar = itemView.findViewById<ImageView>(R.id.imageAvatar)!!
        val staffGender = itemView.findViewById<TextView>(R.id.textGender)!!
        val staffWork = itemView.findViewById<TextView>(R.id.textWork)!!
        val staffDelete = itemView.findViewById<ImageButton>(R.id.btnDelete)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.staff_rv_item, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.staffName.text = "Name: " + allStaffs[position].staffName
        holder.staffGender.text = "Gender: " + allStaffs[position].staffGender
        holder.staffWork.text = "Work: " + allStaffs[position].staffWork

        val avatarBitmap = allStaffs[position].staffAvatarBitmap


        val bitmap = BitmapFactory.decodeByteArray(avatarBitmap, 0, avatarBitmap.size )
        holder.staffAvatar.setImageBitmap(bitmap)


        holder.staffDelete.setOnClickListener {
            staffClickDeleteInterface.onDeleteIconClick(allStaffs[position])
        }

        holder.itemView.setOnClickListener {
            staffClickInterface.onStaffClick(allStaffs[position])
        }

        holder.itemView.setOnLongClickListener {
            staffFocusInterface.onStaffFocus(allStaffs[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return allStaffs.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Staff>) {
        allStaffs.clear()
        allStaffs.addAll(newList)
        notifyDataSetChanged()
    }

    interface StaffClickInterface {
        fun onStaffClick(member: Staff)
    }

    interface StaffClickDeleteInterface {
        fun onDeleteIconClick(member: Staff)
    }

    interface StaffFocusInterface {
        fun onStaffFocus(member: Staff)
    }
}