package com.example.cookbookapp.model

import java.util.*

data class Users(val id: String, val email: String, val firstName: String, val lastName: String, val password: String, val registerDate: Date, val status: String)
