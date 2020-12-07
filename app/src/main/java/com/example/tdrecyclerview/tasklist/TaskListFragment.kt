package com.example.tdrecyclerview.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tdrecyclerview.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.util.UUID;

class TaskListFragment : Fragment()
{
    //var recyclerView = null;
    private val taskList = mutableListOf(
        Task(id = UUID.randomUUID().toString(), title = "Jalik", description = "Codeur fatigué"),
        Task(id = UUID.randomUUID().toString(), title = "Jed", description = "Try harder des enfers")
        )

    var adapter = TaskListAdapter()
    private val tasksRepository = TasksRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        //return super.onCreateView(inflater, container, savedInstanceState)

        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        var recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        recyclerView.adapter = adapter

        var addTask = view.findViewById<FloatingActionButton>(R.id.addTaskButton);
        addTask.setOnClickListener {
            //taskList.add(Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}", description = "new task"))
            lifecycleScope.launch {
                val task = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}", description = "new task")
                tasksRepository.createTask(task)
                adapter.notifyDataSetChanged()
            }
        }

        var taskDeleteButton = recyclerView.findViewById<Button>(R.id.delete_button)

        adapter.onDeleteClickListener = { task ->
            lifecycleScope.launch {
                tasksRepository.deleteTask(task.id)
                adapter.notifyDataSetChanged()
            }
        }
        /*recyclerView.adapter.onDeleteClickListener = { task ->
            // Supprimer la tâche
            taskList.remove(task)
            adapter.notifyDataSetChanged()*/


        tasksRepository.taskList.observe(viewLifecycleOwner, Observer {
            adapter.taskList = it
            adapter.notifyDataSetChanged()
        })
    }



    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            tasksRepository.refresh()
        }
    }

}