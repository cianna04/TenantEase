package com.example.tenantease

data class User(
    val firstName: String,
    val middleName: String?,
    val lastName: String,
    val unitNo: String?,
    val contactNumber: String,
    val email: String,
    val username: String,
    val role: String
)