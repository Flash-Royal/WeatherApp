package com.example.helloworld

import android.os.Parcel
import android.os.Parcelable

class ParcWeather(address: String?, temp: String?, tempMin: String?, tempMax: String?, sunrise: String?, sunset: String?, windSpeed: String?, pressure: String?, humidity: String?) : Parcelable{
    var address = address
    var temp = temp
    var tempMin = tempMin
    var tempMax = tempMax
    var sunrise =sunrise
    var sunset = sunset
    var windSpeed = windSpeed
    var pressure = pressure
    var humidity = humidity

    constructor(parcel: Parcel) : this(
        address = parcel.readString(),
        temp = parcel.readString(),
        tempMin = parcel.readString(),
        tempMax = parcel.readString(),
        sunrise = parcel.readString(),
        sunset = parcel.readString(),
        windSpeed = parcel.readString(),
        pressure = parcel.readString(),
        humidity = parcel.readString()
    ) {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(address)
        parcel.writeString(temp)
        parcel.writeString(tempMin)
        parcel.writeString(tempMax)
        parcel.writeString(sunrise)
        parcel.writeString(sunset)
        parcel.writeString(windSpeed)
        parcel.writeString(pressure)
        parcel.writeString(humidity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcWeather> {
        override fun createFromParcel(parcel: Parcel): ParcWeather {
            return ParcWeather(parcel)
        }

        override fun newArray(size: Int): Array<ParcWeather?> {
            return arrayOfNulls(size)
        }
    }
}