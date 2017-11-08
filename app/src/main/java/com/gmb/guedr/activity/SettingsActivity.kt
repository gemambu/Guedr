package com.gmb.guedr.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.RadioGroup
import com.gmb.guedr.R

class SettingsActivity : AppCompatActivity() {

    companion object {
        val EXTRA_UNITS = "EXTRA_UNITS"

        fun intent(context: Context, units: Int): Intent {
            val intent = Intent(context, SettingsActivity::class.java)
            intent.putExtra(EXTRA_UNITS, units)
            return intent
        }
    }

    val radioGroup by lazy { findViewById<RadioGroup>(R.id.units_rg) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        findViewById<View>(R.id.ok_btn).setOnClickListener { acceptSettings() }
        findViewById<View>(R.id.cancel_btn).setOnClickListener { cancelSettings() }

        val radioSelected = intent.getIntExtra(EXTRA_UNITS, R.id.celsius_rb)
        radioGroup.check(radioSelected)
    }

    private fun acceptSettings() {
        var intent = Intent()
        intent.putExtra(EXTRA_UNITS, radioGroup.checkedRadioButtonId)

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