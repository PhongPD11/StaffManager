package com.example.staffmanager.repository

import androidx.lifecycle.LiveData
import com.example.staffmanager.database.Staff
import com.example.staffmanager.database.StaffDao

class StaffRepository(private val staffDao: StaffDao) {

    val allStaffs: LiveData<List<Staff>> = staffDao.getAllStaffs()

    suspend fun insert(staff: Staff) {
        staffDao.insert(staff)
    }

    suspend fun delete(staff: Staff) {
        staffDao.delete(staff)
    }

    suspend fun update(staff: Staff) {
        staffDao.update(staff)
    }

}