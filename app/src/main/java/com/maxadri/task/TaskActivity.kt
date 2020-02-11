package com.maxadri.task

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.maxadri.tasklist.Task
import com.maxadri.todo.R
import kotlinx.android.synthetic.main.create_or_edit_task.*
import java.util.*

class TaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_or_edit_task)

        val task = intent?.getSerializableExtra("EditTask") as? Task

        if (task?.id != null) statusTask.setText("Edit Task") else statusTask.setText("Create Task")
        if (task?.id != null) createOrEditTask.setText("Edit Task") else createOrEditTask.setText("Create Task")

        titleTask.setText(task?.title)
        descriptionTask.setText(task?.description)


        createOrEditTask.setOnClickListener {
            val newTask = Task(
                id = task?.id ?: UUID.randomUUID().toString(),
                title = titleTask.text.toString(),
                description = descriptionTask.text.toString()
            )
            intent.putExtra(TASK_KEY, newTask)
            setResult(RESULT_OK, intent)
            finish()

        }
    }

    companion object {
        const val TASK_KEY = "task_key"
    }
}
