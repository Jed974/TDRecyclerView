package com.example.tdrecyclerview

import android.app.Application
import com.example.tdrecyclerview.network.Api

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Api.INSTANCE = Api(this)
    }
}