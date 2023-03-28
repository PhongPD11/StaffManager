@file:Suppress("DEPRECATION")

package com.example.staffmanager.feature

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.staffmanager.R
import com.example.staffmanager.database.Staff
import com.example.staffmanager.databinding.ActivityAddEditItemBinding
import com.example.staffmanager.key.*
import com.example.staffmanager.viewmodel.StaffViewModel
import java.io.ByteArrayOutputStream


class AddEditItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditItemBinding
    private lateinit var staffNameEdit: EditText
    private lateinit var staffWorkEdit: EditText
    private lateinit var btnSaveEdit: Button
    private lateinit var avatarEdit: ImageView
    private lateinit var viewModel: StaffViewModel
    private lateinit var genderSpinner: Spinner

    private var selectedGender: String? =""
    private var staffId = -1
    private var staffAvatar : Int = 0
    private var staffAvatarBitmap : ByteArray? = null

    private val pickImage = 100
    private var imageUri: Uri? = null
    private var saveAvatar: ByteArray? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        genderSpinner = binding.spinnerGender
        ArrayAdapter.createFromResource(
            this,
            R.array.gender,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            genderSpinner.adapter = adapter
        }

        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedGender = parent.getItemAtPosition(position).toString()

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        try {
            val defaultAvatar = R.drawable.ic_staff
            staffNameEdit = binding.editTextName
            staffWorkEdit = binding.editTextWork
            btnSaveEdit = binding.btnEditStaff
            avatarEdit = binding.imageEditAvatar

            viewModel = ViewModelProvider(
                this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            )[StaffViewModel::class.java]
            val type = intent.getStringExtra(TYPE)
            if (type.equals(getString(R.string.type_edit))) {
                val staffName = intent.getStringExtra(NAME)
                val staffGender = intent.getStringExtra(GENDER)
                val staffWork = intent.getStringExtra(WORK)
                staffAvatar = intent.getIntExtra(AVATAR, defaultAvatar)
                staffAvatarBitmap = intent.getByteArrayExtra(BITMAP)
                staffId = intent.getIntExtra(ID, -1)

                staffNameEdit.setText(staffName)
                staffWorkEdit.setText(staffWork)

                val adapter = ArrayAdapter.createFromResource(this,R.array.gender ,android.R.layout.simple_spinner_item)
                val selectionIndex = adapter.getPosition(staffGender)
                genderSpinner.setSelection(selectionIndex)

                if (staffAvatarBitmap != null){
                    val bitmap = BitmapFactory.decodeByteArray(staffAvatarBitmap, 0,staffAvatarBitmap!!.size )
                    avatarEdit.setImageBitmap(bitmap)
                } else {
                    avatarEdit.setImageResource(defaultAvatar)
                }
            }

            btnSaveEdit.setOnClickListener {
                val staffName = staffNameEdit.text.toString()
                val staffGender = selectedGender.toString()
                val staffWork = staffWorkEdit.text.toString()
                val staffAvatar = intent.getIntExtra(AVATAR, defaultAvatar)
                staffAvatarBitmap = intent.getByteArrayExtra(BITMAP)
                val staffAvatarChange = saveAvatar

                if (type.equals(getString(R.string.type_edit))) {
                    if (staffName.isNotEmpty() && staffWork.isNotEmpty() && staffGender.isNotEmpty()) {
                        if (staffAvatarChange != null){
                            updateStaff(staffAvatar, staffName, staffGender, staffWork, staffAvatarChange)
                        } else if (staffAvatarBitmap != null){
                            updateStaff(staffAvatar, staffName, staffGender, staffWork, staffAvatarBitmap!!)
                        } else{
                            Toast.makeText(this, getString(R.string.toast_choose_img), Toast.LENGTH_SHORT).show()
                        }
                    } else{
                        Toast.makeText(this, getString(R.string.toast_missing_field), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (staffName.isNotEmpty() && staffWork.isNotEmpty() && staffGender.isNotEmpty() && staffAvatarChange != null) {
                        addStaff(staffAvatar, staffName, staffGender, staffWork, staffAvatarChange)
                    } else if (staffAvatarChange == null){
                        Toast.makeText(this, getString(R.string.toast_choose_img), Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, getString(R.string.toast_missing_field), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            binding.imageEditAvatar.setOnClickListener {
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, pickImage)
            }
        }catch (e:Exception){
            println("ERROR ----------------------------------------------${e.message}")
        }
    }

    @Deprecated("Deprecated in Java")
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
        startActivity(Intent(applicationContext, ListActivity::class.java))
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
        startActivity(Intent(applicationContext, ListActivity::class.java))
    }
}