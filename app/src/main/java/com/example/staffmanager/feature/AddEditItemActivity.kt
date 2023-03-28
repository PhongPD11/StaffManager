package com.example.staffmanager.feature

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.staffmanager.database.Staff
import com.example.staffmanager.databinding.ActivityAddEditItemBinding
import com.example.staffmanager.viewmodel.StaffViewModel
import java.io.ByteArrayOutputStream


class AddEditItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditItemBinding
    lateinit var staffNameEdit: EditText
    lateinit var staffGenderEdit: EditText
    lateinit var staffWorkEdit: EditText
    lateinit var btnSaveEdit: Button
    lateinit var avatarEdit: ImageView
    lateinit var viewModel: StaffViewModel
    var staffId = -1
    var staffAvatar : Int = 0
    var staffAvatarBitmap : ByteArray? = null

    private val pickImage = 100
    private var imageUri: Uri? = null
    private var saveAvatar: ByteArray? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivityAddEditItemBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val defaultAvatar = com.example.staffmanager.R.drawable.ic_staff
            staffNameEdit = binding.editTextName
            staffGenderEdit = binding.editTextGender
            staffWorkEdit = binding.editTextWork
            btnSaveEdit = binding.btnEditStaff
            avatarEdit = binding.imageEditAvatar

            viewModel = ViewModelProvider(
                this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            ).get(StaffViewModel::class.java)
            val type = intent.getStringExtra("type")
            if (type.equals("Edit")) {
                staffAvatar = intent.getIntExtra("staffAvatar", defaultAvatar)
                val staffName = intent.getStringExtra("staffName")
                val staffGender = intent.getStringExtra("staffGender")
                val staffWork = intent.getStringExtra("staffWork")
                staffAvatarBitmap = intent.getByteArrayExtra("staffAvatarBitmap")
                staffId = intent.getIntExtra("staffId", -1)

                staffNameEdit.setText(staffName)
                staffGenderEdit.setText(staffGender)
                staffWorkEdit.setText(staffWork)

                if (staffAvatarBitmap != null){
                    val bitmap = BitmapFactory.decodeByteArray(staffAvatarBitmap, 0,staffAvatarBitmap!!.size )
                    avatarEdit.setImageBitmap(bitmap)
                } else {
                    avatarEdit.setImageResource(defaultAvatar)
                }
            }


            btnSaveEdit.setOnClickListener() {
                val staffName = staffNameEdit.text.toString()
                val staffGender = staffGenderEdit.text.toString()
                val staffWork = staffWorkEdit.text.toString()
                val staffAvatar = intent.getIntExtra("staffAvatar", defaultAvatar)
                staffAvatarBitmap = intent.getByteArrayExtra("staffAvatarBitmap")
                val staffAvatarChange = saveAvatar

                if (type.equals("Edit")) {
                    if (staffName.isNotEmpty() && staffWork.isNotEmpty() && staffGender.isNotEmpty()) {
                        if (staffAvatarChange != null){
                            updateStaff(staffAvatar, staffName, staffGender, staffWork, staffAvatarChange)
                        } else if (staffAvatarBitmap != null){
                            updateStaff(staffAvatar, staffName, staffGender, staffWork, staffAvatarBitmap!!)
                        } else{
                            Toast.makeText(this, "Choose your avatar", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    if (staffName.isNotEmpty() && staffWork.isNotEmpty() && staffGender.isNotEmpty() && staffAvatarChange != null) {
                        addStaff(staffAvatar, staffName, staffGender, staffWork, staffAvatarChange)
                    }
                }
                startActivity(Intent(applicationContext, ListActivity::class.java))
            }

            binding.imageEditAvatar.setOnClickListener {
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, pickImage)
            }
        }catch (e:Exception){
            println("ERROR ----------------------------------------------${e.message}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            val inputStream = contentResolver.openInputStream(imageUri!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
            val compressedByteArray = outputStream.toByteArray()

            saveAvatar = compressedByteArray
            avatarEdit.setImageBitmap(bitmap)
        }
    }

    private fun updateStaff(
        staffAvatar: Int,
        staffName: String,
        staffGender: String,
        staffWork: String,
        staffAvatarBitmap: ByteArray
    ) {
        val updateStaff = Staff(staffAvatar, staffName, staffGender, staffWork, staffAvatarBitmap)
        updateStaff.id = staffId
        viewModel.updateStaff(updateStaff)
    }

    private fun addStaff(
        staffAvatar: Int,
        staffName: String,
        staffGender: String,
        staffWork: String,
        staffAvatarBitmap: ByteArray
    ) {
        val addStaff = Staff(staffAvatar, staffName, staffGender, staffWork, staffAvatarBitmap)
        viewModel.addStaff(addStaff)
    }
}