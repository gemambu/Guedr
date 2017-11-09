package com.gmb.guedr.fragment

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.gmb.guedr.CONSTANT_OWM_APIKEY
import com.gmb.guedr.model.Forecast
import com.gmb.guedr.PREFERENCE_SHOW_CELSIUS
import com.gmb.guedr.R
import com.gmb.guedr.activity.SettingsActivity
import com.gmb.guedr.model.City
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.json.JSONObject
import org.w3c.dom.Text
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class ForecastFragment : Fragment() {

    companion object {
        var REQUEST_UUNITS = 1
        private val ARG_CITY = "ARG_CITY"

        fun newInstance(city: City): ForecastFragment {
            val fragment = ForecastFragment()

            val arguments = Bundle()
            arguments.putSerializable(ARG_CITY, city)
            fragment.arguments = arguments

            return fragment
        }
    }

    lateinit var root: View
    lateinit var maxTemp: TextView
    lateinit var minTemp: TextView

    var city: City? = null

        set(value) {
            field = value

            if (value != null) {
                root.findViewById<TextView>(R.id.city).text = value.name
                forecast = value.forecast
            }
        }

    var forecast: Forecast? = null
        set(value) {

            // asignamos el valor para que la primera vez no sea null
            field = value

            // accedemos a las vistas de la interfaz
            val forecastImage = root.findViewById<ImageView>(R.id.forecast_image)
            maxTemp = root.findViewById(R.id.max_temp)
            minTemp = root.findViewById(R.id.min_temp)
            val humidity = root.findViewById<TextView>(R.id.humidity)
            val forecastDescription = root.findViewById<TextView>(R.id.forecast_description)

            if (value != null) {
                // actualizamos la vista con el modelo
                forecastImage.setImageResource(value.icon)
                forecastDescription.text = value.description

                updateTemperature()

                val humidityString = getString(R.string.humidity_format, value.humidity)
                humidity.text = humidityString
            } else {
                updateForecast()
            }

        }

    private fun updateForecast() {
        async(UI){
            val newForecast: Deferred<Forecast?> = bg {
                downloadForecast(city)
            }

            forecast = newForecast.await()
        }


    }

    fun downloadForecast(city: City?): Forecast? {
        try {
            // descargamos la info del tiempo a saco
            val url = URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=${city?.name}&lang=sp&units=metric&appid=${CONSTANT_OWM_APIKEY}")
            val jsonString = Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next()

            val jsonRoot = JSONObject(jsonString.toString())
            val list = jsonRoot.getJSONArray("list")
            val today = list.getJSONObject(0)
            val max = today.getJSONObject("temp").getDouble("max").toFloat()
            val min = today.getJSONObject("temp").getDouble("min").toFloat()
            val humidity = today.getDouble("humidity").toFloat()
            val description = today.getJSONArray("weather").getJSONObject(0).getString("description")
            var iconString = today.getJSONArray("weather").getJSONObject(0).getString("icon")

            // convertimos el texto iconString a un drawable
            iconString = iconString.substring(0, iconString.length - 1)
            val iconInt = iconString.toInt()
            val iconResource = when (iconInt) {
                2 -> R.drawable.ico_02
                3 -> R.drawable.ico_03
                4 -> R.drawable.ico_04
                9 -> R.drawable.ico_09
                10 -> R.drawable.ico_10
                11 -> R.drawable.ico_11
                13 -> R.drawable.ico_13
                50 -> R.drawable.ico_50
                else -> R.drawable.ico_01
            }

            return Forecast(max, min, humidity, description, iconResource)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        inflater?.let {
            root = it.inflate(R.layout.fragment_forecast, container, false)
            if (arguments != null) {
                city = arguments.getSerializable(ARG_CITY) as City?
            }
        }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.settings, menu)
    }

    // este metodo indica que se hace una vez que se ha pulsado una opcion del menu
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_show_settings) {
            // aqui sabemos que se ha pulsado mostrar ajustes
            val units = if (temperatureUnits() == Forecast.TempUnit.CELSIUS)
                R.id.celsius_rb
            else
                R.id.fahrenheit_rb

            val intent = SettingsActivity.intent(activity, units)
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

                PreferenceManager.getDefaultSharedPreferences(activity)
                        .edit()
                        .putBoolean(PREFERENCE_SHOW_CELSIUS, unitsSelected == R.id.celsius_rb)
                        .apply()

                updateTemperature()

                Snackbar.make(root, "Han cambiado las preferencias", Snackbar.LENGTH_LONG)
                        .setAction("Deshacer") {
                            PreferenceManager.getDefaultSharedPreferences(activity)
                                    .edit()
                                    .putBoolean(PREFERENCE_SHOW_CELSIUS, oldShowCelsius == Forecast.TempUnit.CELSIUS)
                                    .apply()
                            updateTemperature()
                        }
                        .show()

            }
        }
    }

    // para saber si estando en un viewpager por ejemplo, debemos refrescar las unidades
    // de las temperaturas. Es algo asi como el viewWillAppear de los fragment
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && forecast != null) {
            updateTemperature()
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
            if (PreferenceManager.getDefaultSharedPreferences(activity)
                    .getBoolean(PREFERENCE_SHOW_CELSIUS, true)) {
                Forecast.TempUnit.CELSIUS
            } else {
                Forecast.TempUnit.FAHRENHEIT
            }
}