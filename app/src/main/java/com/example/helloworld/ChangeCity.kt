package com.example.helloworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.helloworld.databinding.ActivityChangeCityBinding

class ChangeCity : AppCompatActivity() {

    private var changableCity = ""
    private lateinit var binding: ActivityChangeCityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeCityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showData()
    }

    fun newCity(view: View) {
        val mainIntent = Intent(this, MainActivity::class.java)
        changableCity = binding.textEdit.text.toString()
        mainIntent.putExtra("changeCity", changableCity)
        startActivity(mainIntent)
    }

    fun showData() {
        changableCity = intent.getStringExtra("city").toString()
        binding.city.text = "Город: " + changableCity
    }

    fun goToMain(view: View) {
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.putExtra("changeCity", changableCity)
        startActivity(mainIntent)
    }
}