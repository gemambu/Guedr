package com.gmb.guedr.fragment


import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

import com.gmb.guedr.R
import com.gmb.guedr.model.Cities
import com.gmb.guedr.model.City


class CityListFragment : Fragment() {


    companion object {

        private val ARG_CITIES = "ARG_CITIES"

        fun newInstance(cities: Cities): CityListFragment {
            val fragment = CityListFragment()
            val args = Bundle()
            args.putSerializable(ARG_CITIES, cities)
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var root: View
    private var cities: Cities? = null
    private var onCitySelectedListener: OnCitySelectedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            cities = arguments?.getSerializable(ARG_CITIES) as? Cities
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (inflater != null){
            root = inflater?.inflate(R.layout.fragment_city_list, container, false)
            val list = root.findViewById<ListView>(R.id.city_list)
            val adapter = ArrayAdapter<City>(activity, android.R.layout.simple_list_item_1, cities?.toArray())
            list.adapter = adapter

            //nos enteramos de que se ha pulsado un elemento de la lista
            list.setOnItemClickListener{ parent, view, position, id ->
                // notifico al listener
                onCitySelectedListener?.onCitySelected(cities?.get(position), position)
            }
        }

        return root

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        commonAttach(context)
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        commonAttach(activity)
    }

    override fun onDetach() {
        super.onDetach()
        onCitySelectedListener = null
    }

    fun commonAttach(listener: Any?){
        if (listener is OnCitySelectedListener) {
            onCitySelectedListener = listener
        }
    }

    interface OnCitySelectedListener {
        fun onCitySelected(city: City?, position: Int)
    }

}
