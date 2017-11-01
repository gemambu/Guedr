package com.gmb.guedr

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val TAG = MainActivity::class.java.canonicalName
    var offlineWeatherImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.stone_button).setOnClickListener(this)
        findViewById<Button>(R.id.donkey_button).setOnClickListener(this)

        offlineWeatherImage = findViewById(R.id.offline_weather_image)


        Log.v(TAG, "he pasado por oncreate")

        if (savedInstanceState != null){
            Log.v(TAG, "savedInstranceState no es null: ${savedInstanceState["clave"]}")
        } else {
            Log.v(TAG, "savedInstranceState es null")
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.v(TAG, "he pasado por onSaveInstanceState")

        outState?.putString("clave", "valor")

    }

    override fun onClick(v: View?) {

//        when(v?.id){
//            R.id.stone_button -> Log.v(TAG, "han pulsado stone_button")
//            R.id.donkey_button -> Log.v(TAG, "han pulsado donkey_button")
//        }

        Log.v(TAG, when (v?.id) {
            R.id.stone_button -> "han pulsado stone_button"
            R.id.donkey_button -> "han pulsado donkey_button"
            else -> "no sé qué han pulsado"
        })

       // when (v?.id){
         //   R.id.stone_button -> offlineWeatherImage?.setImageResource(R.drawable.offline_weather)
           // R.id.donkey_button -> offlineWeatherImage?.setImageResource(R.drawable.offline_weather2)
        //}

        offlineWeatherImage?.setImageResource(when (v?.id) {
            R.id.stone_button -> R.drawable.offline_weather
            else -> R.drawable.offline_weather2
        })
    }
}
