package gulati.ankush.covidtracker.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import gulati.ankush.covidtracker.R
import gulati.ankush.covidtracker.databinding.ActivityMainBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val mainActivityViewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mainBinding.viewmodel = mainActivityViewModel
        mainBinding.lifecycleOwner = this
        mainBinding.executePendingBindings()

        mainActivityViewModel.getWeatherForecast()
    }

    override fun onResume() {
        super.onResume()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }
}
