package com.packages.probabilitydistributions

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.ArrayMap
import android.view.MenuItem
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


@Suppress("DEPRECATION")
@RequiresApi(Build.VERSION_CODES.KITKAT)
@SuppressLint("SetTextI18n")
class PoissonDistribution : AppCompatActivity() {

    private var randomVariableSeekBar: SeekBar? = null
    private var meanValueSeekBar: SeekBar? = null
    private var randomVariableProbabilityTextView: TextView? = null
    private var standardDeviationTextView: TextView? = null
    private var meanTextView: TextView? = null
    private var manualBookImageView: ImageView? = null
    private var graphBarChart: BarChart? = null

    private var mean = 10.00
    private var standardDeviation = 0.00
    private var e = 2.7182818285

    private var graphData: BarData? = null
    private var graphDataSet: BarDataSet? = null

    private val graphEntries: ArrayList<BarEntry> = ArrayList()
    private var randomVariableProbabilityMap = ArrayMap<Int, Double>()

    private var actionBar: ActionBar? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poisson_distribution)

        actionBar = supportActionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back_to_home)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        title = "Poisson Distribution"

// fetching widgets
        randomVariableSeekBar = findViewById(R.id.random_variable)
        meanValueSeekBar = findViewById(R.id.mean_value_seek_bar)

        meanTextView = findViewById(R.id.poisson_distribution_mean)
        standardDeviationTextView = findViewById(R.id.poisson_distribution_standard_deviation)

        randomVariableProbabilityTextView = findViewById(R.id.poisson_random_variable_probability)

        graphBarChart = findViewById(R.id.poisson_distribution_graph_view)
        manualBookImageView = findViewById(R.id.manual_book)

// setting up listeners
        manualBookImageView?.setOnClickListener {
            val manualBookIntent = Intent(this, ManualActivity::class.java)
            manualBookIntent.putExtra("manualType", "Poisson Distribution")
            startActivity(manualBookIntent)
        }

        meanValueSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                deriveValues()
                fetchProbabilities()
                setParameters()
                setProbabilityText()
                renderGraph()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        randomVariableSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {

                setProbabilityText()

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })


        initializeValues()

        deriveValues()
        fetchProbabilities()
        setParameters()
        setProbabilityText()
        renderGraph()


    }

    private fun setProbabilityText() {
        randomVariableProbabilityTextView?.text =
            "P(X = ${randomVariableSeekBar?.progress}) : \n" + String.format(
                "%.10f",
                randomVariableProbabilityMap[randomVariableSeekBar?.progress]
            )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setParameters() {
        randomVariableSeekBar?.max = randomVariableProbabilityMap.size - 1
        randomVariableSeekBar?.min = 0
        randomVariableSeekBar?.progress = 0
    }


    private fun deriveValues() {
        meanTextView?.text = meanValueSeekBar?.progress.toString()
        mean = meanValueSeekBar?.progress!!.toDouble()

        standardDeviation = mean.pow(0.5)
        standardDeviationTextView?.text = standardDeviation.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeValues() {
        meanValueSeekBar?.min = 0
        meanValueSeekBar?.max = 200
        meanValueSeekBar?.progress = mean.toInt()

        standardDeviation = mean.pow(0.5)
        standardDeviationTextView?.text = standardDeviation.toString()

    }

    private fun renderGraph() {
        graphEntries.clear()
        for (i in 0 until 250) {
            if (i < randomVariableProbabilityMap.size) {
                graphEntries.add(
                    BarEntry(
                        i.toFloat(),
                        randomVariableProbabilityMap[i]!!.toFloat()
                    ) {})
            } else {
                graphEntries.add(BarEntry(i.toFloat(), 0.0f) {})
            }
        }
        graphDataSet = BarDataSet(graphEntries, "Probability P(X = x)")
        graphData = BarData(graphDataSet)
        graphBarChart?.data = graphData
        graphBarChart?.axisRight?.labelCount = 1

        graphBarChart?.axisRight?.axisMaximum =
            2 * randomVariableProbabilityMap[mean.toInt()]!!.toFloat()
        graphBarChart?.axisRight?.axisMinimum = 0f

        graphBarChart?.axisLeft?.axisMinimum = 0.0000F
        graphBarChart?.axisLeft?.axisMaximum =
            2 * randomVariableProbabilityMap[mean.toInt()]!!.toFloat()


        graphDataSet?.setColors(resources.getColor(R.color.colorBarGraphTransparent))
        graphDataSet?.valueTextColor = Color.DKGRAY
        graphDataSet?.barBorderWidth = 1f
        graphDataSet?.barBorderColor = Color.LTGRAY
        graphDataSet?.valueTextSize = 13f
        graphBarChart?.xAxis?.labelCount = randomVariableProbabilityMap.size
        graphBarChart?.resetZoom()
    }

    private fun fetchProbabilities() {
        randomVariableProbabilityMap.clear()
        var probability = e.pow(-1 * mean)
        randomVariableProbabilityMap[0] = probability

        var i = 1

        val minProb = 0.1 / (10.0).pow(100.0)
        while (probability > minProb) {
            probability = probability * mean / i
            randomVariableProbabilityMap[i] = probability
            i++
        }

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