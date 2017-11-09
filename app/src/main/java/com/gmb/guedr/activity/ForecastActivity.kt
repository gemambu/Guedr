package com.gmb.guedr.activity

import android.app.Fragment
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gmb.guedr.BuildConfig
import com.gmb.guedr.R
import com.gmb.guedr.fragment.CityListFragment
import com.gmb.guedr.model.City
import com.gmb.guedr.activity.CityPagerActivity
import com.gmb.guedr.fragment.CityPagerFragment


class ForecastActivity : AppCompatActivity(), CityListFragment.OnCitySelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        // chuleta para saber los detalles fijos del dispo
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        val dpWidth = (width / metrics.density).toInt()
        val dpHeight = (height / metrics.density).toInt()
        val model = Build.MODEL
        val androidVersion = Build.VERSION.SDK_INT
        val dpi = metrics.densityDpi

        // comprobamos que en la interfaz tenemos un FrameLayout llamado city_list_fragment
        if (findViewById<View>(R.id.city_list_fragment) != null){
            // comprobamos primero que no tenemos ya añadido el fragmen a nuestra jerarquia
            if (fragmentManager.findFragmentById(R.id.city_list_fragment) == null) {
                val fragment = CityListFragment.newInstance()
                fragmentManager.beginTransaction()
                        .add(R.id.city_list_fragment, fragment)
                        .commit()
            }
        }

        // hacemos lo mismo pero con el fragmen de City_pager_fragment
        if (findViewById<View>(R.id.fragment_city_pager) != null){
            // comprobamos primero que no tenemos ya añadido el fragmen a nuestra jerarquia
            if (fragmentManager.findFragmentById(R.id.fragment_city_pager) == null) {
                val fragment = CityPagerFragment.newInstance(0)
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_city_pager, fragment)
                        .commit()

            }
        }
    }

    override fun onCitySelected(city: City?, position: Int) {
        startActivity(CityPagerActivity.intent(this, position))
    }

}
