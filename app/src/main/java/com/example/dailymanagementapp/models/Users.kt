package com.example.dailymanagementapp.models

data class Users(
    val name:String? = "",
    val email:String? = ""
){
    constructor():this("" , "")
}
