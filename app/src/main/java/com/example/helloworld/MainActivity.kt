package com.example.helloworld

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.helloworld.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var city = "Москва"
    private val api: String = "8118ed6ee68db2debfaaa5a44c832918"
    private var url = "http://api.openweathermap.org/"
    private lateinit var broadCastReceiver : BroadcastReceiver
    private lateinit var retrofit: Retrofit
    private lateinit var service: WeatherInt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getCity()
        getWeatherInfo()
    }

    fun getWeatherInfo() {
        retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(WeatherInt::class.java)
        val call = service.getCurrentWeatherData(city, api, "metric", "ru")
        call.enqueue(object : Callback<WeatherResp> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<WeatherResp>, response: Response<WeatherResp>) {
                if (response.isSuccessful) {
                    binding.loader.visibility = View.GONE
                    binding.mainContainer.visibility = View.VISIBLE

                    val weatherInfo = response.body()!!
                    val desc: String? = weatherInfo.weather.get(0).description
                    city = weatherInfo.name.toString()
                    binding.adress.text = weatherInfo.name
                    binding.updatedAt.text = "Обновлено: " + SimpleDateFormat("dd/MM/yyyy HH:mm ", Locale.ENGLISH).format(Date(weatherInfo.dt*1000))
                    binding.status.text = desc!!.capitalize()
                    binding.temp.text = weatherInfo.main?.temp.toString() + "°C"
                    binding.tempMin.text = weatherInfo.main?.tempMin.toString() + "°C"
                    binding.tempMax.text = weatherInfo.main?.tempMax.toString() + "°C"
                    binding.sunrise.text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(weatherInfo.sys?.sunrise!! * 1000))
                    binding.sunset.text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(weatherInfo.sys?.sunset!! * 1000))
                    binding.wind.text = weatherInfo.wind?.speed.toString()
                    binding.pressure.text = weatherInfo.main?.pressure.toString()
                    binding.humidity.text = weatherInfo.main?.humidity.toString()

                }
                else {
                    binding.loader.visibility = View.GONE
                    binding.mainContainer.visibility = View.GONE
                    binding.errortext.visibility = View.VISIBLE
                    binding.errorbutton.visibility = View.VISIBLE
                    binding.errorbutton2.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<WeatherResp>?, t: Throwable?) {
                binding.loader.visibility = View.GONE
                binding.mainContainer.visibility = View.GONE
                binding.errortext.visibility = View.VISIBLE
                binding.errorbutton.visibility = View.VISIBLE
                binding.errorbutton2.visibility = View.VISIBLE
            }
        })
    }

    fun getPos(view: View){
        changeCity()
        getWeatherInfo()
    }

    fun getCity() {
        var newCity = intent.getStringExtra("changeCity")
        if (newCity != null) {
            city = newCity
        }
    }

    fun changeCity() {
        val intent = Intent(this, LocationService::class.java);
        startService(intent)
        broadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, intent: Intent?) {
                var data = intent?.getStringExtra("cityName")

                if (data != null) {
                    city = data
                }
            }
        }
        val filter = IntentFilter(MainActivity::class.java.name)
        registerReceiver(broadCastReceiver, filter)
        Log.d("Info", city)
    }

    fun goToChangeCity(view: View) {
        var cityIntent = Intent(this, ChangeCity::class.java)
        cityIntent.putExtra("city", city)
        startActivity(cityIntent)
    }

    fun goToDetails(view: View) {
        var detailsIntent = Intent(this, ActivityOfDetails::class.java)

        val address = binding.adress.text
        val temp = binding.temp.text
        val tempMin = binding.tempMin.text
        val tempMax = binding.tempMax.text
        val sunrise = binding.sunrise.text
        val sunset = binding.sunset.text
        val windSpeed = binding.wind.text
        val pressure = binding.pressure.text
        val humidity = binding.humidity.text

        var weather = ParcWeather(address.toString(), temp.toString(), tempMin.toString(), tempMax.toString(), sunrise.toString(), sunset.toString(), windSpeed.toString(), pressure.toString(), humidity.toString())
        detailsIntent.putExtra("weatherInfo", weather)

        startActivity(detailsIntent)
    }


}