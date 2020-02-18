package com.maxadri.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxadri.tasklist.Task

class TasksRepository {

    private val tasksWebService = Api.tasksWebService

    suspend fun refresh(): List<Task>? {
        val response = tasksWebService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun deleteTask(task: Task): Boolean {
        val response = tasksWebService.deleteTask(task.id)
        return response.isSuccessful
    }

    suspend fun editTask(task: Task): Task? {
        val response = tasksWebService.updateTask(task)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun createTask(task: Task): Task? {
        val response = tasksWebService.createTask(task)
        return if (response.isSuccessful) response.body() else null
    }

}