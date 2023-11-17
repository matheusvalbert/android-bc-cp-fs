package com.matheusvalbert.app1

import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.matheusvalbert.app1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val uri = Uri.parse("content://com.matheusvalbert.app1.mycp/test_table/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        saveAction()
        loadAction()
    }

    private fun saveAction() {
        binding.save.setOnClickListener {

            val values = ContentValues()
            values.put("title", binding.title.text.toString())
            values.put("description", binding.description.text.toString())

            val uri = contentResolver.insert(uri, values)

            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadAction() {
        binding.load.setOnClickListener {
            val cr = contentResolver.query(uri, null, null, null, "_id")
            var data = ""

            while (cr?.moveToNext() == true) {
                data += "${cr.getString(1)} | ${cr.getString(2)}\n"
            }
            cr?.close()

            println(data)
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
        }
    }
}