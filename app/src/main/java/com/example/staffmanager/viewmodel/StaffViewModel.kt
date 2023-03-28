package com.example.staffmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.staffmanager.database.Staff
import com.example.staffmanager.database.StaffDatabase
import com.example.staffmanager.repository.StaffRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StaffViewModel(application: Application) : AndroidViewModel(application) {

    val repository: StaffRepository
    val allStaffs: LiveData<List<Staff>>

    init {
        val dao = StaffDatabase.getDatabase(application).getStaffDao()
        repository = StaffRepository(dao)
        allStaffs = repository.allStaffs
    }

    fun deleteStaff(staff: Staff) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(staff)
    }

    fun updateStaff(staff: Staff) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(staff)
    }

    fun addStaff(staff: Staff) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(staff)
    }

}