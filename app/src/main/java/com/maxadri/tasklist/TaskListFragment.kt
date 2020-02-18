package com.maxadri.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxadri.network.Api
import com.maxadri.network.UserInfo
import com.maxadri.task.TaskActivity
import com.maxadri.todo.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_task_list.*
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.io.Serializable


class TaskListFragment : Fragment() {

    private val adapter = TaskListAdapter()

    private val viewModel by lazy {
        ViewModelProvider(this).get(TaskListViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(activity)

        addTask.setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
           startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        adapter.onEditClickListener = {
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra("EditTask", it as Serializable)
            startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
        }

        adapter.onDeleteClickListener = { task ->
            viewModel.deleteTask(task)
        }

        viewModel.taskList.observe(this, Observer { newList ->
            adapter.list = newList.orEmpty()
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val task = data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_TASK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.createTask(task)
            }
        }

        if (requestCode == EDIT_TASK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.editTask(task)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadTask()

        lifecycleScope.launch {
           val userInfo: UserInfo = Api.userService.getInfo().body()!!
           user_info_text.text = "${userInfo.firstName} ${userInfo.lastName}"
        }



    }

    companion object {
        const val ADD_TASK_REQUEST_CODE = 201
        const val EDIT_TASK_REQUEST_CODE = 200
    }
}
