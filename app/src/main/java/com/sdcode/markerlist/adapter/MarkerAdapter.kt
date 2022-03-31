package com.sdcode.markerlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdcode.markerlist.R
import com.sdcode.markerlist.adapter.MarkerAdapter.ReposViewHolder
import com.sdcode.markerlist.models.MarkerListModel

class MarkerAdapter(
    private val markers: List<MarkerListModel>,
    private val rowLayout: Int
) : RecyclerView.Adapter<ReposViewHolder>() {
    class ReposViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var markerDate: TextView = v.findViewById(R.id.markerDate)
        var markerName: TextView = v.findViewById(R.id.markerName)
        var markerLat: TextView = v.findViewById(R.id.markerLat)
        var markerLng: TextView = v.findViewById(R.id.markerLng)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(rowLayout, parent, false)
        return ReposViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
        holder.markerDate.text = markers[position].date
        holder.markerName.text = markers[position].name
        holder.markerLat.text = markers[position].latitude
        holder.markerLng.text = markers[position].longitude
    }

    override fun getItemCount(): Int {
        return markers.size
    }
}