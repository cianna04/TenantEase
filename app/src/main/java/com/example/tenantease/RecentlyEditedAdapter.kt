package com.example.tenantease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecentlyEditedAdapter(private val documents: List<String>) :
    RecyclerView.Adapter<RecentlyEditedAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDocumentName: TextView = itemView.findViewById(R.id.tvDocumentName)
        val ivDocumentIcon: ImageView = itemView.findViewById(R.id.ivDocumentIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recently_edited, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDocumentName.text = documents[position]
        holder.ivDocumentIcon.setImageResource(R.drawable.ic_folder)
    }

    override fun getItemCount(): Int = documents.size
}
