package com.example.tdrecyclerview.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tdrecyclerview.network.Api

class TasksRepository {
    private val tasksWebService = Api.INSTANCE.tasksWebService

    /*// Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableLiveData<List<Task>>()

    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: LiveData<List<Task>> = _taskList*/

    suspend fun refresh(): List<Task>? {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        /*if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            _taskList.value = fetchedTasks!!
        }*/
        return if (tasksResponse.isSuccessful) tasksResponse.body() else null
    }

    suspend fun updateTask(task: Task): Task? {
        val tasksResponse = tasksWebService.updateTask(task)
        return if (tasksResponse.isSuccessful) tasksResponse.body() else null
    }

    suspend fun createTask(task: Task): Task? {
        val tasksResponse = tasksWebService.createTask(task)
        return if (tasksResponse.isSuccessful) tasksResponse.body() else null
    }

    suspend fun deleteTask(id: String): String? {
        val tasksResponse = tasksWebService.deleteTask(id)
        return if (tasksResponse.isSuccessful) tasksResponse.body() else null
    }
}