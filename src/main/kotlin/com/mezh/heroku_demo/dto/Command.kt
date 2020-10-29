package com.mezh.heroku_demo.dto

enum class Command(val desc: String, val menuName: String) {
    START("/start", "Начать"),
    ADD_EXERCISES("/add_exercises", "Добавить упражнения"),
    STATISTIC("/stat", "Покажи статистику"),
    CHART("/chart", "Покажи график"),
    ADD_COMPLEX("/add_complex","Добавить комплекс упражнений"),
    MENU("/menu", "Покажи главное меню"),
    HELP("/help", "Помощь")
}