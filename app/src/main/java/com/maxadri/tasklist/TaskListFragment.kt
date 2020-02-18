package com.maxadri.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxadri.network.Api
import com.maxadri.network.TasksRepository
import com.maxadri.task.TaskActivity
import com.maxadri.todo.R
import kotlinx.android.synthetic.main.fragment_task_list.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.io.Serializable


class TaskListFragment : Fragment() {

    private lateinit var taskList

            : MutableList<Task>
    private val tasksRepository = TasksRepository()
    private val coroutineScope = MainScope()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskList = savedInstanceState?.getParcelableArrayList<Task>(KEY_TASK_LIST).orEmpty().toMutableList()
        val adapter = TaskListAdapter(taskList)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(activity)

        addTask.setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
           startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        // Action sur un element en particulier de la liste
        adapter.onEditClickListener = {
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra("EditTask", it as Serializable)
            startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
        }

        adapter.onDeleteClickListener = { task ->
            lifecycleScope.launch {
                tasksRepository.deleteTask(task)
            }
        }

        //val info = view.findViewById<TextView>(R.id.info)

        tasksRepository.taskList.observe(this, Observer {
            taskList.clear()
            taskList.addAll(it)
            adapter.notifyDataSetChanged()
        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val task = data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_TASK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                lifecycleScope.launch {
                    tasksRepository.createTask(task)
                }
            }
        }

        if (requestCode == EDIT_TASK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                lifecycleScope.launch{
                    tasksRepository.updateTask(task)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(KEY_TASK_LIST, taskList as ArrayList<Task>)
    }

    override fun onResume() {
        super.onResume()

        coroutineScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            info.text = "${userInfo.firstName} ${userInfo.lastName}"
        }

        lifecycleScope.launch {
            tasksRepository.refresh()
        }
    }

    companion object {
        const val ADD_TASK_REQUEST_CODE = 201
        const val EDIT_TASK_REQUEST_CODE = 200
        const val KEY_TASK_LIST = "taskList"
    }
}
