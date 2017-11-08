package com.gmb.guedr.activity

import android.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gmb.guedr.R
import com.gmb.guedr.fragment.CityListFragment
import com.gmb.guedr.model.Cities
import com.gmb.guedr.model.City


class ForecastActivity : AppCompatActivity(), CityListFragment.OnCitySelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        // comprobamos primero que no tenemos ya añadido el fragmen a nuestra jerarquia
        if (fragmentManager.findFragmentById(R.id.city_list_fragment) == null) {
            val fragment = CityListFragment.newInstance(Cities())
            fragmentManager.beginTransaction()
                    .add(R.id.city_list_fragment, fragment)
                    .commit()
        }

    }

    override fun onCitySelected(city: City?, position: Int) {
        startActivity(CityPagerActivity.intent(this, position))
    }




}
