package com.example.cookbookapp.model

data class Recipe(val id: String ?= null,
                  val description: String ?= null,
                  val extraInformation: String ?= null,
                  val ingredients: String ?= null,
                  val link: String ?= null,
                  val name: String ?= null,
                  val notes: String ?= null,
                  val photo: String ?= null,
                  val userID: String ?= null,
                  val status: String ?= null,
                  val tagList: ArrayList<Int> ?= null)
