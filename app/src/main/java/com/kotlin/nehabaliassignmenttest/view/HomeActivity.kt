package com.kotlin.nehabaliassignmenttest.view

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kotlin.nehabaliassignmenttest.R
import com.kotlin.nehabaliassignmenttest.viewmodel.DownloadFileViewModel
import kotlinx.android.synthetic.main.activity_home.*
import java.io.File

class HomeActivity : AppCompatActivity() {
    private val mainViewModel: DownloadFileViewModel by viewModels()
    var fileName: String = "UdemyAssignmentDocument"
    lateinit var filePath :File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)



        downloadUrl.setOnClickListener {

            dialog_FileName()
        }

    }

    fun callingViewModel(){
        mainViewModel.dwonloadFile(filePath!!, fileName, url_tv.text.toString(), { success ->

            Log.d("TAG", "Download success, file path $success")

        }) { Key ->
            Log.d("TAG", "Download failed , worksheet: $Key")

        }
    }


    fun dialog_locations() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Where do you want to save the file?")
        val animals = arrayOf("Downloads", "Pictures", "DCIM", "Music")
        val checkedItem = 0
        var pos: Int = 0
        builder.setSingleChoiceItems(animals, checkedItem) { dialog, which ->
            pos = which

        }


        builder.setPositiveButton("OK") { dialog, which ->
            if (pos == 0) {
                 filePath = fileName.getFilePath_Downloads(this)
            } else if (pos == 1) {
                filePath = fileName.getFilePath_PICTURES(this)
            } else if (pos == 2) {
                filePath = fileName.getFilePath_DCIM(this)
            } else if (pos == 3) {
                filePath = fileName.getFilePath_Music(this)
            }
            dialog.cancel()
            callingViewModel()
        }
        builder.setNegativeButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
    }

    fun dialog_FileName() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_file_name)
        val body = dialog.findViewById(R.id.file_name_Ed) as EditText

        val saveFileName_mbutton = dialog.findViewById(R.id.saveFileName_mbutton) as Button
        val noBtn = dialog.findViewById(R.id.cancle_m) as Button

        saveFileName_mbutton.setOnClickListener {

            if (body.text.toString().equals("")) {
                body.setError("Enter File Name")
            } else {
                fileName=body.text.toString()
                dialog_locations()
                dialog.dismiss()
            }

        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }



}
/* getting  path */
fun String.getFilePath_Downloads(context: Context) = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "MyPDFs/$this")
fun String.getFilePath_PICTURES(context: Context) = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyPDFs/$this")
fun String.getFilePath_DCIM(context: Context) = File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "MyPDFs/$this")
fun String.getFilePath_Music(context: Context) = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "MyPDFs/$this")
