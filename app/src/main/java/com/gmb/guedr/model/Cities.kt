package com.gmb.guedr.model

import com.gmb.guedr.R
import java.io.Serializable

class Cities : Serializable{
    private var cities: List<City> = listOf(
            City("Madrid", Forecast(25f, 10f, 35f, "Soleado con alguna nube", R.drawable.ico_01)),
            City("Jaén", Forecast(36f, 12f, 19f, "Sol a tope", R.drawable.ico_01)),
            City("Quito", Forecast(30f, 15f, 40f, "Arcoiris", R.drawable.ico_10))
    )

    val count
        get() = cities.size

    //fun getCity(index: Int) = cities[index]
    operator fun get(i: Int) = cities[i]

    fun toArray() = cities.toTypedArray()

}