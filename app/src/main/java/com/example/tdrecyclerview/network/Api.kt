package com.example.tdrecyclerview.network

import android.content.Context
import android.os.strictmode.InstanceCountViolation
import android.provider.Settings.System.putString
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.example.tdrecyclerview.authentication.SHARED_PREF_TOKEN_KEY
import com.example.tdrecyclerview.tasklist.TasksWebService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class Api(private val context: Context) {

    companion object{
        // constantes qui serviront à faire les requêtes
        private const val BASE_URL = "https://android-tasks-api.herokuapp.com/api/"
        lateinit var INSTANCE: Api
    }

    fun GetToken(): String? {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SHARED_PREF_TOKEN_KEY, "")
    }
    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val tasksWebService: TasksWebService by lazy {
        retrofit.create(TasksWebService::class.java)
    }

    // on construit une instance de parseur de JSON:
    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    // instance de convertisseur qui parse le JSON renvoyé par le serveur:
    private val converterFactory =
        jsonSerializer.asConverterFactory("application/json".toMediaType())

    // client HTTP
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                // intercepteur qui ajoute le `header` d'authentification avec votre token:
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${GetToken()}")
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    // permettra d'implémenter les services que nous allons créer:
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .build()


}