package com.mezh.heroku_demo.handler

import com.mezh.heroku_demo.dto.Command
import com.mezh.heroku_demo.handler.dto.CommandContext
import com.mezh.heroku_demo.services.QuickChartService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class ChartCommandHandler (
        private val quickChartService: QuickChartService
) : CommandHadler {

    override fun handle(context: CommandContext): SendMessage {
        return handleInternal(context.message)
    }

    private fun handleInternal(message: Message?): SendMessage {

      return SendMessage()
              .setChatId(message?.chatId)
              .setText(quickChartService.createChart(listOf("1", "2","3"), listOf(1,2,3,4,5), "testChart-1"))
              .enableHtml(true)
    }

    override fun getType(): Command {
        return Command.CHART
    }
}