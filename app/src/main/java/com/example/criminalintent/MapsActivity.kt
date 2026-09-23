package com.example.criminalintent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.criminalintent.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

private const val EXTRA_LATITUDE = "com.example.criminalintent.latitude"
private const val EXTRA_LONGITUDE = "com.example.criminalintent.longitude"
private const val EXTRA_TITLE = "com.example.criminalintent.title"

// Default location from the Week 10 slides (used only if no location is passed in)
private const val DEFAULT_LATITUDE = -26.717400
private const val DEFAULT_LONGITUDE = 153.062151
private const val DEFAULT_ZOOM = 15f

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Keep the map's controls and Google logo clear of the status and navigation bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // Maps load asynchronously; this callback runs once the map is ready to customise
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val latitude = intent.getDoubleExtra(EXTRA_LATITUDE, DEFAULT_LATITUDE)
        val longitude = intent.getDoubleExtra(EXTRA_LONGITUDE, DEFAULT_LONGITUDE)
        val title = intent.getStringExtra(EXTRA_TITLE)
            ?.ifBlank { null }
            ?: getString(R.string.crime_map_default_title)

        val crimeLocation = LatLng(latitude, longitude)
        map.addMarker(
            MarkerOptions()
                .position(crimeLocation)
                .title(title)
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(crimeLocation, DEFAULT_ZOOM))
    }

    companion object {
        fun newIntent(
            packageContext: Context,
            latitude: Double,
            longitude: Double,
            title: String
        ): Intent {
            return Intent(packageContext, MapsActivity::class.java).apply {
                putExtra(EXTRA_LATITUDE, latitude)
                putExtra(EXTRA_LONGITUDE, longitude)
                putExtra(EXTRA_TITLE, title)
            }
        }
    }
}
