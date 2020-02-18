package com.maxadri.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxadri.tasklist.Task

class TasksRepository {

    private val tasksWebService = Api.tasksWebService

    private val _tasklist = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> = _tasklist

    suspend fun refresh() {
        val tasksResponse = tasksWebService.getTasks()
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            _tasklist.postValue(fetchedTasks)
        }
    }

    // UPDATE
    suspend fun updateTask(task: Task) {
        val tasksResponse = tasksWebService.updateTask(task)
        if (tasksResponse.isSuccessful) {
            val editableList = _tasklist.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { task.id == it.id }
            editableList[position] = task
            _tasklist.value = editableList
        }
    }

    // DELETE
    suspend fun deleteTask(task: Task) {
        val tasksResponse = tasksWebService.deleteTask(task.id)
        if (tasksResponse.isSuccessful) {
            val deleteOneTask = _tasklist.value.orEmpty().toMutableList()
            deleteOneTask.remove(task)
            _tasklist.value = deleteOneTask
        }
    }

    // CREATE
    suspend fun createTask(task: Task) {
        val taskResponse = tasksWebService.createTask(task)
        if (taskResponse.isSuccessful) {
            val createOneTask = _tasklist.value.orEmpty().toMutableList()
            createOneTask.add(task)
            _tasklist.value = createOneTask
        }
    }

}