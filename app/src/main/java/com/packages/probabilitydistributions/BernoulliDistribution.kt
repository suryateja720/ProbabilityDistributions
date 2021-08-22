package com.packages.probabilitydistributions

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt

private var probabilitySeekBar: SeekBar? = null
private var meanTextView: TextView? = null
private var standardDeviationTextView: TextView? = null
private var probabilityFunctionTextView: TextView? = null
private var probabilityTextDisplay: TextView? = null
private var manualBook: ImageView? = null

private var probabilitySuccess: Double = 1.0
private var probabilityFailure: Double = 0.0

private var graph: BarChart? = null

class BernoulliDistribution : AppCompatActivity() {

    private var graphData: BarData? = null
    private var graphDataSet: BarDataSet? = null

    private val graphEntries: ArrayList<BarEntry> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back_to_home)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        title = "Bernoulli Distribution"
        setContentView(R.layout.activity_bernoulli_distribution)

        probabilitySeekBar = findViewById(R.id.probability_p_seek_bar)
        meanTextView = findViewById(R.id.mean_value)
        standardDeviationTextView = findViewById(R.id.standard_deviation_value)
        probabilityFunctionTextView = findViewById(R.id.rv_text)
        graph = findViewById(R.id.graph_view)
        probabilityTextDisplay = findViewById(R.id.p_text)

        probabilitySeekBar?.min = 0
        probabilitySeekBar?.max = 100
        probabilitySeekBar?.progress = 10

        probabilitySuccess = probabilitySeekBar?.progress!!.toDouble().div(100.0)
        probabilityFailure = (100 - probabilitySeekBar?.progress!!).toDouble().div(100.0)

        probabilityTextDisplay?.text = probabilitySuccess.toString()
        meanTextView?.text = probabilitySuccess.toString()
        standardDeviationTextView?.text = String.format("%.5f",(probabilitySuccess*probabilityFailure).pow(0.5))
        probabilityFunctionTextView?.text = "P ( X = 0 ) = "+ String.format("%.2f",probabilityFailure)+"\nP ( X = 1 ) = "+ String.format("%.2f", probabilitySuccess)


        renderGraph()

        manualBook = findViewById(R.id.manual_book)
        manualBook?.setOnClickListener {
            val i = Intent(this, ManualActivity::class.java)
            i.putExtra("manualType", "Bernoulli Distribution")
            startActivity(i)
        }
        probabilitySeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                probabilitySuccess = probabilitySeekBar?.progress!!.toDouble().div(100.0)
                probabilityFailure = (100 - probabilitySeekBar?.progress!!).toDouble().div(100.0)

                probabilityTextDisplay?.text = probabilitySuccess.toString()
                meanTextView?.text = probabilitySuccess.toString()
                standardDeviationTextView?.text = String.format("%.5f",(probabilitySuccess*probabilityFailure).pow(0.5))
                probabilityFunctionTextView?.text = "P ( X = 0 ) = "+ String.format("%.2f",probabilityFailure)+"\nP ( X = 1 ) = "+ String.format("%.2f", probabilitySuccess)
                renderGraph()

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

    }

    private fun renderGraph() {
        graphEntries.clear()

        graphEntries.add(BarEntry(0f, probabilityFailure.toFloat()) {})
        graphEntries.add(BarEntry(1f, probabilitySuccess.toFloat()) {})

        graphDataSet = BarDataSet(graphEntries, "Probability P(X = x)")
        graphData = BarData(graphDataSet)
        graph?.data = graphData

        graph?.axisLeft?.labelCount = 10

        graph?.axisLeft?.axisMinimum = 0f
        graph?.axisLeft?.axisMaximum = 1f


        graphDataSet?.setColors(resources.getColor(R.color.colorBarGraphTransparent))
        graphDataSet?.valueTextColor = Color.DKGRAY
        graphDataSet?.barBorderWidth = 1f
        graphDataSet?.barBorderColor = Color.LTGRAY
        graphDataSet?.valueTextSize = 13f
        graph?.xAxis?.labelCount = 2
        graph?.resetZoom()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}