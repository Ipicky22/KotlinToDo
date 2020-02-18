package com.maxadri.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maxadri.todo.R
import kotlinx.android.synthetic.main.item_task.view.*
import kotlin.properties.Delegates

class TaskListAdapter() : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    var list: List<Task> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    var onDeleteClickListener: (Task) -> Unit = { }

    var onEditClickListener: (Task) -> Unit = { }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {

            itemView.findViewById<TextView>(R.id.task_title).text = task.title
            itemView.findViewById<TextView>(R.id.task_description).text = task.description

            // Une autre notation
            // itemView.task_title.text = task.title
            // itemView.task_description = task.description

            itemView.deleteTask.setOnClickListener { onDeleteClickListener.invoke(task) }
            itemView.editTask.setOnClickListener   { onEditClickListener.invoke(task) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(list[position])
    }

}
