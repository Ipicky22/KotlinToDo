package com.maxadri.task

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.maxadri.tasklist.Task
import com.maxadri.todo.R
import kotlinx.android.synthetic.main.create_task.*
import java.util.*

class TaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_task)

        createTask.setOnClickListener {
            val task = Task(id = UUID.randomUUID().toString(), title = newTitle.text.toString(), description = newDescription.text.toString())
            intent.putExtra(Companion.EXTRA_REPLY.toString(), task)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = 1
    }
}
