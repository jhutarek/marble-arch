package cz.jhutarek.marble.example.current.system

import androidx.core.view.isVisible
import cz.jhutarek.marble.arch.mvvm.system.MarbleFragment
import cz.jhutarek.marble.example.current.presentation.CurrentWeatherViewModel
import cz.jhutarek.marble.example.main.system.MainApplication.Injector
import cz.jhutarek.marblearch.R
import io.reactivex.Observable
import kotlinx.android.synthetic.main.current__current_weather_fragment.*

class CurrentWeatherFragment : MarbleFragment<CurrentWeatherViewModel, CurrentWeatherViewModel.State>() {

    override val layoutResId = R.layout.current__current_weather_fragment

    override fun onInjection() {
        Injector.get(context).inject(this)
    }

    override fun onBindStates(states: Observable<CurrentWeatherViewModel.State>) = states.subscribeForViews {
        progressBar.isVisible = it.loadingVisible
        emptyMessage.isVisible = it.emptyVisible
        dataGroup.isVisible = it.dataVisible
        errorMessage.isVisible = it.errorVisible
        errorMessage.text = it.error
        icon.setImageResource(it.descriptionIcon)
        timestamp.text = it.timestamp
        location.text = it.location
        temperature.text = it.temperature
        pressure.text = it.pressure
        description.text = it.descriptionText
        additionalInfo.text = it.additionalInfo
    }
}