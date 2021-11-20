package com.example.helloworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.helloworld.databinding.ActivityOfDetailsBinding


class ActivityOfDetails : AppCompatActivity() {
    private var city = ""
    private lateinit var binding: ActivityOfDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showData()
    }

    fun goToMain(view: View) {
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.putExtra("changeCity", city)
        startActivity(mainIntent)
    }

    fun showData() {
        val weather : ParcWeather? = intent.getParcelableExtra("weatherInfo")

        if (weather != null) {
            city = weather.address.toString()
            binding.city.text = "Город: " + weather.address
            binding.temperature.text = "Температура: " + weather.temp
            binding.minTemperature.text = "Минимальная температура: " + weather.tempMin
            binding.maxTemperature.text = "Максимальная температура: " + weather.tempMax
            binding.windSpeed.text = "Скорость ветра: " + weather.windSpeed + " м/с"
            binding.sunrise.text = "Рассвет: " + weather.sunrise
            binding.sunset.text = "Закат: " + weather.sunset
            binding.pressure.text = "Давление: " + weather.pressure
            binding.humidity.text = "Влажность: " + weather.humidity + " %"
        }

    }
}