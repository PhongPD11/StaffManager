package com.example.staffmanager.feature

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.staffmanager.adapter.StaffRVAdapter
import com.example.staffmanager.database.Staff
import com.example.staffmanager.databinding.ActivityListBinding
import com.example.staffmanager.viewmodel.StaffViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListActivity : AppCompatActivity(), StaffRVAdapter.StaffClickInterface,
    StaffRVAdapter.StaffClickDeleteInterface, StaffRVAdapter.StaffFocusInterface {

    lateinit var staffsRV: RecyclerView
    lateinit var btnAddFAB: FloatingActionButton
    lateinit var viewModel: StaffViewModel

    private lateinit var binding: ActivityListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val staffRVAdapter = StaffRVAdapter(this, this, this, this)

        staffsRV = binding.staffsRV
        btnAddFAB = binding.btnAddFAB

        staffsRV.layoutManager = LinearLayoutManager(this)

        staffsRV.adapter = staffRVAdapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(StaffViewModel::class.java)

        viewModel.allStaffs.observe(this, Observer { list ->
            list?.let {
                staffRVAdapter.updateList(it)
            }
        })
        btnAddFAB.setOnClickListener {
            val intent = Intent(this@ListActivity, AddEditItemActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStaffClick(staff: Staff) {
        val intent = Intent(this@ListActivity, AddEditItemActivity::class.java)
        intent.putExtra("type", "Edit")
        intent.putExtra("staffAvatar", staff.staffAvatar)
        intent.putExtra("staffName", staff.staffName)
        intent.putExtra("staffGender", staff.staffGender)
        intent.putExtra("staffWork", staff.staffWork)
        intent.putExtra("staffId", staff.id)
        intent.putExtra("staffAvatarBitmap", staff.staffAvatarBitmap)
        try {
            println("--------------------------------Log vô nè baaaaaaaaaaaaaaaaa:" +staff.staffAvatarBitmap)
            startActivity(intent)
        }catch (e: Exception){
            println("-------------------------------error baaaaaaaaaaaaaaaaa:" + e.message)
//            intent.putExtra("staffAvatarBitmap", "")
//            startActivity(intent)
//            this.finish()
        }

    }

    override fun onDeleteIconClick(staff: Staff) {
        showConfirmDelete(staff)
    }

    override fun onStaffFocus(staff: Staff) {
        showConfirmDelete(staff)
    }

    private fun showConfirmDelete(staff: Staff) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete")
            .setMessage("Do you want to delete?")
            .setCancelable(true)
            .setPositiveButton("Yes") { _, _ ->
                confirmDelete(staff)
            }
            .setNegativeButton("No") { _, _ ->
                MaterialAlertDialogBuilder(this).create().dismiss()
            }
            .show()
    }

    private fun confirmDelete(staff: Staff) {
        viewModel.deleteStaff(staff)
    }

}