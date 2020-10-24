package com.mezh.heroku_demo.handler.dto

import com.mezh.heroku_demo.entity.StateData

class ComplexDto : StateData {
    var name: String? = null
    var exeList: Set<String>? = null
}