package com.maxadri.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.maxadri.network.Api
import com.maxadri.network.UserInfo
import com.maxadri.task.TaskActivity
import com.maxadri.todo.R
import com.maxadri.userInfo.UserInfoActivity
import kotlinx.android.synthetic.main.fragment_task_list.*
import kotlinx.coroutines.launch
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

        user_info_text.setOnClickListener {
            val intent = Intent(activity, UserInfoActivity::class.java)
            startActivityForResult(intent, EDIT_USER_REQUEST_CODE)
        }

        avatar.setOnClickListener {
            val intent = Intent(activity, UserInfoActivity::class.java)
            startActivityForResult(intent, EDIT_USER_REQUEST_CODE)
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
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_TASK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val task = data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task
                viewModel.createTask(task)
            }
        }

        if (requestCode == EDIT_TASK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val task = data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task
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
            Glide.with(this@TaskListFragment).load(userInfo.avatar).apply(RequestOptions.circleCropTransform().override(200,200)).into(avatar)
        }
    }

    companion object {
        const val ADD_TASK_REQUEST_CODE = 201
        const val EDIT_TASK_REQUEST_CODE = 200
        const val EDIT_USER_REQUEST_CODE = 300
    }
}
