package com.example.tdrecyclerview.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tdrecyclerview.R

class TaskListAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>()
{
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(taskTitle: String) {
            itemView.apply { // `apply {}` permet d'éviter de répéter `itemView.*`
                // TODO: afficher les données et attacher les listeners aux différentes vues de notre [itemView]
                var taskTextView = findViewById<TextView>(R.id.task_title)
                taskTextView.text = taskTitle
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position].title + " ("+taskList[position].description+")")
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}