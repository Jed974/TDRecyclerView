package com.example.tdrecyclerview.task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.GregorianCalendar
import android.os.Build
import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.format.DateUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tdrecyclerview.R
import com.example.tdrecyclerview.notification.AlarmReceiver
import com.example.tdrecyclerview.notification.sendNotification
import com.example.tdrecyclerview.tasklist.Task
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.Serializable
import java.lang.Exception
import java.lang.NumberFormatException
import java.util.*

class TaskActivity : AppCompatActivity() {
    companion object {
        val TASK_KEY: String? = "TASK_KEY"
        const val ADD_TASK_REQUEST_CODE = 666
        const val RESULT_OK = 100
    }
    // Instanciation d'un nouvel objet [Task]
    var newTask = Task(id = UUID.randomUUID().toString(), title = "New Task !", description = "New Description", date = Date())

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_activity)
        val titleText = findViewById<EditText>(R.id.TaskInputEditTitle)
        val descriptionText = findViewById<EditText>(R.id.TaskInputEditDescription)
        val saveButton = findViewById<Button>(R.id.SaveButton)
        val dateText = findViewById<EditText>(R.id.inputDateTask)
        val timeText = findViewById<EditText>(R.id.inputTimeTask)
        val dateButton = findViewById<Button>(R.id.buttonSetDate)
        val timeButton = findViewById<Button>(R.id.buttonSetTime)
        saveButton.setOnClickListener {
            if (titleText.text.toString() != "")
            {
                newTask.title = titleText.text.toString()
            }
            if (descriptionText.text.toString() != "")
            {
                newTask.description = descriptionText.text.toString()
            }

            var newDate = Date()
            val dateTextString = dateText.text.toString()
            if(!dateTextString.equals("")){
                try{
                    newDate = GregorianCalendar(
                            dateTextString.substring(6, 10).toInt(),
                            dateTextString.substring(3,5).toInt() - 1,
                            dateTextString.substring(0,2).toInt()).time
                }catch(ex : Exception){
                    println(ex.message)
                    Toast.makeText(applicationContext, "Can't parse date ! Setting to today", Toast.LENGTH_LONG).show()
                }
            }
            val timeTextString = timeText.text.toString()
            if(!dateTextString.equals("")){
                try{
                    var nbMinutesTotal = timeTextString.substring(3,5).toInt()
                    nbMinutesTotal += 60 * timeTextString.substring(0,2).toInt()
                    val nbMillisTotal = nbMinutesTotal * DateUtils.MINUTE_IN_MILLIS
                    newDate = Date(newDate.time + nbMillisTotal)
                }catch(ex: Exception){
                    println(ex.message)
                    android.widget.Toast.makeText(applicationContext, "Can't parse time !", android.widget.Toast.LENGTH_LONG).show()
                }
            }
            newTask.date = newDate

            intent.putExtra(TASK_KEY, Json.encodeToString(newTask))
            //println(intent.extras?.get(TASK_KEY))
            setResult(RESULT_OK, intent)
            finish()

        }
        val cancelButton = findViewById<Button>(R.id.cancel_addtask_button)
        cancelButton.setOnClickListener(){
            finish()
        }
        if (intent.getStringExtra("TASK") != null)
        {
            val oldTask = intent!!.getStringExtra("TASK")?.let {
                Json.decodeFromString<Task>(Task.serializer(),
                    it
                )
            }
            newTask.id = oldTask?.id.toString()
            titleText.setText(oldTask?.title)
            descriptionText.setText(oldTask?.description)
            dateText.setText(SimpleDateFormat("dd-MM-yyyy").format(oldTask?.date))
            timeText.setText(SimpleDateFormat("HH:mm").format(oldTask?.date))
        }

        dateButton.setOnClickListener(){
            var year = 0
            var month = 0
            var day = 0
            var c = Calendar.getInstance()
            if(dateText.text.toString().equals("")){
                day = c.get(Calendar.DAY_OF_MONTH)
                month = c.get(Calendar.MONTH)
                year = c.get(Calendar.YEAR)
            }else{
                val currentDate = dateText.text.toString()
                try {
                    day = currentDate.subSequence(0, 2).toString().toInt()
                }catch (ex: Exception){
                    day = c.get(Calendar.DAY_OF_MONTH)
                }
                try {
                    month = currentDate.subSequence(3, 5).toString().toInt()
                }catch (ex: Exception){
                    month = c.get(Calendar.MONTH)
                }
                try {
                    year = currentDate.subSequence(6, 10).toString().toInt()
                }catch (ex: Exception){
                    year = c.get(Calendar.YEAR)
                }
            }

            val dialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener(){
                    view, y, m, d -> dateText.setText("%02d".format(d)+"-" + "%02d".format(m) + "-" + "%04d".format(y))
                },
                        year, month, day )
            dialog.show()
        }

        timeButton.setOnClickListener(){
            var hour = 0
            var minute = 0
            var c = Calendar.getInstance()
            if(timeText.text.toString().equals("")){
                hour = c.get(Calendar.HOUR_OF_DAY)
                minute = c.get(Calendar.MINUTE)
            }else{
                val currentTime = timeText.text.toString()
                try{
                    hour = currentTime.subSequence(0, 2).toString().toInt()
                }catch(ex: Exception){
                    hour = c.get(Calendar.HOUR_OF_DAY)
                }
                try{
                    minute = currentTime.subSequence(3, 4).toString().toInt()
                }catch(ex: Exception){
                    minute = c.get(Calendar.MINUTE)
                }
            }
            val dialog = TimePickerDialog(this,
                TimePickerDialog.OnTimeSetListener(){
                    v, h, m -> timeText.setText("%02d".format(h) + ":" + "%02d".format(m))
                },
                    hour, minute, true)
            dialog.show()
        }

        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(
            this.getText(R.string.task_due).toString(),
            applicationContext
        )
        //println("hello")

    }

}