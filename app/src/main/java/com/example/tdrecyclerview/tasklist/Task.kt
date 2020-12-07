package com.example.tdrecyclerview.tasklist

import kotlinx.serialization.SerialName

data class Task(
        @SerialName("id")
        val id : String,
        @SerialName("title")
        val title : String,
        @SerialName("description")
        val description : String
)
{
    override fun equals(other: Any?): Boolean {
        return  (other is Task)
                && other.id.equals(this.id);
    }

}