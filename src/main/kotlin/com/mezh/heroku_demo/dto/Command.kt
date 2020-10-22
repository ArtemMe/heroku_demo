package com.mezh.heroku_demo.dto

enum class Command(val desc: String) {
    START("/start"),
    ADD_EXERCISES("/add_exercises"),
    STATISTIC("/statistic"),
    CHART("/chart");
}