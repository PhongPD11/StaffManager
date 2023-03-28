package com.example.staffmanager.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tableStaff")
class Staff(
    @ColumnInfo(name = "avatar") val staffAvatar: Int,
    @ColumnInfo(name = "name") val staffName: String,
    @ColumnInfo(name = "gender") val staffGender: String,
    @ColumnInfo(name = "work") val staffWork: String,
    @ColumnInfo(name = "bitmap_avatar") val staffAvatarBitmap: ByteArray,
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}