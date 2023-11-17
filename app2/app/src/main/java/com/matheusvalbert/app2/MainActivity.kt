package com.matheusvalbert.app2

import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.matheusvalbert.app2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val uri = Uri.parse("content://com.matheusvalbert.app1.mycp/test_table/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissions(arrayOf("com.matheusvalbert.app1.mycp.RW"), 9999)

        Util.startServiceIfNeeded(this)
        updateData()
        deleteData()
    }

    private fun updateData() {
        binding.update.setOnClickListener {

            val values = ContentValues()
            values.put("title", binding.title.text.toString())
            values.put("description", binding.description.text.toString())

            contentResolver.update(uri, values, null, arrayOf(binding.idUpdate.text.toString()))

            Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteData() {
        binding.delete.setOnClickListener {
            contentResolver.delete(uri, null, arrayOf(binding.idDelete.text.toString()))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 9999) {
            Util.startServiceIfNeeded(this)
        }
    }
}