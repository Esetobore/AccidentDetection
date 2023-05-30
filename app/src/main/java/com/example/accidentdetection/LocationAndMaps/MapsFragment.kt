package com.example.accidentdetection.LocationAndMaps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.accidentdetection.R
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

@Suppress("DEPRECATION")
class MapsFragment : Fragment() ,LocationListener,OnMapReadyCallback,LocationSource.OnLocationChangedListener{

    lateinit var userLocationMarker :Marker




    override fun onMapReady(googleMap: GoogleMap) {

//        googleMap.isMyLocationEnabled
        val lt = Vals.lati
        val lg = Vals.longi
        val techno = LatLng(lt, lg)
        val markerOptions = MarkerOptions().position(techno).title("Me")
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ac))
        userLocationMarker = googleMap.addMarker(markerOptions)!!
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(techno, 17f))
        startCurrentLocationUpdates(googleMap)


    }


    private fun startCurrentLocationUpdates(googleMap: GoogleMap){
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        //gets location every 4 minutes
        val locationRequest = com.google.android.gms.location.LocationRequest().setInterval(2000).setFastestInterval(2000)
                .setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)


        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        for (location in locationResult.locations) {
                            val loc = locationResult.lastLocation
                            val latlng = LatLng(loc.latitude,loc.longitude)



                            if(userLocationMarker == null){
                                 //create new marker
                                val markerOptions = MarkerOptions().position(latlng).title("Me")
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ac))
                                markerOptions.rotation(location.bearing)
                                markerOptions.anchorU
                                markerOptions.anchorV
                                userLocationMarker = googleMap.addMarker(markerOptions)!!
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,17f))
                            }
                            else{
                                //use previously created marker
                                userLocationMarker.position=latlng
                                userLocationMarker.rotation=location.bearing
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,17f))
                            }

                        }
                        // Few more things we can do here:
                        // For example: Update the location of user on server
                    }
                },
                Looper.myLooper()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }



    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.onCreate(savedInstanceState)
        mapFragment?.onResume()
        mapFragment?.getMapAsync(this)
    }


    override fun onLocationChanged(location: Location) {
        location.latitude=Vals.lati
        location.longitude=Vals.longi
    }





}