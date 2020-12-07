package com.example.tdrecyclerview.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tdrecyclerview.network.Api

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableLiveData<List<Task>>()

    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: LiveData<List<Task>> = _taskList

    suspend fun refresh() {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            _taskList.value = fetchedTasks!!
        }
    }

    suspend fun updateTask(task: Task) {
        val tasksResponse = tasksWebService.updateTask(task)
        var editedTask = Task("", "", "")
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            editedTask = fetchedTasks!!
        }
        val editableList = _taskList.value.orEmpty().toMutableList()
        val position = editableList.indexOfFirst { task.id == it.id }
        editableList[position] = editedTask
        _taskList.value = editableList
    }

    suspend fun createTask(task: Task) {
        val tasksResponse = tasksWebService.createTask(task)
        var newTask = Task("", "", "")
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            newTask = fetchedTasks!!
            val editableList = _taskList.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { task.id == it.id }
            editableList.add(position, newTask)
            _taskList.value = editableList
        }
    }

    suspend fun deleteTask(id: String) {
        val tasksResponse = tasksWebService.deleteTask(id)
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            val editableList = _taskList.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { id == it.id }
            editableList.removeAt(position)
            _taskList.value = editableList
        }
    }
}