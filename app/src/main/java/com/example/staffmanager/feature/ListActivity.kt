package com.example.staffmanager.feature

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.staffmanager.R
import com.example.staffmanager.adapter.StaffRVAdapter
import com.example.staffmanager.database.Staff
import com.example.staffmanager.databinding.ActivityListBinding
import com.example.staffmanager.key.*
import com.example.staffmanager.viewmodel.StaffViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListActivity : AppCompatActivity(), StaffRVAdapter.StaffClickInterface,
    StaffRVAdapter.StaffClickDeleteInterface, StaffRVAdapter.StaffFocusInterface {

    private lateinit var staffsRV: RecyclerView
    private lateinit var btnAddFAB: FloatingActionButton
    private lateinit var viewModel: StaffViewModel

    private lateinit var binding: ActivityListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val staffRVAdapter = StaffRVAdapter(this, this, this)

        staffsRV = binding.staffsRV
        btnAddFAB = binding.btnAddFAB

        staffsRV.layoutManager = LinearLayoutManager(this)

        staffsRV.adapter = staffRVAdapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[StaffViewModel::class.java]

        viewModel.allStaffs.observe(this) { list ->
            list?.let {
                staffRVAdapter.updateList(it)
            }
        }
        btnAddFAB.setOnClickListener {
            val intent = Intent(this@ListActivity, AddEditItemActivity::class.java)
            startActivity(intent)
        }

        supportActionBar?.title = getString(R.string.list_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStaffClick(member: Staff) {
        val intent = Intent(this@ListActivity, AddEditItemActivity::class.java)

        intent.putExtra(TYPE, getString(R.string.type_edit))
        intent.putExtra(AVATAR, member.staffAvatar)
        intent.putExtra(NAME, member.staffName)
        intent.putExtra(GENDER, member.staffGender)
        intent.putExtra(WORK, member.staffWork)
        intent.putExtra(ID, member.id)
        intent.putExtra(BITMAP, member.staffAvatarBitmap)
        try {
            startActivity(intent)
        } catch (e: Exception) {
            println("-------------------ERROR-------------:" + e.message)
        }
    }

    override fun onDeleteIconClick(member: Staff) {
        showConfirmDelete(member)
    }

    override fun onStaffFocus(member: Staff) {
        showConfirmDelete(member)
    }

    private fun showConfirmDelete(staff: Staff) {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.delete))
            .setMessage(getString(R.string.delete_warning))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                confirmDelete(staff)
            }
            .setNegativeButton(getString(R.string.no)) { _, _ ->
                MaterialAlertDialogBuilder(this).create().dismiss()
            }
            .show()
    }

    private fun confirmDelete(staff: Staff) {
        viewModel.deleteStaff(staff)
    }

}