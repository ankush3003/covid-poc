package gulati.ankush.covidtracker.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import gulati.ankush.covidtracker.R
import gulati.ankush.covidtracker.databinding.ActivityMainBinding
import gulati.ankush.covidtracker.utils.AppConstants
import gulati.ankush.covidtracker.utils.GpsUtils
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.String


class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val mainActivityViewModel: MainActivityViewModel by viewModel()

    private var wayLatitude: Double = 0.0
    private  var wayLongitude: Double = 0.0
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var isGPS = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mainBinding.viewmodel = mainActivityViewModel
        mainBinding.lifecycleOwner = this
        mainBinding.executePendingBindings()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        initObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    fun initObservers() {
        mainActivityViewModel.fetchLocationEvent.observe(this, Observer {
            if(!checkPermissions()) {
                requestPermissions()
            } else {
                getLastLocation()
            }
        })

        mainActivityViewModel.locationResultLiveData.observe(this, Observer {
            val location: Location? = mainActivityViewModel.locationResultLiveData.value
            location?.let {
                //Toast.makeText(this, "Lat: " + it.latitude + " Long: " + it.longitude + " Accuracy: " +it.accuracy, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            AppConstants.LOCATION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out kotlin.String>,
        grantResults: IntArray
    ) {
        if (requestCode == AppConstants.LOCATION_REQUEST) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    location?.let {
                        mainActivityViewModel.updateLocation(it)
                    }
                    requestNewLocationData()
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5000
        mLocationRequest.fastestInterval = 3000
        //mLocationRequest.numUpdates = 1

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient?.let {
                it.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
                )
        }
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            //var mLastLocation: Location = locationResult.lastLocation
            mainActivityViewModel.updateLocation(locationResult.lastLocation)
            //Toast.makeText(, "Lat: " + location.latitude.toString() + " Long: " + location.longitude.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
