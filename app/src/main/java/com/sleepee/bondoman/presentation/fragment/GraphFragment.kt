package com.sleepee.bondoman.presentation.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.sleepee.bondoman.R
import com.sleepee.bondoman.databinding.FragmentGraphBinding

class GraphFragment: Fragment() {
    private lateinit var binding: FragmentGraphBinding
    private lateinit var pieChart: PieChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGraphBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pieChart = binding.pieChart

        // Set user percent value
        pieChart.setUsePercentValues(true)
        // Set description as enabled
        pieChart.getDescription().setEnabled(false)
        // Set offset for pie chart
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // Set drag for pie chart
        pieChart.setDragDecelerationFrictionCoef(0.95f)

        // Disable hole
        pieChart.setDrawHoleEnabled(false)

        // Set rotation for pie chart
        pieChart.setRotationAngle(0f)

        // Enable rotation of pieChart by touch
        pieChart.setRotationEnabled(true)
        pieChart.setHighlightPerTapEnabled(true)

        // Sett animation for pie chart
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        // Disabling legend for pie chart
        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)

        // Dummy data
        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(70f))
        entries.add(PieEntry(30f))

        // Sett pie data set
        val dataSet = PieDataSet(entries, "Mobile OS")

        // Set icons.
        dataSet.setDrawIcons(false)

        // Setting slice for pie
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // Add colors to list
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.green))
        colors.add(resources.getColor(R.color.red))

        // Sett colors.
        dataSet.colors = colors

        // Sett pie data set
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart.setData(data)

        // Undo all highlights
        pieChart.highlightValues(null)

        // Loading chart
        pieChart.invalidate()
    }
}