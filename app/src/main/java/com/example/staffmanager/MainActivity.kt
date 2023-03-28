package com.example.staffmanager

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.staffmanager.databinding.ActivityMainBinding
import com.example.staffmanager.feature.ListActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolBarMain
        setSupportActionBar(toolbar)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.listStaff ->{
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
            return true
            }
            R.id.exit ->{
                finish()
            return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}