package com.example.staffmanager.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StaffDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(staff: Staff)

    @Delete
    fun delete(staff: Staff)

    @Update
    fun update(staff: Staff)

    @Query("SELECT * FROM tableStaff ORDER BY id")
    fun getAllStaffs():LiveData<List<Staff>>
}