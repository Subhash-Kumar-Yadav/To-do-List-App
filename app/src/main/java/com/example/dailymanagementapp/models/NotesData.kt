package com.example.dailymanagementapp.models

data class NotesData(val data:String?="" ,  val noteId:String? = ""){
    constructor(): this("" , "")
}
