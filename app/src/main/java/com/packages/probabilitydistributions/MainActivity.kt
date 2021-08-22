package com.packages.probabilitydistributions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.ads.*

var uniformButton: Button? = null
var poissonButton: Button? = null
var bernoulliButton: Button? = null
var binomialButton: Button? = null
var i: Intent? = null

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uniformButton = findViewById(R.id.uniform_button)
        poissonButton = findViewById(R.id.poission_button)
        bernoulliButton = findViewById(R.id.bernouli_button)
        binomialButton = findViewById(R.id.binomial_button)

        var mAdView : AdView = findViewById(R.id.adView)
        var mAdView2 : AdView = findViewById(R.id.adView2)

        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()
        val adRequest2 = AdRequest.Builder().build()

        mAdView.loadAd(adRequest)
        mAdView2.loadAd(adRequest2)

        uniformButton?.setOnClickListener {
            i = Intent(this, UniformDistribution::class.java)
            startActivity(i)
        }
        poissonButton?.setOnClickListener {
            i = Intent(this, PoissonDistribution::class.java)
            startActivity(i)
        }
        bernoulliButton?.setOnClickListener {
            i = Intent(this, BernoulliDistribution::class.java)
            startActivity(i)
        }
        binomialButton?.setOnClickListener {
            i = Intent(this, BinomialDistribution::class.java)
            startActivity(i)
        }
    }

}