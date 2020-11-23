package com.example.tdrecyclerview.tasklist

data class Task(val id : String, val title : String, val description : String){
    override fun equals(other: Any?): Boolean {
        return  (other is Task)
                && other.id.equals(this.id);
    }
}