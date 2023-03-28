package com.example.staffmanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE `tableStaff` ADD COLUMN uri_avatar TEXT NOT NULL DEFAULT ''"
        )
    }
}
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE `tableStaff` ADD COLUMN bitmap_avatar TEXT NOT NULL DEFAULT ''"
        )
    }
}

@Database(entities = arrayOf(Staff::class), version = 3, exportSchema = false)
abstract class StaffDatabase : RoomDatabase() {

    abstract fun getStaffDao(): StaffDao

    companion object {
        @Volatile
        private var INSTANCE: StaffDatabase? = null

        fun getDatabase(context: Context): StaffDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, StaffDatabase::class.java,
                    "staff_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}