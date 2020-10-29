package com.mezh.heroku_demo.services

import com.mezh.heroku_demo.entity.MessageDto
import com.mezh.heroku_demo.entity.TrainingComplexEntity
import com.mezh.heroku_demo.handler.dto.Treatment
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class ExerciseService (
        val buttonService: ButtonService
) {
    fun collectTreatmentByExercises(exeMap: Map<String, MutableList<String>>, msgList: List<MessageDto>)
            : MutableMap<String, MutableList<Treatment?>> {
        var currentExe : String? = null
        var wrongExercise = false
        val exeTreatmentMap = mutableMapOf<String, MutableList<Treatment?>>()
        var treatmentCounter = 0

        for(m in msgList) {
            val text = m.message

            if (exeMap.containsKey(text)) {
                currentExe = text!!
                wrongExercise = false
                treatmentCounter++
                continue
            } else if (!isTreatmentRecord(text)) {
                wrongExercise = true
                continue
            }

            if (isTreatmentRecord(text) && currentExe != null && !wrongExercise) {
                val treatmentList = exeTreatmentMap[currentExe]

                if(treatmentList == null) {
                    exeTreatmentMap[currentExe] = mutableListOf(getTreatment(text!!, m.dateTime!!, SHIFT_NUMBER_COUNTER))
                } else {
                    treatmentList.add(getTreatment(text!!, m.dateTime!!, treatmentList.size + SHIFT_NUMBER_COUNTER))
                    exeTreatmentMap[currentExe] = treatmentList
                }

            }
        }

        return exeTreatmentMap
    }

    fun createListButtonsExercises(exercisesList: Set<TrainingComplexEntity>?) : InlineKeyboardMarkup? {
        if(exercisesList == null || exercisesList.isEmpty()) return null

        val allExercise = exercisesList.flatMap { it.exercises }.toSet()

        return buttonService.createListButtons(allExercise)
    }

    private fun getTreatment(str: String, unixTime: Int, treatmentNumber: Int): Treatment? {
        val strArr = str.split(":")

        if (strArr.size == 1) {
            return Treatment(treatmentNumber, strArr[0].toInt(), null, LocalDateTime.ofEpochSecond(unixTime.toLong(), 0, ZoneOffset.UTC))
        }

        if (strArr.size == 2) {
            return Treatment(treatmentNumber, strArr[1].toInt(), strArr[0].toInt(), LocalDateTime.ofEpochSecond(unixTime.toLong(), 0, ZoneOffset.UTC))
        }

        return null;
    }

    private fun isTreatmentRecord(text: String?) : Boolean {
        text ?: return false
        val regex = Regex(pattern = TREATMENT_PATTERN)
        val regexWithoutWeight = Regex(pattern = TREATMENT_PATTERN_WITHOUT_WEIGHT)
        return regex.containsMatchIn(text) || regexWithoutWeight.containsMatchIn(text)
    }

    companion object {
        const val TREATMENT_PATTERN = "^\\d+:\\d+\$"
        const val TREATMENT_PATTERN_WITHOUT_WEIGHT = "^\\d+$"
        const val SHIFT_NUMBER_COUNTER = 1
    }
}