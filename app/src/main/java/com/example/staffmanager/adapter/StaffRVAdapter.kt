package com.example.staffmanager.adapter

import android.content.Context
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
    val ctx: Context,
    val staffClickInterface: StaffClickInterface,
    val staffClickDeleteInterface: StaffClickDeleteInterface,
    val staffFocusInterface: StaffFocusInterface,
): RecyclerView.Adapter<StaffRVAdapter.ViewHolder>() {

    private val allStaffs = ArrayList<Staff>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val staffName = itemView.findViewById<TextView>(R.id.textName)
        val staffAvatar = itemView.findViewById<ImageView>(R.id.imageAvatar)
        val staffGender = itemView.findViewById<TextView>(R.id.textGender)
        val staffWork = itemView.findViewById<TextView>(R.id.textWork)
        val staffDelete = itemView.findViewById<ImageButton>(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.staff_rv_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.staffName.setText("Name: " + allStaffs.get(position).staffName)
        holder.staffGender.setText("Gender: " +allStaffs.get(position).staffGender)
        holder.staffWork.setText("Work: " + allStaffs.get(position).staffWork)

        val avatarBitmap = allStaffs.get(position).staffAvatarBitmap


        if(avatarBitmap != null){
            val bitmap = BitmapFactory.decodeByteArray(avatarBitmap, 0,avatarBitmap!!.size )
            holder.staffAvatar.setImageBitmap(bitmap)
        }
        else {
            holder.staffAvatar.setImageResource(allStaffs.get(position).staffAvatar)
        }


        holder.staffDelete.setOnClickListener() {
            staffClickDeleteInterface.onDeleteIconClick(allStaffs.get(position))
        }

        holder.itemView.setOnClickListener() {
            staffClickInterface.onStaffClick(allStaffs.get(position))
        }

        holder.itemView.setOnLongClickListener {
            staffFocusInterface.onStaffFocus(allStaffs.get(position))
            true
        }
    }

    override fun getItemCount(): Int {
        return allStaffs.size
    }

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