package com.packages.probabilitydistributions

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.ArrayMap
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlin.math.pow

private var probabilitySeekBar: SeekBar? = null
private var trailSeekBar: SeekBar? = null
private var probabilityRvSeekBar: SeekBar? = null
private var meanTextView: TextView? = null
private var standardDeviationTextView: TextView? = null
private var probabilityFunctionTextView: TextView? = null
private var probabilityTextDisplay: TextView? = null
private var trailTextDisplay: TextView? = null
private var probabilityRvTextDisplay: TextView? = null
private var probabilityRvText: TextView? = null
private var manualBook: ImageView? = null

private var probabilitySuccess: Double = 1.0
private var probabilityFailure: Double = 0.0
private var trail_n: Int = 1

@RequiresApi(Build.VERSION_CODES.KITKAT)
private var probabilitiesMap = ArrayMap<Int, Double>()

private var graph: BarChart? = null

@Suppress("DEPRECATION")
class BinomialDistribution : AppCompatActivity() {

    private var graphData: BarData? = null
    private var graphDataSet: BarDataSet? = null

    private val graphEntries: ArrayList<BarEntry> = ArrayList()
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back_to_home)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        title = "Binomial Distribution"
        setContentView(R.layout.activity_binomial_distribution)

        probabilitySeekBar = findViewById(R.id.probability_p_seek_bar)
        trailSeekBar = findViewById(R.id.trails_seek_bar)
        meanTextView = findViewById(R.id.mean_value)
        standardDeviationTextView = findViewById(R.id.standard_deviation_value)
        probabilityFunctionTextView = findViewById(R.id.rv_text)
        graph = findViewById(R.id.graph_view)
        probabilityTextDisplay = findViewById(R.id.p_text)
        trailTextDisplay = findViewById(R.id.n_text)
        probabilityRvSeekBar = findViewById(R.id.rv_seek_bar)
        probabilityRvTextDisplay = findViewById(R.id.probability_text)
        probabilityRvText = findViewById(R.id.probability_rv_text)

        probabilitySeekBar?.min = 0
        probabilitySeekBar?.max = 100
        probabilitySeekBar?.progress = 10

        probabilitySuccess = probabilitySeekBar?.progress!!.toDouble().div(100.0)
        probabilityFailure = (100 - probabilitySeekBar?.progress!!).toDouble().div(100.0)
        probabilityTextDisplay?.text = probabilitySuccess.toString()

        trailSeekBar?.min = 1
        trailSeekBar?.max = 10
        trailSeekBar?.progress = 1
        trail_n = trailSeekBar?.progress!!.toInt()
        trailTextDisplay?.text = trail_n.toString()

        meanTextView?.text = (trail_n * probabilitySuccess).toString()
        standardDeviationTextView?.text =
            String.format("%.5f", (trail_n * probabilitySuccess * probabilityFailure).pow(0.5))
        probabilityFunctionTextView?.text = "P ( X = 0 ) = " + String.format(
            "%.2f",
            probabilityFailure
        ) + "\nP ( X = 1 ) = " + String.format("%.2f", probabilitySuccess)

        fetchProbability()
        renderGraph()

        probabilityRvSeekBar?.min = 1
        probabilityRvSeekBar?.max = trailSeekBar!!.progress
        probabilityRvSeekBar?.progress=1
        probabilityRvText?.text = "Probability P( X = ${probabilityRvSeekBar?.progress} )"
        probabilityRvTextDisplay?.text = String.format("%.5f",probabilitiesMap[probabilityRvSeekBar?.progress])

        probabilityRvSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                probabilityRvText?.text = "Probability P( X = ${probabilityRvSeekBar?.progress} )"
                probabilityRvTextDisplay?.text = String.format("%.5f",probabilitiesMap[probabilityRvSeekBar?.progress])
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })



        manualBook = findViewById(R.id.manual_book)
        manualBook?.setOnClickListener {
            val i = Intent(this, ManualActivity::class.java)
            i.putExtra("manualType", "Binomial Distribution")
            startActivity(i)
        }



        probabilitySeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                probabilitySuccess = probabilitySeekBar?.progress!!.toDouble().div(100.0)
                probabilityFailure = (100 - probabilitySeekBar?.progress!!).toDouble().div(100.0)

                probabilityTextDisplay?.text = probabilitySuccess.toString()
                meanTextView?.text = String.format("%.2f", trail_n.toDouble() * probabilitySuccess)
                standardDeviationTextView?.text = String.format(
                    "%.5f",
                    (trail_n * probabilitySuccess * probabilityFailure).pow(0.5)
                )
                fetchProbability()
                renderGraph()

                probabilityRvSeekBar?.max = trailSeekBar!!.progress
                probabilityRvSeekBar?.progress=1
                probabilityRvText?.text = "Probability P( X = ${probabilityRvSeekBar?.progress} )"
                probabilityRvTextDisplay?.text = String.format("%.5f",probabilitiesMap[probabilityRvSeekBar?.progress])


            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        trailSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                trail_n = trailSeekBar?.progress!!.toInt()
                trailTextDisplay?.text = trail_n.toString()

                meanTextView?.text = String.format("%.2f", trail_n.toDouble() * probabilitySuccess)
                standardDeviationTextView?.text = String.format(
                    "%.5f",
                    (trail_n * probabilitySuccess * probabilityFailure).pow(0.5)
                )
                fetchProbability()
                renderGraph()

                probabilityRvSeekBar?.max = trailSeekBar!!.progress
                probabilityRvSeekBar?.progress=1
                probabilityRvText?.text = "Probability P( X = ${probabilityRvSeekBar?.progress} )"
                probabilityRvTextDisplay?.text = String.format("%.5f",probabilitiesMap[probabilityRvSeekBar?.progress])


            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

    }

    private fun fetchProbability() {
        var p: Double
        probabilitiesMap.clear()
        for (i in 1..trail_n) {
            p = calculateProbability(i).toDouble()
            probabilitiesMap[i] = p
        }
    }

    private fun renderGraph() {
        graphEntries.clear()

        for (i in 1..trail_n) {
            graphEntries.add(BarEntry(i.toFloat(), probabilitiesMap[i]!!.toFloat()) {})
        }

        graphDataSet = BarDataSet(graphEntries, "Probability P(X = x)")
        graphData = BarData(graphDataSet)
        graph?.data = graphData

        graph?.axisLeft?.labelCount = 10

        graph?.axisRight?.axisMaximum =
            2 * calculateProbability((trail_n * probabilitySuccess).toInt())
        graph?.axisRight?.axisMinimum = 0f

        graph?.axisLeft?.axisMinimum = 0.0000F
        graph?.axisLeft?.axisMaximum =
            2 * calculateProbability((trail_n * probabilitySuccess).toInt())

        graphDataSet?.setColors(resources.getColor(R.color.colorBarGraphTransparent))
        graphDataSet?.valueTextColor = Color.DKGRAY
        graphDataSet?.barBorderWidth = 1f
        graphDataSet?.barBorderColor = Color.LTGRAY
        graphDataSet?.valueTextSize = 13f
        graph?.xAxis?.labelCount = trail_n
        graph?.resetZoom()
    }

    private fun calculateProbability(i: Int): Float {
        val nCr = (fact(trail_n).toFloat() / (fact(i) * fact(trail_n - i)).toFloat())
        return nCr * probabilitySuccess.pow(i).toFloat() * probabilityFailure.pow(trail_n - i)
            .toFloat()
    }

    private fun fact(n: Int): Int {
        var res = 1
        for (i in 2..n) res *= i
        return res
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