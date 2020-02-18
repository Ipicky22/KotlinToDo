package com.maxadri.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxadri.network.TasksRepository
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {

    private val tasksRepository = TasksRepository()
    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> = _taskList

    private fun getMutableList() = _taskList.value.orEmpty().toMutableList()

    fun loadTask() {
        viewModelScope.launch {
            tasksRepository.refresh()?.let {
                _taskList.value = it
            }
        }
    }

    fun createTask(task: Task) {
        viewModelScope.launch {
            tasksRepository.createTask(task)?.let {
                _taskList.value = getMutableList().apply {
                    add(it)
                }
            }
        }
    }

    fun editTask(task: Task) {
        viewModelScope.launch {
            tasksRepository.editTask(task)?.let {
                _taskList.value = getMutableList().apply {
                    val position = indexOfFirst { task.id == it.id }
                    set(position, task)
                }
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            if (tasksRepository.deleteTask(task)) {
                _taskList.value = getMutableList().apply { remove(task) }
            }
        }
    }
}