// ChatAdapter.kt
package com.example.tenantease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class chatadapter(private val messages: List<message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_TENANT = 0
        const val TYPE_LANDLORD = 1
        const val TYPE_PROPERTY_MANAGER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (messages[position].senderType) {
            "tenant" -> TYPE_TENANT
            "landlord" -> TYPE_LANDLORD
            "property_manager" -> TYPE_PROPERTY_MANAGER
            else -> throw IllegalArgumentException("Invalid sender type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_TENANT -> TenantViewHolder(inflater.inflate(R.layout.chat_tenant, parent, false))
            TYPE_LANDLORD -> LandlordViewHolder(inflater.inflate(R.layout.chat_landlord, parent, false))
            TYPE_PROPERTY_MANAGER -> PropertyManagerViewHolder(inflater.inflate(R.layout.chat_propertymanager, parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
                            }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is TenantViewHolder -> holder.bind(message)
            is LandlordViewHolder -> holder.bind(message)
            is PropertyManagerViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    class TenantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val senderName: TextView = itemView.findViewById(R.id.sender_name)
        private val messageText: TextView = itemView.findViewById(R.id.message_text)
        private val timestampText: TextView = itemView.findViewById(R.id.timestamp_text)

        fun bind(message: message) {
            senderName.text = message.senderName
            messageText.text = message.messageText
            timestampText.text = message.timestamp
        }
    }

    class LandlordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val senderName: TextView = itemView.findViewById(R.id.sender_name)
        private val messageText: TextView = itemView.findViewById(R.id.message_text)
        private val timestampText: TextView = itemView.findViewById(R.id.timestamp_text)

        fun bind(message: message) {
            senderName.text = message.senderName
            messageText.text = message.messageText
            timestampText.text = message.timestamp
        }
    }

    class PropertyManagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val senderName: TextView = itemView.findViewById(R.id.sender_name)
        private val messageText: TextView = itemView.findViewById(R.id.message_text)
        private val timestampText: TextView = itemView.findViewById(R.id.timestamp_text)

        fun bind(message: message) {
            senderName.text = message.senderName
            messageText.text = message.messageText
            timestampText.text = message.timestamp
        }
    }
}
