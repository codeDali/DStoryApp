package com.dicoding.picodiploma.loginwithanimation.view.maps

import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMapsBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.login.LoginViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.getStoriesLoc()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        try {
            val success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
            )
            if (!success) {
                Log.e("MapsActivity", "Failed to apply map style.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("MapsActivity", "Map style resource not found.", e)
        }

        viewModel.getStoriesLoc()
        viewModel.storiesLoc.observe(this) { response ->
            response.listStory?.forEach { listStory ->
                if (listStory != null) {
                    if (listStory.lat != null && listStory.lon != null) {
                        val latLng = LatLng(listStory.lat, listStory.lon)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(listStory.name)
                                .snippet(listStory.description)
                        )
                    } else {
                        Log.e("MapsActivity", "Story ${listStory.name} does not have valid location.")
                    }
                }
            }
            response.listStory?.firstOrNull()?.let {
                val location = LatLng(it.lat!!, it.lon!!)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 5f))
            }
            getMyLocation()
        }
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


}