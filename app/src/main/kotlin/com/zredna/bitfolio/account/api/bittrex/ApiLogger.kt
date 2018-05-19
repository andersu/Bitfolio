package com.zredna.bittrex.apiclient

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.logging.HttpLoggingInterceptor
import java.util.logging.Level
import java.util.logging.Logger

class ApiLogger : HttpLoggingInterceptor.Logger {
    private val logger = Logger.getLogger(ApiLogger::class.java.name)

    override fun log(message: String) {
        if (message.startsWith("{") || message.startsWith("[")) {
            try {
                val prettyPrintJson = GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .toJson(JsonParser().parse(message))
                logger.log(Level.INFO, prettyPrintJson)
            } catch (m: JsonSyntaxException) {
                logger.log(Level.INFO, message)
            }
        } else {
            logger.log(Level.INFO, message)
        }
    }
}