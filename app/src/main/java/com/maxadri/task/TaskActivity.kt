package com.maxadri.task

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.maxadri.tasklist.Task
import com.maxadri.todo.R
import kotlinx.android.synthetic.main.create_task.*
import kotlinx.android.synthetic.main.item_task.*
import java.util.*
import kotlin.reflect.typeOf

class TaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_task)

        val task = intent?.getSerializableExtra("EditTask") as? Task
        //if (task?.id != null) newTask.setText("Edit Task") else newTask.setText("Create Task")
        //(task?.id != null) ? newTask.setText("Edit Task") : newTask.setText("Create Task")
        newTitle.setText(task?.title)
        newDescription.setText(task?.description)

        createTask.setOnClickListener {
            val newTask = Task(
                id = task?.id ?: UUID.randomUUID().toString(),
                title = newTitle.text.toString(),
                description = newDescription.text.toString()
            )
            newTask.setText()
            intent.putExtra(TASK_KEY, newTask)
            setResult(RESULT_OK, intent)
            finish()

        }
    }

    companion object {
        const val TASK_KEY = "task_key"
    }
}
