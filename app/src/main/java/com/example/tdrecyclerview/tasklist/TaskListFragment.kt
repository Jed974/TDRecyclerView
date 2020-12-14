package com.example.tdrecyclerview.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tdrecyclerview.R
import com.example.tdrecyclerview.network.Api
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskListFragment : Fragment()
{
    //var recyclerView = null;
    val taskList = emptyList<Task>()

    val adapter = TaskListAdapter()

    // On récupère une instance de ViewModel
    private val viewModel: TaskListViewModel = TaskListViewModel(adapter)


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
        viewModel.taskList.observe(viewLifecycleOwner) { newList ->
            // utliser la liste
            adapter.taskList = newList
            adapter.notifyDataSetChanged()
        }

        var recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        recyclerView.adapter = adapter

        var addTask = view.findViewById<FloatingActionButton>(R.id.addTaskButton);
        addTask.setOnClickListener {
            viewModel.addTask()
        }

        adapter.onDeleteClickListener = { task ->
            viewModel.deleteTask(task)
        }

        adapter.onUpdateClickListener = { task ->
            viewModel.editTask(task)
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.loadTasks(this)

        val imageView = view?.findViewById<ImageView>(R.id.AvatarImage)
        imageView?.load("https://goo.gl/gEgYUd")

    }

    suspend fun infoChanged()
    {
        val textView = view?.findViewById<TextView>(R.id.textViewUser)
        val userInfo = Api.userService.getInfo().body()
        textView?.text = userInfo.toString()
    }

}