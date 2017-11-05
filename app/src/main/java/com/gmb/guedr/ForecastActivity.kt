package com.gmb.guedr

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_forecast.*

class ForecastActivity : AppCompatActivity() {

    companion object {
        var REQUEST_UUNITS = 1
    }

    lateinit var maxTemp: TextView
    lateinit var minTemp: TextView

    var forecast: Forecast? = null
        set(value) {

            // asignamos el valor para que la primera vez no sea null
            field = value

            // accedemos a las vistas de la interfaz
            val forecastImage = findViewById<ImageView>(R.id.forecast_image)
            maxTemp = findViewById(R.id.max_temp)
            minTemp = findViewById(R.id.min_temp)
            val humidity = findViewById<TextView>(R.id.humidity)
            val description = findViewById<TextView>(R.id.forecast_description)

            value?.let {
                // actualizamos la vista con el modelo
                forecastImage.setImageResource(value.icon)
                forecast_description.text = value.description

                updateTemperature()

                val humidityString = getString(R.string.humidity_format, value.humidity)
                humidity.text = humidityString
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        forecast = Forecast(25f, 10f, 35f, "Soleado con alguna nube", R.drawable.ico_01)
    }

    // Este método define qué opciones de menu tenemos
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return true
    }

    // este metodo indica que se hace una vez que se ha pulsado una opcion del menu
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_show_settings) {
            // aqui sabemos que se ha pulsado mostrar ajustes
            val units = if (temperatureUnits() == Forecast.TempUnit.CELSIUS)
                R.id.celsius_rb
            else
                R.id.fahrenheit_rb

            val intent = SettingsActivity.intent(this, units)
            // esto lo haríamos si la segunda pantalla no nos tiene que devolver nada
            //startActivity(intent)

            // esto lo hariamos si la segunda pantala tiene que devolvernos valores
            startActivityForResult(intent, REQUEST_UUNITS)

            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_UUNITS) {
            if (resultCode == Activity.RESULT_OK) {
                val unitsSelected = data?.getIntExtra(SettingsActivity.EXTRA_UNITS, R.id.celsius_rb)
                when (unitsSelected) {
                    R.id.celsius_rb -> {
                        Log.v("TAG", "ForecastActivity: OK & Celsius")
                        //Toast.makeText(this, "Celsius seleccionado", Toast.LENGTH_LONG)
                          //      .show()
                    }
                    R.id.fahrenheit_rb -> {
                        Log.v("TAG", "ForecastActivity: OK & Fahrenheit")
                        //Toast.makeText(this, "Fahrenheit seleccionado", Toast.LENGTH_LONG)
                          //      .show()
                    }


                }

                val oldShowCelsius = temperatureUnits()

                PreferenceManager.getDefaultSharedPreferences(this)
                        .edit()
                        .putBoolean(PREFERENCE_SHOW_CELSIUS, unitsSelected == R.id.celsius_rb)
                        .apply()

                updateTemperature()

                Snackbar.make(findViewById<View>(android.R.id.content), "Han cambiado las preferencias", Snackbar.LENGTH_LONG)
                        .setAction("Deshacer") {
                            PreferenceManager.getDefaultSharedPreferences(this)
                                    .edit()
                                    .putBoolean(PREFERENCE_SHOW_CELSIUS, oldShowCelsius == Forecast.TempUnit.CELSIUS)
                                    .apply()
                            updateTemperature()
                        }
                        .show()


            }
        }
    }

    private fun updateTemperature() {
        val units = temperatureUnits()
        val unitsString = temperatureUnitsString(units)

        val maxTempString = getString(R.string.max_temp_format, forecast?.getMaxTemp(units), unitsString)
        val minTempString = getString(R.string.min_temp_format, forecast?.getMinTemp(units), unitsString)

        maxTemp.text = maxTempString
        minTemp.text = minTempString

    }

    private fun temperatureUnitsString(units: Forecast.TempUnit) = when (units) {
        Forecast.TempUnit.CELSIUS -> "ºC"
        else -> "F"
    }

    private fun temperatureUnits(): Forecast.TempUnit =
            if (PreferenceManager.getDefaultSharedPreferences(this)
                    .getBoolean(PREFERENCE_SHOW_CELSIUS, true)) {
                Forecast.TempUnit.CELSIUS
            } else {
                Forecast.TempUnit.FAHRENHEIT
            }

}
