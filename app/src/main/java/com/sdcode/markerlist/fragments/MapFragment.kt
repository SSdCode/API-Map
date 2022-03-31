package com.sdcode.markerlist.fragments

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sdcode.markerlist.R
import com.sdcode.markerlist.models.MarkerListModel
import com.sdcode.markerlist.viewmodels.MarkersViewModel
import org.json.JSONObject


class MapFragment : Fragment() {
    private var progressDialog: ProgressDialog? = null
    private val mMapMarkers: ArrayList<MarkerListModel> = ArrayList();

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.fragment_map, container, false)
        initData()
        val supportMapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        loadData(supportMapFragment)
        return v
    }

    private fun loadData(supportMapFragment: SupportMapFragment) {
        val viewModel = ViewModelProvider(this)[MarkersViewModel::class.java]
        viewModel.markers.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                val jsonObject = JSONObject(it.toString())
                val keys: Iterator<String> = jsonObject.keys() //response keys
                while (keys.hasNext()) {
                    val dynamicKey: String = keys.next()
                    val datesObject: JSONObject = jsonObject.getJSONObject(dynamicKey)
                    val dateKeys: Iterator<String> = datesObject.keys()

                    while (dateKeys.hasNext()) {
                        val dynamicDate: String = dateKeys.next()
                        val markerList: JSONObject = datesObject.getJSONObject(dynamicDate)

                        val mMarkerListModel =
                            MarkerListModel(
                                dynamicDate,
                                markerList.getString("name"),
                                markerList.getString("latitude"),
                                markerList.getString("longitude")
                            )
                        mMapMarkers.add(mMarkerListModel);
                    }
                    val height = 100
                    val width = 100
                    val bitmapDraw = resources.getDrawable(R.drawable.custom_icon) as BitmapDrawable
                    val b = bitmapDraw.bitmap
                    val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)

                    supportMapFragment.getMapAsync { googleMap: GoogleMap ->
                        for (i in 0 until mMapMarkers.size) {
                            val mMarkerListModel = mMapMarkers[i]
                            val latLng = LatLng(
                                mMarkerListModel.latitude.toDouble(),
                                mMarkerListModel.longitude.toDouble()
                            )
                            var title = ""
                            title += "Date - " + mMarkerListModel.date + "   "
                            title += "Name - " + mMarkerListModel.name
                            val markerOptions =
                                MarkerOptions().position(latLng)
                                    .title(title)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
//                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_smiley))
                            googleMap.addMarker(markerOptions)
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                        }
                    }
                    progressDialog!!.dismiss()
                }

            } else {
                progressDialog!!.dismiss()
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.makeApiCall()
    }

    private fun initData() {
        progressDialog = ProgressDialog(context)
        progressDialog!!.setMessage("Loading...") // Setting Message
        progressDialog!!.setTitle("ProgressDialog") // Setting Title
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER) // Progress Dialog Style Spinner
        progressDialog!!.show() // Display Progress Dialog
        progressDialog!!.setCancelable(false)
        Thread {
            try {
                Thread.sleep(10000000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            progressDialog!!.dismiss()
        }.start()
    }
}