package com.example.helloworld

import com.google.gson.annotations.SerializedName

class WeatherResp {
    @SerializedName("sys")
    var sys: Sys? = null
    @SerializedName("weather")
    var weather = ArrayList<Weather>()
    @SerializedName("main")
    var main: Main? = null
    @SerializedName("wind")
    var wind: Wind? = null
    @SerializedName("dt")
    var dt: Long = 0.toLong()
    @SerializedName("name")
    var name: String? = null
}

class Main {
    @SerializedName("temp")
    var temp: Float = 0f
    @SerializedName("temp_min")
    var tempMin: Float = 0f
    @SerializedName("temp_max")
    var tempMax: Float = 0f
    @SerializedName("humidity")
    var humidity: Float = 0f
    @SerializedName("pressure")
    var pressure: Float = 0f
}

class Sys {
    @SerializedName("sunrise")
    var sunrise: Long = 0.toLong()
    @SerializedName("sunset")
    var sunset: Long = 0.toLong()
}

class Weather {
    @SerializedName("description")
    var description: String? = null
}

class Wind {
    @SerializedName("speed")
    var speed: Float = 0f
}




