package com.sdcode.markerlist.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.sdcode.markerlist.retrofit.APIService.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarkersViewModel : ViewModel() {
    var markers: MutableLiveData<JsonObject?> = MutableLiveData()
    fun makeApiCall() {
        val call = service!!.markerList
        call!!.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                markers.value = response.body()
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                markers.value = null
            }
        })
    }

}