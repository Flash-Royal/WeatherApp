package com.example.helloworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class ChangeCity : AppCompatActivity() {

    var changableCity = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_city)
        showData()
    }

    fun newCity(view: View) {
        val mainIntent = Intent(this, MainActivity::class.java)
        changableCity = findViewById<EditText>(R.id.textEdit).text.toString()
        mainIntent.putExtra("changeCity", changableCity)
        startActivity(mainIntent)
    }

    fun showData() {
        changableCity = intent.getStringExtra("city").toString()
        findViewById<TextView>(R.id.city).text = "Город: " + changableCity
    }

    fun goToMain(view: View) {
        val mainIntent = Intent(this, MainActivity::class.java)

        startActivity(mainIntent)
    }
}