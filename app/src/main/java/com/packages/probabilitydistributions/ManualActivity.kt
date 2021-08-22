package com.packages.probabilitydistributions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ManualActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual)
        title = intent.getStringExtra("manualType").toString()
    }
}