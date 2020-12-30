package com.example.tdrecyclerview.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.tdrecyclerview.MainActivity
import com.example.tdrecyclerview.R
import com.example.tdrecyclerview.network.Api
import com.example.tdrecyclerview.userinfo.UserInfoActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.tdrecyclerview.task.TaskActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TaskListFragment : Fragment()
{
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    //var recyclerView = null;
    val taskList = emptyList<Task>()

    val adapter = TaskListAdapter()

    // On récupère une instance de ViewModel
    private val viewModel: TaskListViewModel = TaskListViewModel(adapter)

    private val tasksRepository = TasksRepository()

    enum class Purpose{
        Add,
        Edit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode === TaskActivity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                //println(data)
                //doSomeOperations()
                handleTaskFromResult(data)
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        //return super.onCreateView(inflater, container, savedInstanceState)

        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModel.taskList.observe(viewLifecycleOwner) { newList ->
            // utliser la liste
            adapter.taskList = newList
            adapter.notifyDataSetChanged()
        }

        var recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        recyclerView.adapter = adapter

        var addTask = view.findViewById<FloatingActionButton>(R.id.addTaskButton);
        addTask.setOnClickListener {
            //viewModel.addTask()
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra("Purpose", Purpose.Add)
            resultLauncher.launch(intent)
            //(activity as MainActivity).changeActivity(intent)
        }

        adapter.onDeleteClickListener = { task ->
            viewModel.deleteTask(task)
        }

        adapter.onUpdateClickListener = { task ->
            //viewModel.editTask(task)
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra("Purpose", Purpose.Edit)
            intent.putExtra("TASK", Json.encodeToString(task))
            resultLauncher.launch(intent)
        }

        adapter.onLongClickListener = {
            val sendIntent : Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "**" + it.title + "**: " + it.description)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
            true
        }
        val imageView = view?.findViewById<ImageView>(R.id.AvatarImage)
        imageView.setOnClickListener {
            val intent = Intent(activity, UserInfoActivity::class.java)
            (activity as MainActivity).changeActivity(intent)

        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.loadTasks(this)

        val imageView = view?.findViewById<ImageView>(R.id.AvatarImage)
        imageView?.load("https://goo.gl/gEgYUd")
        {
            transformations(CircleCropTransformation())
        }

    }

    suspend fun infoChanged()
    {
        val textView = view?.findViewById<TextView>(R.id.textViewUser)
        val userInfo = Api.userService.getInfo().body()
        textView?.text = userInfo.toString()
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }*/
    fun handleTaskFromResult(data: Intent?)
    {
        val task = data!!.getStringExtra(TaskActivity.TASK_KEY)?.let {
            Json.decodeFromString<Task>(Task.serializer(),
                it
            )
        }
        println("hello")
        if (task != null) {
            if (data.getSerializableExtra("Purpose") == Purpose.Add)
            {
                viewModel.addTask(task)
            }
            if (data.getSerializableExtra("Purpose") == Purpose.Edit)
            {
                viewModel.editTask(task)
            }
        }
        onResume()
    }
}