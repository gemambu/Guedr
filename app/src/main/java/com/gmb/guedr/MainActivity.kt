package com.gmb.guedr

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    val TAG = MainActivity::class.java.canonicalName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
}
