package com.mezh.heroku_demo

import com.mezh.heroku_demo.entity.MessageDto

class TestData {
    companion object {
        fun getMsgs(): MutableList<MessageDto> {
            return mutableListOf(
                    MessageDto("1",100, "жим лежа", 1577840400),
                    MessageDto("1",100, "20:10", 1577841300),
                    MessageDto("1",100, "20:10", 1577842200),
                    MessageDto("1",100, "20:10", 1577843100),
                    MessageDto("1",100, "присед", 157784000),
                    MessageDto("1",100, "20:10", 1577844900),
                    MessageDto("1",100, "20:10", 1577845800),
                    MessageDto("1",100, "Подтягивания", 1577845800),
                    MessageDto("1",100, "10", 1577845800),
                    MessageDto("1",100, "8", 1577845800),
                    MessageDto("1",100, "6", 1577845800)
            )
        }

        fun getMsgsWithWrongExercises(): MutableList<MessageDto> {
            return mutableListOf(
                    MessageDto("1",100, "странное упражнение которого нет в списке", 1603119970),
                    MessageDto("1",100, "20:10", 1603119970),
                    MessageDto("1",100, "20:10", 1603119970),
                    MessageDto("1",100, "жим лежа", 1577840400),
                    MessageDto("1",100, "20:10", 1577841300),
                    MessageDto("1",100, "20:10", 1577842200),
                    MessageDto("1",100, "20:10", 1577843100),
                    MessageDto("1",100, "присед", 157784000),
                    MessageDto("1",100, "20:10", 1577844900),
                    MessageDto("1",100, "20:10", 1577845800)
            )
        }
    }
}