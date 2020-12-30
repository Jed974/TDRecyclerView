package com.example.tdrecyclerview.userinfo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.tdrecyclerview.BuildConfig
import com.example.tdrecyclerview.MainActivity

import com.example.tdrecyclerview.R
import com.example.tdrecyclerview.network.Api
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {

    private var imageView: ImageView? = null
    private val userWebService = Api.INSTANCE.userService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        imageView = findViewById<ImageView>(R.id.UserImage)
        val takePictureButton = findViewById<Button>(R.id.TakePictureButton)
        takePictureButton.setOnClickListener {
            askCameraPermissionAndOpenCamera()

        }
        val choosePictureButton = findViewById<Button>(R.id.UploadImageButton)
        choosePictureButton.setOnClickListener {
            askFilePermissionAndOpenBrowser()

        }
        val quitButton = findViewById<Button>(R.id.QuitUserInfoButton)
        quitButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val userInfo = userWebService.getInfo()
            if (userInfo.isSuccessful)
            {
                imageView?.load(userInfo.body()?.avatar)
            }

        }
    }

    private val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) openCamera()
                else showCameraExplanationDialog()
            }

    private fun requestCameraPermission() =
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)

    private fun askCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> openCamera()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showCameraExplanationDialog()
            else -> requestCameraPermission()
        }
    }


    private fun showCameraExplanationDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("On a besoin de la caméra sivouplé ! 🥺")
            setPositiveButton("Bon, ok") { _, _ ->
                requestCameraPermission()
            }
            setCancelable(true)
            show()
        }
    }

    private fun handleImage(toUri: Uri) {
        lifecycleScope.launch {
            userWebService.updateAvatar(convert(toUri))
        }
    }

    // use
    //private fun openCamera() = takePicture.launch()

    // convert     
    private fun convert(uri: Uri) =
            MultipartBody.Part.createFormData(
                    name = "avatar",
                    filename = "temp.jpeg",
                    body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
            )

    // create a temp file and get a uri for it
    private val photoUri by lazy {
        FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID +".fileprovider",
            File.createTempFile("avatar", ".jpeg", externalCacheDir)

        )
    }

    // register
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) handleImage(photoUri)
        else Toast.makeText(this, "Erreur ! 😢", Toast.LENGTH_LONG).show()
    }

    // use
    private fun openCamera() = takePicture.launch(photoUri)


    private val requestFilePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) openBrowser()
            else showFileExplanationDialog()
        }

    private fun requestFilePermission() =
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

    private fun askFilePermissionAndOpenBrowser() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED -> openBrowser()
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> showFileExplanationDialog()
            else -> requestFilePermission()
        }
    }


    private fun showFileExplanationDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("On a besoin des images ! 🥺")
            setPositiveButton("Bon, ok") { _, _ ->
                requestFilePermission()
            }
            setCancelable(true)
            show()
        }
    }
    // register
    private val pickInGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            handleImage(it)
        }

    // use
    private fun openBrowser() = pickInGallery.launch("image/*")
}