
package com.example.tenantease

data class message(
    val senderName: String,
    val messageText: String,
    val timestamp: String,
    val senderType: String  // "tenant", "landlord", or "property_manager"
)
