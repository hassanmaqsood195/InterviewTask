package com.hassan.myapplication

import android.Manifest
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.ProgressBar
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        btn_start.setOnClickListener {


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        33)
            } else {
                DownloadFile(progressBar1,"file1").execute("https://images.unsplash.com/photo-1521106047354-5a5b85e819ee?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb&dl=thomas-jensen-596085-unsplash.jpg")
                DownloadFile(progressBar2,"file2").execute("https://images.freeimages.com/images/large-previews/8f3/white-flower-power-1403046.jpg")
                DownloadFile(progressBar3,"file3").execute("https://images.pexels.com/photos/1213447/pexels-photo-1213447.jpeg?cs=srgb&dl=4k-wallpaper-blur-bulb-1213447.jpg&fm=jpg?dl&fit=crop&crop=entropy&w=6000&h=4000")
                DownloadFile(progressBar4,"file4").execute("https://images.unsplash.com/photo-1483058712412-4245e9b90334?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb&dl=carl-heyerdahl-181868-unsplash.jpg")
                DownloadFile(progressBar5,"file5").execute("https://images.unsplash.com/photo-1486312338219-ce68d2c6f44d?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb&dl=glenn-carstens-peters-203007-unsplash.jpg")


            }
        }



        fun onPermissionsGranted(requestCode: Int, perms: List<String>) {

        }
    }
        private inner class DownloadFile(val progressBar: ProgressBar,var fileNam : String) : AsyncTask<String, String, String>() {

            private var fileName: String? = null
            private var folder: String? = null

            /**
             * Before starting background thread
             * Show Progress Bar Dialog
             */
            override fun onPreExecute() {
                super.onPreExecute()
                Toast.makeText(this@MainActivity, "Starting Download!", Toast.LENGTH_SHORT).show()
            }

            /**
             * Downloading file in background thread
             */
            override fun doInBackground(vararg f_url: String): String {
                var count: Int
                try {
                    val url = URL(f_url[0])
                    val connection = url.openConnection()
                    connection.connect()
                    val lengthOfFile = connection.contentLength


                    // input stream to read file - with 8k buffer
                    val input = BufferedInputStream(url.openStream(), 8192)

                    val timestamp = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(Date())

                    //Extract file name from URL
                    fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length)

                    //Append timestamp to file name
                    fileName = timestamp + "_" + fileName

                    folder = Environment.getExternalStorageDirectory().absolutePath

                    val directory = File(folder)

                    if (!directory.exists()) {
                        directory.mkdirs()
                    }

                    val output = FileOutputStream(folder!! + "/Download/"+fileName +".jpg"!!)

                    val data = ByteArray(1024)

                    var total: Long = 0

                    count = input.read(data)

                    while (count != -1) {
                        total += count.toLong()

                        publishProgress("" + (total * 100 / lengthOfFile).toInt())
                        output.write(data, 0, count)
                        count = input.read(data)
                    }
                    output.flush()
                    output.close()
                    input.close()
                    return "Downloaded at: $folder$fileName"

                } catch (e: Exception) {
//                Log.e("Error: ", e.message)
                    e.printStackTrace()
                }

                return "Something went wrong"
            }

            /**
             * Updating progress bar
             */
            override fun onProgressUpdate(vararg progress: String) {
                progressBar!!.progress = Integer.parseInt(progress[0])
            }


            override fun onPostExecute(message: String) {
                Toast.makeText(this@MainActivity, "Download Completed!", Toast.LENGTH_SHORT).show()
            }
        }
}
