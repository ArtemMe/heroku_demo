package com.mezh.heroku_demo

import com.google.common.io.Files
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*

object StringLoader {

    fun fromClasspath(path: String?): String {

        val searchRoot = StringLoader::class.java
        val resourceUrl = Optional.ofNullable(searchRoot)
                .map { x -> x.getResource(path) }
                .orElseThrow { IllegalArgumentException(String.format("Resource %s not found", path)) }

        return Files.toString(File(resourceUrl.toURI()), StandardCharsets.UTF_8)
    }
}