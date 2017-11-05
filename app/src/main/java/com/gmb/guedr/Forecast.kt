package com.gmb.guedr

data class Forecast(val maxTemp: Float,
               val minTemp: Float,
               val humidity: Float,
               val description: String,
               val icon: Int) {
    enum class TempUnit {
        CELSIUS,
        FAHRENHEIT
    }

    protected fun toFahrenheit(celsius: Float) = celsius * 1.8f + 31

    fun getMaxTemp(units: TempUnit) = when (units) {
        TempUnit.CELSIUS -> maxTemp
        TempUnit.FAHRENHEIT -> toFahrenheit(maxTemp)
    }

    fun getMinTemp(units: TempUnit) = when (units) {
        TempUnit.CELSIUS -> minTemp
        TempUnit.FAHRENHEIT -> toFahrenheit(minTemp)
    }
}