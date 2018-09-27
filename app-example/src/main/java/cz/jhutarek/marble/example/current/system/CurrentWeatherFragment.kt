package cz.jhutarek.marble.example.current.system

import android.os.Bundle
import cz.jhutarek.marble.arch.mvvm.system.MarbleFragment
import cz.jhutarek.marble.example.current.presentation.CurrentWeatherViewModel
import cz.jhutarek.marble.example.main.system.MainApplication
import cz.jhutarek.marblearch.R

class CurrentWeatherFragment : MarbleFragment<CurrentWeatherViewModel, CurrentWeatherViewModel.State>() {

    override val layoutResId = R.layout.current__current_weather_fragment

    // TODO use Android Dagger, move to base class
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainApplication.getInjector(context).inject(this)
    }
}