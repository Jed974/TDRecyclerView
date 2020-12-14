package com.example.tdrecyclerview.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tdrecyclerview.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class TaskListAdapter() : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>()
{


    var taskList: List<Task> = emptyList()
    // Déclaration de la variable lambda dans l'adapter:
    var onDeleteClickListener: ((Task) -> Unit)? = null
    var onUpdateClickListener: ((Task) -> Unit)? = null


    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(taskTitle: String) {
            val apply = itemView.apply { // `apply {}` permet d'éviter de répéter `itemView.*`
                // TODO: afficher les données et attacher les listeners aux différentes vues de notre [itemView]
                var taskTextView = findViewById<TextInputEditText>(R.id.TaskInputText)
                taskTextView.setText(taskTitle)


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
        var deleteTaskButton = holder.itemView.findViewById<Button>(R.id.DeleteTaskButton)
        deleteTaskButton.setOnClickListener {
            onDeleteClickListener?.invoke(taskList[position])
        }
        var updateTaskButton = holder.itemView.findViewById<FloatingActionButton>(R.id.UpdateTaskButton)
        updateTaskButton.setOnClickListener {
            onUpdateClickListener?.invoke(taskList[position])
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}