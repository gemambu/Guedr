package com.gmb.guedr.activity

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v13.app.FragmentPagerAdapter

import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.gmb.guedr.R
import com.gmb.guedr.fragment.CityPagerFragment


class CityPagerActivity : AppCompatActivity() {

    companion object {
        val EXTRA_CITY_INDEX = "EXTRA_CITY_INDEX"

        fun intent(context: Context, cityIndex: Int) : Intent {
            val intent = Intent(context, CityPagerActivity::class.java)
            intent.putExtra(EXTRA_CITY_INDEX, cityIndex)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_pager)

        // Configuramos la Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setLogo(R.mipmap.ic_launcher)
        setSupportActionBar(toolbar)

        // Recibimos el índice de la ciudad que queremos mostrar
        val cityIndex = intent.getIntExtra(EXTRA_CITY_INDEX, 0)

        if (fragmentManager.findFragmentById(R.id.fragment_city_pager) == null) {
            val fragment = CityPagerFragment.newInstance(cityIndex)
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_city_pager, fragment)
                    .commit()
        }


    }

}