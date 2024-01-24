package com.example.instagramclone.data

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.instagramclone.R
import com.example.instagramclone.domain.repository.SaveImageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject


class SaveImageRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context) : SaveImageRepository {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun saveImage(bitmap: Bitmap){

        val timestamp = System.currentTimeMillis()

        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, timestamp)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            moreThanQueenCake(values, timestamp, bitmap)
        } else {
            lessThanQueenCake(timestamp, bitmap, values)
        }
    }

    private fun lessThanQueenCake(
        timestamp: Long,
        bitmap: Bitmap,
        values: ContentValues
    ) {
        val imageFileFolder = File(
            Environment.getExternalStorageDirectory()
                .toString() + '/' + appContext.getString(R.string.app_name)
        )
        if (!imageFileFolder.exists()) {
            imageFileFolder.mkdirs()
        }
        val mImageName = "$timestamp.png"
        val imageFile = File(imageFileFolder, mImageName)
        try {
            val outputStream: OutputStream = FileOutputStream(imageFile)
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                Log.e("saveImage",  e.message.toString())
            }
            values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)

            appContext.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            Toast.makeText(appContext, "Saved...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("saveImage",  e.message.toString())
        }
    }

    private fun moreThanQueenCake(
        values: ContentValues,
        timestamp: Long,
        bitmap: Bitmap
    ) {
        values.put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
        values.put(
            MediaStore.Images.Media.RELATIVE_PATH,
            "Pictures/" + appContext.getString(R.string.app_name)
        )
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        val uri: Uri? =
            appContext.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            try {
                val outputStream: OutputStream? = appContext.contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    try {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                        outputStream.close()
                    } catch (e: Exception) {
                        Log.e("saveImage",  e.message.toString())
                    }
                }
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                appContext.contentResolver.update(uri, values, null, null)
                Toast.makeText(appContext, "Saved...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("saveImage",  e.message.toString())
            }
        }
    }
}