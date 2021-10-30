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
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    var city = "Москва"
    val api: String = "8118ed6ee68db2debfaaa5a44c832918"
    lateinit var broadCastReceiver : BroadcastReceiver



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherTask().execute()
        getCity()
    }

    fun getPos(view: View){
        weatherTask().execute()
    }




    fun getChangeCity(): String? {
        return intent.getStringExtra("changeCity")
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



    inner class weatherTask() : AsyncTask<String, Void, String>()
    {


        override fun onPreExecute(){
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errortext).visibility = View.GONE
            findViewById<Button>(R.id.errorbutton).visibility = View.GONE
            findViewById<Button>(R.id.errorbutton2).visibility = View.GONE
        }

        override fun doInBackground(vararg p0: String?) : String? {
            var response: String?
            try {

                    response = URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$api")
                        .readText(Charsets.UTF_8)
            }
            catch (e: Exception) {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt: Long = jsonObj.getLong("dt")
                val updateAtText = "Обновлено: " + SimpleDateFormat("dd/MM/yyyy HH:mm ", Locale.ENGLISH).format(Date(updatedAt*1000))
                val temp = main.getString("temp") + "°C"
                val tempMin = main.getString("temp_min") + "°C"
                val tempMax = main.getString("temp_max") + "°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name") + "," + sys.getString("country")



                findViewById<TextView>(R.id.adress).text = city
                findViewById<TextView>(R.id.updated_at).text = updateAtText
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_min).text = tempMin
                findViewById<TextView>(R.id.temp_max).text = tempMax
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity


                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE

            }
            catch(e:Exception){
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errortext).visibility = View.VISIBLE
                findViewById<Button>(R.id.errorbutton).visibility = View.VISIBLE
                findViewById<Button>(R.id.errorbutton2).visibility = View.VISIBLE
            }
        }
    }


}