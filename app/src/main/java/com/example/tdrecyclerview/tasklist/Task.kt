package com.example.tdrecyclerview.tasklist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task (
        @SerialName("id")
        var id : String,
        @SerialName("title")
        var title : String,
        @SerialName("description")
        var description : String
)