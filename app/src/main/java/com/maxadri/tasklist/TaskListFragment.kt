package com.maxadri.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxadri.task.TaskActivity
import com.maxadri.todo.R
import kotlinx.android.synthetic.main.fragment_task_list.*
import java.io.Serializable


class TaskListFragment : Fragment() {

    private lateinit var taskList: MutableList<Task>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskList = savedInstanceState?.getParcelableArrayList<Task>(KEY_TASK_LIST)?.toMutableList()
            ?: mutableListOf(
                Task(id = "id_1", title = "Task 1", description = "description 1"),
                Task(id = "id_2", title = "Task 2"),
                Task(id = "id_3", title = "Task 3")
            )
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
            taskList.remove(task)
            recyclerview.adapter?.notifyDataSetChanged()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val task = data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_TASK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                taskList.add(task)
                recyclerview.adapter?.notifyDataSetChanged()
            }
        }

        if (requestCode == EDIT_TASK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val index = taskList.indexOfFirst { it.id == task.id }
                taskList[index] = task
                //taskList.set(index, task)
                recyclerview.adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(KEY_TASK_LIST, taskList as ArrayList<Task>)
    }

    companion object {
        const val ADD_TASK_REQUEST_CODE = 201
        const val EDIT_TASK_REQUEST_CODE = 200
        const val KEY_TASK_LIST = "taskList"
    }
}
