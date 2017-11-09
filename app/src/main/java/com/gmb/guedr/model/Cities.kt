package com.gmb.guedr.model

import com.gmb.guedr.R
import java.io.Serializable

object Cities : Serializable{
    private var cities: List<City> = listOf(
            City("Madrid"),
            City("Jaén"),
            City("Quito")
    )

    val count
        get() = cities.size

    //fun getCity(index: Int) = cities[index]
    operator fun get(i: Int) = cities[i]

    fun toArray() = cities.toTypedArray()

}