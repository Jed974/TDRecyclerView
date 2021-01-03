package com.example.tdrecyclerview.tasklist

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.util.*

// Le ViewModel met à jour la liste de com.example.tdrecyclerview.task qui est une LiveData
class TaskListViewModel(private var adapter: TaskListAdapter): ViewModel() {
    private val repository = TasksRepository()
    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> = _taskList
    //var adapter = TaskListAdapter()

    fun loadTasks(taskListFragment: TaskListFragment)
    {
        adapter = taskListFragment.adapter
        viewModelScope.launch{

            val fetchedTasks = repository.refresh()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            _taskList.value = fetchedTasks!!
            taskListFragment.infoChanged()

        }
    }
    fun deleteTask(task: Task)
    {
        viewModelScope.launch {
            repository.deleteTask(task.id)
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            val editableList = _taskList.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { task.id == it.id }
            editableList.removeAt(position)
            _taskList.value = editableList
            adapter.notifyDataSetChanged()
        }
    }
    fun addTask()
    {
        viewModelScope.launch {
            val task = Task(id = UUID.randomUUID().toString(), title = "New Task", description = "new com.example.tdrecyclerview.task", date= Date())
            var newTask: Task
            val fetchedTasks = repository.createTask(task)
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            newTask = fetchedTasks!!
            val editableList = _taskList.value.orEmpty().toMutableList()
            editableList.add(editableList.size, newTask)
            _taskList.value = editableList
            adapter.notifyDataSetChanged()
        }
    }
    fun addTask(task: Task)
    {
        viewModelScope.launch {

            var newTask: Task
            val fetchedTasks = repository.createTask(task)
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            newTask = fetchedTasks!!
            val editableList = _taskList.value.orEmpty().toMutableList()
            editableList.add(editableList.size, newTask)
            _taskList.value = editableList
            adapter.notifyDataSetChanged()
        }
    }

    fun editTask(task: Task)
    {
        viewModelScope.launch {

            var editedTask: Task
            val fetchedTasks = repository.updateTask(task)
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            editedTask = fetchedTasks!!
            val editableList = _taskList.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { task.id == it.id }
            editableList[position] = editedTask
            _taskList.value = editableList
            adapter.notifyDataSetChanged()
        }

    }
}