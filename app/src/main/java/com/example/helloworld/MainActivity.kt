package com.example.helloworld

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import com.example.helloworld.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var city = "Москва"
    private val api: String = "8118ed6ee68db2debfaaa5a44c832918"
    private var url = "https://api.openweathermap.org/"
    private lateinit var retrofitService: WeatherInt
    private var myService: LocationService? = null
    private var isBound = false

    private val myConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as LocationService.LocalBinder
            myService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getCity()
        getWeatherInfo()
    }

    fun getWeatherInfo() {
        retrofitService = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(WeatherInt::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofitService.getCurrentWeatherData(city, api, "metric", "ru")
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    binding.mainContainer.visibility = View.VISIBLE
                    binding.errortext.visibility = View.GONE
                    binding.errorbutton.visibility = View.GONE
                    binding.errorbutton2.visibility = View.GONE

                    val weatherInfo = response.body()!!
                    val desc: String? = weatherInfo.weather.get(0).description
                    city = weatherInfo.name.toString()
                    binding.adress.text = weatherInfo.name
                    binding.updatedAt.text = "Обновлено: " + SimpleDateFormat("dd/MM/yyyy HH:mm ", Locale.ENGLISH).format(Date(weatherInfo.dt * 1000))
                    binding.status.text = desc!!.capitalize()
                    binding.temp.text = weatherInfo.main?.temp.toString() + "°C"
                    binding.tempMin.text = weatherInfo.main?.tempMin.toString() + "°C"
                    binding.tempMax.text = weatherInfo.main?.tempMax.toString() + "°C"
                    binding.sunrise.text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(weatherInfo.sys?.sunrise!! * 1000))
                    binding.sunset.text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(weatherInfo.sys?.sunset!! * 1000))
                    binding.wind.text = weatherInfo.wind?.speed.toString()
                    binding.pressure.text = weatherInfo.main?.pressure.toString()
                    binding.humidity.text = weatherInfo.main?.humidity.toString()

                } else {
                    binding.mainContainer.visibility = View.GONE
                    binding.errortext.visibility = View.VISIBLE
                    binding.errorbutton.visibility = View.VISIBLE
                    binding.errorbutton2.visibility = View.VISIBLE
                }
            }
        }
    }

    fun getPos(view: View){
        changeCity()
        getWeatherInfo()
    }

    fun getCity() {
        val newCity = intent.getStringExtra("changeCity")
        if (newCity != null) {
            city = newCity
        }
    }

    override fun onStart() {
        super.onStart()

        val intent = Intent(this, LocationService::class.java)
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(myConnection)
    }

    fun changeCity() {
        if (isBound) {
            city = myService?.getCity().toString()
        }
    }

    fun goToChangeCity(view: View) {
        val cityIntent = Intent(this, ChangeCity::class.java)
        cityIntent.putExtra("city", city)
        startActivity(cityIntent)
    }

    fun goToDetails(view: View) {
        val detailsIntent = Intent(this, ActivityOfDetails::class.java)

        val address = binding.adress.text
        val temp = binding.temp.text
        val tempMin = binding.tempMin.text
        val tempMax = binding.tempMax.text
        val sunrise = binding.sunrise.text
        val sunset = binding.sunset.text
        val windSpeed = binding.wind.text
        val pressure = binding.pressure.text
        val humidity = binding.humidity.text

        val weather = ParcWeather(address.toString(), temp.toString(), tempMin.toString(), tempMax.toString(), sunrise.toString(), sunset.toString(), windSpeed.toString(), pressure.toString(), humidity.toString())
        detailsIntent.putExtra("weatherInfo", weather)

        startActivity(detailsIntent)
    }


}