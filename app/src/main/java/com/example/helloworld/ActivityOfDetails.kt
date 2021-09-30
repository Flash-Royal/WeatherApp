package com.example.helloworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import org.w3c.dom.Text


class ActivityOfDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_of_details)
        showData()
    }

    fun goToMain(view: View) {
        val mainIntent = Intent(this, MainActivity::class.java)

        startActivity(mainIntent)
    }

    fun showData() {
        findViewById<TextView>(R.id.city).text = "Город: " + intent.getStringExtra("address")
        findViewById<TextView>(R.id.temperature).text = "Температура: " + intent.getStringExtra("temp")
        findViewById<TextView>(R.id.minTemperature).text = "Минимальная температура: " + intent.getStringExtra("tempMin")
        findViewById<TextView>(R.id.maxTemperature).text = "Максимальная температура: " + intent.getStringExtra("tempMax")
        findViewById<TextView>(R.id.windSpeed).text = "Скорость ветра: " + intent.getStringExtra("windSpeed") + " м/с"
        findViewById<TextView>(R.id.sunrise).text = "Рассвет: " + intent.getStringExtra("sunrise")
        findViewById<TextView>(R.id.sunset).text = "Закат: " + intent.getStringExtra("sunset")
        findViewById<TextView>(R.id.pressure).text = "Давление: " + intent.getStringExtra("pressure")
        findViewById<TextView>(R.id.humidity).text = "Влажность: " + intent.getStringExtra("humidity") + " %"
    }
}