package com.mehmetali.dialogapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.PagerSnapHelper
import com.mehmetali.dialogapp.databinding.ActivityDialogBinding
import com.mehmetali.dialogapp.databinding.DialogHeaderBinding

class DialogActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDialogBinding.inflate(layoutInflater)
        val x = DisplayMetrics()
        applicationContext.display?.getRealMetrics(x)
        Toast.makeText(applicationContext,"${x.widthPixels} to ${x.heightPixels}",Toast.LENGTH_LONG)
        Log.d("[Metrics]","${x.widthPixels} to ${x.heightPixels}")
        binding.dialogParent.layoutParams = RelativeLayout.LayoutParams(
            (x.widthPixels * 0.9).toInt(),(x.heightPixels *0.8).toInt()
        )
        setContentView(binding.root)
        val adapter = RecyclerViewAdapter(applicationContext)
        val dialogHeaderBinding = DialogHeaderBinding.inflate(layoutInflater)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(dialogHeaderBinding.imageList)
        dialogHeaderBinding.imageList.adapter = adapter
        binding.dialogContentView.addHeaderView(dialogHeaderBinding.root)
        binding.dialogContentView.adapter = ImageAdapter(applicationContext)

    }
}