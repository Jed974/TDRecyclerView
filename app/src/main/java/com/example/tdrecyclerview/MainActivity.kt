package com.example.tdrecyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tdrecyclerview.userinfo.UserInfoActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun changeActivity(java: Class<UserInfoActivity>)
    {

        val intent = Intent(this, java)
        startActivity(intent)

    }
}