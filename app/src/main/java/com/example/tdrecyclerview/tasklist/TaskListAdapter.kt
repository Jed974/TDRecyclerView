package com.example.tdrecyclerview.tasklist

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.tdrecyclerview.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class TaskListAdapter() : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>()
{


    var taskList: List<Task> = emptyList()
    // Déclaration de la variable lambda dans l'adapter:
    var onDeleteClickListener: ((Task) -> Unit)? = null
    var onUpdateClickListener: ((Task) -> Unit)? = null
    var onLongClickListener: ((Task) -> Boolean)? = null

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(task : Task) {
            val apply = itemView.apply { // `apply {}` permet d'éviter de répéter `itemView.*`
                var taskTextView = findViewById<TextView>(R.id.TitleTextView)
                taskTextView.text = task.title
                var taskDescriptionView = findViewById<TextView>(R.id.DescriptionTextView)
                taskDescriptionView.text = task.description
                var taskDateView = findViewById<TextView>(R.id.DateTextViewItem)
                if(task.date != null)
                    taskDateView.text = SimpleDateFormat("dd/MM/yyyy").format(task.date)
                else
                    taskDateView.text = "No date"
                var taskTimeView = findViewById<TextView>(R.id.TimeTextViewItem)
                if(task.date != null)
                    taskTimeView.text = SimpleDateFormat("HH:mm", Locale.FRENCH).format(task.date)
                else
                    taskTimeView.text = "No time"
                itemView.setOnLongClickListener {
                    onLongClickListener?.invoke(task) ?: false
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
        var deleteTaskButton = holder.itemView.findViewById<Button>(R.id.DeleteTaskButton)
        deleteTaskButton.setOnClickListener {
            onDeleteClickListener?.invoke(taskList[position])
        }
        var updateTaskButton = holder.itemView.findViewById<FloatingActionButton>(R.id.EditTaskButton)
        updateTaskButton.setOnClickListener {
            onUpdateClickListener?.invoke(taskList[position])
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}