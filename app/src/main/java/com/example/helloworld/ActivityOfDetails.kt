package com.example.helloworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        val weather : ParcWeather? = intent.getParcelableExtra("weatherInfo")

        if (weather != null) {
            findViewById<TextView>(R.id.city).text = "Город: " + weather.address
            findViewById<TextView>(R.id.temperature).text = "Температура: " + weather.temp
            findViewById<TextView>(R.id.minTemperature).text = "Минимальная температура: " + weather.tempMin
            findViewById<TextView>(R.id.maxTemperature).text = "Максимальная температура: " + weather.tempMax
            findViewById<TextView>(R.id.windSpeed).text = "Скорость ветра: " + weather.windSpeed + " м/с"
            findViewById<TextView>(R.id.sunrise).text = "Рассвет: " + weather.sunrise
            findViewById<TextView>(R.id.sunset).text = "Закат: " + weather.sunset
            findViewById<TextView>(R.id.pressure).text = "Давление: " + weather.pressure
            findViewById<TextView>(R.id.humidity).text = "Влажность: " + weather.humidity + " %"
        }

    }
}