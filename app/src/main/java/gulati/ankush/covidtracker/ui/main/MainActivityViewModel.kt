package gulati.ankush.covidtracker.ui.main

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationResult
import gulati.ankush.covidtracker.navigation.SingleLiveEvent

class MainActivityViewModel(/*private val dataRepository: IDataRepository*/) : ViewModel(){
    val fetchLocationEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    var locationResultLiveData: MutableLiveData<Location> = MutableLiveData<Location>()

    fun  getWeatherForecast() {

    }

    fun onFetchLocationBtnClicked() {
        fetchLocationEvent.call()
    }

    fun updateLocation(location: Location) {
        locationResultLiveData.postValue(location)
    }
}