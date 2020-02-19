package com.maxadri.userInfo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.maxadri.network.Api
import com.maxadri.network.IUserService
import com.maxadri.network.UserInfo
import com.maxadri.tasklist.UserInfoViewModel
import com.maxadri.todo.R
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class UserInfoActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(UserInfoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        take_picture_button.setOnClickListener {
            askCameraPermission()
        }

        upload_image_button.setOnClickListener {
            askStoragePermission()
        }

        editUserInfo.setOnClickListener{
            val user = UserInfo(
                email = mailEdit.text.toString(),
                firstName = firstNameEdit.text.toString(),
                lastName = lastNameEdit.text.toString(),
                avatar = ""
            )

            viewModel.update(user)
            setResult(RESULT_OK, intent)
            finish()
        }

        val glide = Glide.with(this)
        viewModel.userInfo.observe(this, Observer {
            lastNameEdit.setText(it.lastName)
            firstNameEdit.setText(it.firstName)
            mailEdit.setText(it.email)
            glide.load(it.avatar).apply(RequestOptions.circleCropTransform().override(500,500)).into(image_view)
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.getInfos()
    }

    private fun askCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                showDialogBeforeRequest()
            } else {
                requestCameraPermission()
            }
        } else {
            openCamera()
        }
    }

    private fun askStoragePermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showDialogBeforeRequest()
            } else {
                requestStoragePermission()
            }
        } else {
            openStorage()
        }
    }

    private fun openStorage() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    private fun showDialogBeforeRequest() {
        AlertDialog.Builder(this).apply {
            setMessage("On a besoin de la caméra")
            setPositiveButton(android.R.string.ok) { _, _ -> requestCameraPermission()}
            setCancelable(true)
            show()
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            Toast.makeText(this, "Si vous refusez, on ne peut pas prendre de photo.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                handlePhotoTaken(data)
            }
        }

        if(requestCode == GALLERY_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data?.data)
                val imageBody = imageToBody(bitmap)
                viewModel.updateAvatar(imageBody)
            }
        }
    }

    private fun handlePhotoTaken(data: Intent?) {
        val image = data?.extras?.get("data") as? Bitmap

        val imageBody = imageToBody(image)
        viewModel.updateAvatar(imageBody)
    }

    private fun imageToBody(image: Bitmap?): MultipartBody.Part {
        val f = File(cacheDir, "tmpfile.jpg")
        f.createNewFile()
        try {
            val fos = FileOutputStream(f)
            image?.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(MediaType.parse("image/png"), f)
        return MultipartBody.Part.createFormData("avatar", f.path, body)
    }

    companion object {
        const val CAMERA_PERMISSION_CODE = 42
        const val STORAGE_PERMISSION_CODE = 52
        const val CAMERA_REQUEST_CODE = 2001
        const val GALLERY_REQUEST_CODE = 2011
    }


}
