package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID
import com.google.android.material.textfield.TextInputEditText

class DetailTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //TODO 11 : Show detail task and implement delete action
        val viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[DetailTaskViewModel::class.java]

        val edtTitle = findViewById<TextInputEditText>(R.id.detail_ed_title)
        val edtDescription = findViewById<TextInputEditText>(R.id.detail_ed_description)
        val edtDueDate = findViewById<TextInputEditText>(R.id.detail_ed_due_date)
        val btnDelete = findViewById<Button>(R.id.btn_delete_task)

        val taskId = intent.getIntExtra(TASK_ID, 0)
        viewModel.setTaskId(taskId)
        viewModel.task.observe(this){ task ->
            edtTitle.setText(task.title)
            edtDescription.setText(task.description)
            edtDueDate.setText(DateConverter.convertMillisToString(task.dueDateMillis))
        }

        btnDelete.setOnClickListener {
            viewModel.task.removeObservers(this)
            viewModel.deleteTask()
            finish()
        }
    }
}