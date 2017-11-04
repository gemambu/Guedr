package com.gmb.guedr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.RadioGroup

class SettingsActivity : AppCompatActivity() {

    companion object {
        val EXTRA_UNITS = "EXTRA_UNITS"

//        fun intent(context: Context): Intent {
//            return Intent(context, SettingsActivity::class.java)
//        }

        fun intent(context: Context) = Intent(context, SettingsActivity::class.java)
    }

    var radioGroup: RadioGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

       /* Esto seria un equivalente a una clase anonima en kotlin para los javeros
       findViewById<View>(R.id.ok_btn).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // aqui iria el codigo de aceptar
                acceptSettings()

            }
        })*/

        // esto es una closure indicadon que recibe V (una view) y no devuelve nada
        /* findViewById<View>(R.id.ok_btn).setOnClickListener { v ->
            acceptSettings()
        }*/

        findViewById<View>(R.id.ok_btn).setOnClickListener { acceptSettings() }
        findViewById<View>(R.id.cancel_btn).setOnClickListener { cancelSettings() }

        radioGroup = findViewById(R.id.units_rg)
    }

    private fun acceptSettings() {
        var intent = SettingsActivity.intent(this)
        intent.putExtra(EXTRA_UNITS, radioGroup?.checkedRadioButtonId)

        setResult(Activity.RESULT_OK, intent)
        // finalizamos esta actividad regresando a la anterior
        finish()
    }

    private fun cancelSettings() {
        // finalizamos esta actividad regresando a la anterior
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}