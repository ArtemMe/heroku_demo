package com.mezh.heroku_demo.services

import com.mezh.heroku_demo.handler.dto.Treatment
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class ChartServiceTest {

    @Autowired
    lateinit var chartService: ChartService

    @Test
    fun `should return valid statistic`() {

        val result = chartService.buildXYAxis(getTreatmentList())

        assert(result.first == listOf("1 January", "2 January"))
        assert(result.second == listOf(40, 10))
    }

    private fun getTreatmentList(): MutableList<Treatment?> {
        return mutableListOf(
                Treatment(1, 10, 10, LocalDateTime.of(1, 1, 1, 0,0)),
                Treatment(2, 10, 20, LocalDateTime.of(1, 1, 1, 0,0)),
                Treatment(3, 10, 30, LocalDateTime.of(1, 1, 1, 0,0)),
                Treatment(4, 10, 40, LocalDateTime.of(1, 1, 1, 0,0)),
                Treatment(1, 10, 10, LocalDateTime.of(1, 1, 2, 0,0)),
                Treatment(2, 10, 10, LocalDateTime.of(1, 1, 2, 0,0))
        )
    }


}