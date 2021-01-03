package com.example.tdrecyclerview.task

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tdrecyclerview.R
import com.example.tdrecyclerview.notification.AlarmReceiver
import com.example.tdrecyclerview.notification.sendNotification
import com.example.tdrecyclerview.tasklist.Task
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.Serializable
import java.util.*

class TaskActivity : AppCompatActivity() {
    companion object {
        val TASK_KEY: String? = "TASK_KEY"
        const val ADD_TASK_REQUEST_CODE = 666
        const val RESULT_OK = 100
    }
    // Instanciation d'un nouvel objet [Task]
    var newTask = Task(id = UUID.randomUUID().toString(), title = "New Task !", description = "New Description", date = Date())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_activity)
        val titleText = findViewById<EditText>(R.id.TaskInputEditTitle)
        val descriptionText = findViewById<EditText>(R.id.TaskInputEditDescription)
        val saveButton = findViewById<Button>(R.id.SaveButton)
        saveButton.setOnClickListener {
            if (titleText.text.toString() != "")
            {
                newTask.title = titleText.text.toString()
            }
            if (descriptionText.text.toString() != "")
            {
                newTask.description = descriptionText.text.toString()
            }


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

        }



        val notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(
            applicationContext.getText(R.string.task_due).toString(),
            applicationContext
        )
        //println("hello")

    }

}