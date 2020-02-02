package com.paulopacheco.study.arrow.reactor.config

import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config

/**
 * Exposes the configuration in the application.conf file.
 */
object Properties {
    private val config: Config = ConfigFactory.load()

    // webserver settings
    val SERVER_PORT = config.getInt("server.port")
    val SERVER_SHUTDOWN_TIMEOUT = config.getLong("server.shutdown-timeout")

    // Database settings
    val DATABASE_NAME: String = config.getString("store.database-name")
    val DATABASE_HOST: String = config.getString("store.database-host")
    val DATABASE_PORT: Int = config.getInt("store.database-port")

    // rest stuff
    val BASE_ROUTE: String = config.getString("route.base-route")
}
