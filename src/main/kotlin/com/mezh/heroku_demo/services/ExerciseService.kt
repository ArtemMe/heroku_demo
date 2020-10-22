package com.mezh.heroku_demo.services

import com.mezh.heroku_demo.entity.MessageDto
import com.mezh.heroku_demo.handler.dto.Treatment
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class ExerciseService {
    fun collectTreatmentByExercises(exeMap: Map<String, MutableList<String>>, msgList: List<MessageDto>)
            : MutableMap<String, MutableList<Treatment?>> {
        var currentExe : String? = null
        var wrongExercise = false
        val exeTreatmentMap = mutableMapOf<String, MutableList<Treatment?>>()

        for(m in msgList) {
            val text = m.message

            if (exeMap.containsKey(text)) {
                currentExe = text!!
                wrongExercise = false
                continue
            } else if (!isTreatmentRecord(text)) {
                wrongExercise = true
                continue
            }

            if (isTreatmentRecord(text) && currentExe != null && !wrongExercise) {
                exeTreatmentMap
                        .getOrPut(currentExe, { mutableListOf(getTreatment(text!!, m.dateTime!!)) })
                        .add(getTreatment(text!!, m.dateTime!!))
            }
        }

        return exeTreatmentMap
    }

    private fun getTreatment(str: String, unixTime: Int): Treatment? {
        val strArr = str.split(":")

        if (strArr.size == 2) {
            return Treatment(strArr[0].toInt(), strArr[1].toInt(), null, LocalDateTime.ofEpochSecond(unixTime.toLong(), 0, ZoneOffset.UTC))
        }

        if (strArr.size == 3) {
            return Treatment(strArr[0].toInt(), strArr[1].toInt(), strArr[2].toInt(), LocalDateTime.ofEpochSecond(unixTime.toLong(), 0, ZoneOffset.UTC))
        }

        return null;
    }

    private fun isTreatmentRecord(text: String?) : Boolean {
        text ?: return false
        val regex = Regex(pattern = "\\d*:\\d*:\\d*")
        return regex.containsMatchIn(input = text)
    }
}