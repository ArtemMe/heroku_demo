package com.mezh.heroku_demo.services

import com.mezh.heroku_demo.handler.dto.Treatment
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

@Service
class ChartService {

    fun buildXYAxis(treatmentList: MutableList<Treatment?>) : Pair<MutableList<String>, MutableList<Int>> {
        var currentTreatment: Treatment? = null
        var first = true
        var maxWeight = 0
        val resultXY: Pair<MutableList<String>, MutableList<Int>> = Pair(mutableListOf(), mutableListOf())
        var counter = 0

        for(nextTreatment in treatmentList.filterNotNull()) {
            if(first) {
                currentTreatment = nextTreatment
                first = false
                counter++
                continue
            }//todo  попрвить и для упражнений без веса
            if(nextTreatment.dateTime.dayOfMonth - currentTreatment!!.dateTime.dayOfMonth == 0) {
                if(nextTreatment.weight!! > currentTreatment.weight!!) maxWeight = nextTreatment.weight
                else maxWeight = currentTreatment.weight!!
            } else {
                resultXY.first.add(createSpelloutDate(currentTreatment.dateTime))
                resultXY.second.add(maxWeight)
            }

            if(counter+1 == treatmentList.size) {
                resultXY.first.add(createSpelloutDate(nextTreatment.dateTime))
                resultXY.second.add(nextTreatment.weight!!)
            }

            currentTreatment = nextTreatment
            counter++
        }

        return resultXY
    }

    private fun createSpelloutDate(date: LocalDateTime) : String {
        return date.dayOfMonth.toString() + " " +date.month.getDisplayName(TextStyle.FULL, Locale.US)
    }
}