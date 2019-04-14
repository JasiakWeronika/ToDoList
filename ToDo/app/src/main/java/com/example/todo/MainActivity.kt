package com.example.todo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_task.view.*
import kotlinx.android.synthetic.main.content_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var toDoList = ArrayList<ToDoList>()
    private lateinit var myArrayAdapter: MyArrayAdapter
    private var calendar: Calendar = Calendar.getInstance()
    private var timeFormat = "HH:mm"
    private var dateFormat = "dd.MM.yyyy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.setOnClickListener {
            dialog("", 0,"HH:MM","DD.MM.YYYY", "star", -1)
        }

        listView.setOnItemLongClickListener { _, _, i, _ ->
            confirm(i)
            true
        }

//        toDoList.add(ToDoList("1", 0, "14 : 11", "19.12.1998", "heart"))
//        toDoList.add(ToDoList("2", 1, "13 : 11", "20.01.1997", "star"))
//        toDoList.add(ToDoList("3", 2, "05 : 12", "18.02.2010", "work"))
//        toDoList.add(ToDoList("4", 0, "05 : 11", "21.02.2011", "travel"))
//        toDoList.add(ToDoList("5", 1, "05 : 11", "09.06.2011", "school"))
//        toDoList.add(ToDoList("6", 2, "15 : 11", "09.07.2018", "eat"))
//        toDoList.add(ToDoList("7", 0, "13 : 11", "09.07.2018", "travel"))
//        toDoList.add(ToDoList("8", 1, "11 : 11", "09.01.2019", "cake"))

        myArrayAdapter = MyArrayAdapter(this, toDoList)
        listView.adapter = myArrayAdapter
        myArrayAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.descriptionU -> {
                toDoList.sortBy { it.description }
            }
            R.id.descriptionD -> {
                toDoList.sortByDescending { it.description }
            }
            R.id.priorityU -> {
                toDoList.sortBy { it.priority }
            }
            R.id.priorityD -> {
                toDoList.sortByDescending { it.priority }
            }
            R.id.timeU -> {
                toDoList.sortBy { it.time }
            }
            R.id.timeD -> {
                toDoList.sortByDescending { it.time }
            }
            R.id.dateU -> {
                toDoList.sortWith(compareBy<ToDoList> {
                    it.date.takeLast(4)
                }.thenBy {
                    it.date.takeLast(7).take(2)
                }.thenBy { it.date.take(2)
                })
            }
            R.id.dateD -> {
                toDoList.sortWith(compareByDescending<ToDoList> {
                    it.date.takeLast(4)
                }.thenByDescending {
                        it.date.takeLast(7).take(2)
                }.thenByDescending { it.date.take(2)
                })
            }
            R.id.imageUD -> {
                toDoList.sortBy { it.image }
            }
            else -> return super.onOptionsItemSelected(item)
        }
        myArrayAdapter.notifyDataSetChanged()
        return true
    }

    private fun confirm(i: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.confirm_title)

        builder.setPositiveButton(R.string.edit) { _, _ ->
            dialog(
                toDoList[i].description,
                toDoList[i].priority,
                toDoList[i].time,
                toDoList[i].date,
                toDoList[i].image,
                i
            )
        }

        builder.setNeutralButton(R.string.negative) { _, _ ->
            delete(i)
            Toast.makeText(applicationContext, "Usunięto zadanie!", Toast.LENGTH_LONG).show()
        }

        builder.show()
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun dialog(description: String, priority: Int, time: String, date: String, image: String, i: Int) {
        var descriptionTemp: String = description
        var priorityTemp: Int = priority
        var timeTemp: String = time
        var dateTemp: String = date
        var imageTemp: String = image

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = layoutInflater
        builder.setTitle(R.string.dialog_title)

        val dialog: View = inflater.inflate(R.layout.add_task, null)
        builder.setView(dialog)

        dialog.descriptionAdd.text = description.toEditable()
        dialog.descriptionAdd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                descriptionTemp = s.toString()
            }
        })

        when(priority) {
            0 -> dialog.low.isChecked = true
            1 -> dialog.medium.isChecked = true
            2 -> dialog.high.isChecked = true
        }
        dialog.priority_group.setOnCheckedChangeListener { _, id->
            when(id) {
                R.id.low -> priorityTemp = 0
                R.id.medium -> priorityTemp = 1
                R.id.high -> priorityTemp = 2
            }
        }

        dialog.timeAdd.text = time
        val timeSetListener: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar.set(android.icu.util.Calendar.HOUR_OF_DAY, hour)
            calendar.set(android.icu.util.Calendar.MINUTE, minute)
            val simpleDateFormat = SimpleDateFormat(timeFormat, Locale.UK)
            dialog.timeAdd.text = simpleDateFormat.format(calendar.time)
            timeTemp = simpleDateFormat.format(calendar.time)
        }
        dialog.timeAdd.setOnClickListener {
            TimePickerDialog(
                this,
                timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        dialog.dateAdd.text = date
        val dateSetListener: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.UK)
            dialog.dateAdd.text = simpleDateFormat.format(calendar.time)
            dateTemp = simpleDateFormat.format(calendar.time)
        }
        dialog.dateAdd.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(android.icu.util.Calendar.YEAR),
                calendar.get(android.icu.util.Calendar.MONTH),
                calendar.get(android.icu.util.Calendar.DAY_OF_MONTH)
            ).show()
        }

        when(image) {
            "star" -> dialog.star.isChecked = true
            "heart" -> dialog.heart.isChecked = true
            "school" -> dialog.school.isChecked = true
            "eat" -> dialog.eat.isChecked = true
            "travel" -> dialog.travel.isChecked = true
            "work" -> dialog.work.isChecked = true
            "phone" -> dialog.phone.isChecked = true
            "cake" -> dialog.cake.isChecked = true
            "buy" -> dialog.buy.isChecked = true
        }
        dialog.image_group.setOnCheckedChangeListener {_, id->
            when(id) {
                R.id.star -> imageTemp = "star"
                R.id.heart -> imageTemp = "heart"
                R.id.school -> imageTemp = "school"
                R.id.eat -> imageTemp = "eat"
                R.id.travel -> imageTemp = "travel"
                R.id.work -> imageTemp = "work"
                R.id.phone -> imageTemp = "phone"
                R.id.cake -> imageTemp = "cake"
                R.id.buy -> imageTemp = "buy"
            }
        }

        builder.setPositiveButton(R.string.positive) { _, _ ->
            if(i == -1) {
                add(descriptionTemp, priorityTemp, timeTemp, dateTemp, imageTemp)
            } else {
                toDoList[i] = ToDoList(descriptionTemp, priorityTemp, timeTemp, dateTemp, imageTemp)
                myArrayAdapter.notifyDataSetChanged()
            }
        }

        builder.setNeutralButton(R.string.neutral) { _, _ -> }

        builder.show()
    }

    private fun add(description: String, priority: Int, time: String, date: String, image: String) {
        val toDo = ToDoList(description, priority, time, date, image)
        toDoList.add(toDo)
        myArrayAdapter.notifyDataSetChanged()
    }

    private fun delete(i: Int) {
        toDoList.removeAt(i)
        myArrayAdapter.notifyDataSetChanged()
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

}
