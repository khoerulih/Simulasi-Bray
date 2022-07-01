package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddCourseViewModel

    private var day: Int = 0
    private var startTime: String = "00.00"
    private var endTime: String = "00.00"
    private var ibChecker: String = ""

    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        val factory = AddCourseViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory).get(AddCourseViewModel::class.java)

        val spinner = findViewById<Spinner>(R.id.day_spinner)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                day = position
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        val ibStartTime = findViewById<ImageButton>(R.id.ib_start_time)
        val ibEndTime = findViewById<ImageButton>(R.id.ib_end_time)

        tvStartTime = findViewById(R.id.tv_start_time)
        tvEndTime = findViewById(R.id.tv_end_time)

        ibStartTime.setOnClickListener {
            ibChecker = "startTime"
            val timePickerFragment: DialogFragment = TimePickerFragment()
            timePickerFragment.show(supportFragmentManager, "timePickerFragment")
        }

        ibEndTime.setOnClickListener {
            ibChecker = "endTime"
            val timePickerFragment: DialogFragment = TimePickerFragment()
            timePickerFragment.show(supportFragmentManager, "timePickerFragment")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                val edCourseName = findViewById<TextInputEditText>(R.id.ed_course_name)
                val edLecturer = findViewById<TextInputEditText>(R.id.ed_lecturer)
                val edNote = findViewById<TextInputEditText>(R.id.ed_note)

                val courseName = edCourseName.text.toString()
                val lecturer = edLecturer.text.toString()
                val note = edNote.text.toString()

                viewModel.insertCourse(courseName, day, startTime, endTime, lecturer, note)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        if (ibChecker == "startTime") {
            startTime = if (hour < 10 && minute < 10) {
                "0$hour:0$minute"
            } else if (hour < 10 && minute > 10) {
                "0$hour:$minute"
            } else if (hour > 10 && minute < 10) {
                "$hour:0$minute"
            } else {
                "$hour:$minute"
            }
            tvStartTime.text = startTime
        } else if (ibChecker == "endTime") {
            endTime = if (hour < 10 && minute < 10) {
                "0$hour:0$minute"
            } else if (hour < 10 && minute > 10) {
                "0$hour:$minute"
            } else if (hour > 10 && minute < 10) {
                "$hour:0$minute"
            } else {
                "$hour:$minute"
            }
            tvEndTime.text = endTime
        }
    }
}