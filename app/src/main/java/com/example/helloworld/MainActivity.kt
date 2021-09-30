package com.example.helloworld

import android.Manifest
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
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val PERMISSION_ID = 1010



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getCity()

        weatherTask().execute()
    }

    fun CheckPermission():Boolean{
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false

    }

    fun getCity() {
        var newCity = getChangeCity()
        Log.d("Info", newCity.toString())
        if (newCity != null) {
            city = newCity
        }
        else {
            getLastLocation()
        }
    }

    fun getPos(view: View){
        RequestPermission()
        getLastLocation()
        weatherTask().execute()
    }

    fun RequestPermission(){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    fun isLocationEnabled():Boolean{
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    fun getLastLocation(){
        if(CheckPermission()){
            if(isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task->
                    var location: Location? = task.result
                    if(location != null){
                        var latitude = location.latitude
                        var longitude = location.longitude
                        city = getCityName(latitude,longitude)
                    }
                }
            }
        }else{
            RequestPermission()
        }
    }


    fun getChangeCity(): String? {
        return intent.getStringExtra("changeCity")
    }

    private fun getCityName(lat: Double,long: Double):String{
        var cityName:String = ""
        var countryName = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var address = geoCoder.getFromLocation(lat,long,3)

        cityName = address.get(0).locality
        countryName = address.get(0).countryName
        return cityName
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

        detailsIntent.putExtra("address", address)
        detailsIntent.putExtra("temp", temp)
        detailsIntent.putExtra("tempMin", tempMin)
        detailsIntent.putExtra("tempMax", tempMax)
        detailsIntent.putExtra("sunrise", sunrise)
        detailsIntent.putExtra("sunset", sunset)
        detailsIntent.putExtra("windSpeed", windSpeed)
        detailsIntent.putExtra("pressure", pressure)
        detailsIntent.putExtra("humidity", humidity)
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