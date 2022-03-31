package com.sdcode.markerlist.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdcode.markerlist.R
import com.sdcode.markerlist.adapter.MarkerAdapter
import com.sdcode.markerlist.models.MarkerListModel
import com.sdcode.markerlist.viewmodels.MarkersViewModel
import org.json.JSONObject


class MarkerListFragment : Fragment() {
    private var mRecyclerView: RecyclerView? = null
    private var progressDialog: ProgressDialog? = null
    private var myAdapter: RecyclerView.Adapter<MarkerAdapter.ReposViewHolder>? = null
    private val mMap: HashMap<String, ArrayList<MarkerListModel>> = HashMap()
    private val mMapMarkers: ArrayList<MarkerListModel> = ArrayList();

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_marker_list, container, false)
        initData();
        loadData()
        return view
    }

    private fun loadData() {
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
                    mRecyclerView = view?.findViewById(R.id.MarkerList)
                    mRecyclerView!!.layoutManager = LinearLayoutManager(context)
                    myAdapter = MarkerAdapter(mMapMarkers, R.layout.row_marker_details)
                    mRecyclerView!!.adapter = myAdapter
                    mMap[dateKeys.toString()] = mMapMarkers
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