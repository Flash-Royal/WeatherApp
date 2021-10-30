package com.example.helloworld

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    var city = "Москва"
    val api: String = "8118ed6ee68db2debfaaa5a44c832918"
    var url = "http://api.openweathermap.org/"
    lateinit var broadCastReceiver : BroadcastReceiver


    fun getWeatherInfo() {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherInt::class.java)
        val call = service.getCurrentWeatherData(city, api, "metric", "ru")
        call.enqueue(object : Callback<WeatherResp> {
            override fun onResponse(call: Call<WeatherResp>, response: Response<WeatherResp>) {
                if (response.isSuccessful) {
                    findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                    findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE

                    var weatherInfo = response.body()!!
                    var desc: String? = weatherInfo.weather.get(0).description
                    findViewById<TextView>(R.id.adress).text = weatherInfo.name
                    findViewById<TextView>(R.id.updated_at).text = "Обновлено: " + SimpleDateFormat("dd/MM/yyyy HH:mm ", Locale.ENGLISH).format(Date(weatherInfo.dt*1000))
                    findViewById<TextView>(R.id.status).text = desc!!.capitalize()
                    findViewById<TextView>(R.id.temp).text = weatherInfo.main?.temp.toString() + "°C"
                    findViewById<TextView>(R.id.temp_min).text = weatherInfo.main?.tempMin.toString() + "°C"
                    findViewById<TextView>(R.id.temp_max).text = weatherInfo.main?.tempMax.toString() + "°C"
                    findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(weatherInfo.sys?.sunrise!! * 1000))
                    findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(weatherInfo.sys?.sunset!! * 1000))
                    findViewById<TextView>(R.id.wind).text = weatherInfo.wind?.speed.toString()
                    findViewById<TextView>(R.id.pressure).text = weatherInfo.main?.pressure.toString()
                    findViewById<TextView>(R.id.humidity).text = weatherInfo.main?.humidity.toString()

                }
                else {
                    findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                    findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
                    findViewById<TextView>(R.id.errortext).visibility = View.VISIBLE
                    findViewById<Button>(R.id.errorbutton).visibility = View.VISIBLE
                    findViewById<Button>(R.id.errorbutton2).visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<WeatherResp>?, t: Throwable?) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
                findViewById<TextView>(R.id.errortext).visibility = View.VISIBLE
                findViewById<Button>(R.id.errorbutton).visibility = View.VISIBLE
                findViewById<Button>(R.id.errorbutton2).visibility = View.VISIBLE
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getCity()
        getWeatherInfo()

    }

    fun getPos(view: View){
        getCity()
        getWeatherInfo()
    }

    fun getCity() {
        var newCity = intent.getStringExtra("changeCity")
        Log.d("Info", newCity.toString())
        if (newCity != null) {
            city = newCity
        } else {
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
        }
    }

    fun goToChangeCity(view: View) {
        var cityIntent = Intent(this, ChangeCity::class.java)
        cityIntent.putExtra("city", city)
        startActivity(cityIntent)
    }

    fun goToDetails(view: View) {
        var detailsIntent = Intent(this, ActivityOfDetails::class.java)

        val address = findViewById<TextView>(R.id.adress).text
        val temp = findViewById<TextView>(R.id.temp).text
        val tempMin = findViewById<TextView>(R.id.temp_min).text
        val tempMax = findViewById<TextView>(R.id.temp_max).text
        val sunrise = findViewById<TextView>(R.id.sunrise).text
        val sunset = findViewById<TextView>(R.id.sunset).text
        val windSpeed = findViewById<TextView>(R.id.wind).text
        val pressure = findViewById<TextView>(R.id.pressure).text
        val humidity = findViewById<TextView>(R.id.humidity).text

        var weather = ParcWeather(address.toString(), temp.toString(), tempMin.toString(), tempMax.toString(), sunrise.toString(), sunset.toString(), windSpeed.toString(), pressure.toString(), humidity.toString())
        detailsIntent.putExtra("weatherInfo", weather)

        startActivity(detailsIntent)
    }


}