package com.packages.probabilitydistributions

import android.annotation.SuppressLint
import androidx.annotation.RequiresApi
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.textfield.TextInputEditText

@Suppress("DEPRECATION")
class UniformDistribution : AppCompatActivity() {

    private var randomVariableSeekBar: SeekBar? = null
    private var randomVariableMinimumTextView: TextInputEditText? = null
    private var randomVariableMaximumTextView: TextInputEditText? = null
    private var randomVariableProbabilityTextView: TextView? = null
    private var meanTextView: TextView? = null
    private var standardDeviationTextView: TextView? = null
    private var manualBookImageView: ImageView? = null
    private var graphBarChart: BarChart? = null

    private var mean = 0.0
    private var standardDeviation = 0.0
    private var randomVariableProbability: Double = 0.0

    private var graphDataSet: BarDataSet? = null
    private val graphEntries: ArrayList<BarEntry> = ArrayList()

    private var actionBar: ActionBar? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uniform_distribution)

        actionBar = supportActionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back_to_home)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        title = "Uniform Distribution"

// fetching widgets
        randomVariableSeekBar = findViewById(R.id.uniform_random_variable_seek_bar)

        randomVariableMinimumTextView = findViewById(R.id.uniform_random_variable_minimum)
        randomVariableMaximumTextView = findViewById(R.id.uniform_random_variable_maximum)
        randomVariableProbabilityTextView = findViewById(R.id.uniform_random_variable_probability)

        standardDeviationTextView = findViewById(R.id.uniform_distribution_standard_deviation)
        meanTextView = findViewById(R.id.uniform_distribution_mean)

        manualBookImageView = findViewById(R.id.uniform_distribution_manual_book)
        graphBarChart = findViewById(R.id.uniform_distribution_graph_view)

// setting up listeners
        manualBookImageView?.setOnClickListener {
            val manualBookIntent = Intent(this, ManualActivity::class.java)
            manualBookIntent.putExtra("manualType", "Uniform Distribution")
            startActivity(manualBookIntent)
        }

        randomVariableMinimumTextView?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    randomVariableProbabilityTextView?.text =
                        "P( X = ${randomVariableSeekBar?.progress} ) : Invalid Input"
                } else {
                    randomVariableSeekBar?.min = Integer.parseInt(p0.toString())
                    deriveValues()
                    renderGraph()
                }
            }
        })

        randomVariableMaximumTextView?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    randomVariableProbabilityTextView?.text =
                        "P( X = ${randomVariableSeekBar?.progress} ) : Invalid Input"
                } else {
                    randomVariableSeekBar?.max = Integer.parseInt(p0.toString()) - 1
                    deriveValues()
                    renderGraph()
                }
            }
        })

        randomVariableSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                randomVariableProbabilityTextView?.text =
                    "P( X = ${randomVariableSeekBar?.progress} ) : " + String.format(
                        "%.5f",
                        randomVariableProbability
                    )
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

// setting up initial states
        initializeValues()
        deriveValues()
        renderGraph()
    }

    // functions
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeValues() {
        randomVariableSeekBar?.min = 10
        randomVariableSeekBar?.max = 19
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun deriveValues() {
        val minRV = randomVariableSeekBar?.min!!
        val maxRV = randomVariableSeekBar?.max!!

        randomVariableProbability = (1.0000 / (maxRV - minRV + 1))

        val randomVariable = randomVariableSeekBar?.progress
        val probability = String.format("%.5f", randomVariableProbability)
        val probabilityText = "P( X = $randomVariable ) : $probability"

        mean = (maxRV + minRV + 1).toDouble() / 2
        meanTextView?.text = mean.toString()

        standardDeviation = String.format("%.5f", (maxRV - minRV + 1) / 3.46410162).toDouble()
        standardDeviationTextView?.text = standardDeviation.toString()

        randomVariableProbabilityTextView?.text = probabilityText
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderGraph() {
        val minRV = randomVariableSeekBar?.min!!
        val maxRV = randomVariableSeekBar?.max!!

        graphEntries.clear()

        for (i in minRV..maxRV) {
            graphEntries.add(object : BarEntry(i.toFloat(), randomVariableProbability.toFloat()) {})
        }

        graphDataSet = BarDataSet(graphEntries, "Probability P(X = x)")
        graphDataSet?.setColors(resources.getColor(R.color.colorBarGraphTransparent))
        graphDataSet?.valueTextColor = Color.DKGRAY
        graphDataSet?.barBorderWidth = 1f
        graphDataSet?.barBorderColor = Color.LTGRAY
        graphDataSet?.valueTextSize = 13f

        graphBarChart?.data = BarData(graphDataSet)
        graphBarChart?.axisRight?.labelCount = 0
        graphBarChart?.axisLeft?.axisMinimum = 0.0000F
        graphBarChart?.axisLeft?.axisMaximum = 2 * randomVariableProbability.toFloat()
        graphBarChart?.xAxis?.labelCount = maxRV - minRV + 1

        graphBarChart?.resetZoom()

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