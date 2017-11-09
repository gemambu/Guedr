package com.gmb.guedr.model

import java.io.Serializable


data class City(var name: String, var forecast: Forecast?) : Serializable {

    constructor(name: String) : this(name, null)

    override fun toString() = name

}