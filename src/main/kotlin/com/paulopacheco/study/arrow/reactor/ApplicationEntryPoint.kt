package com.paulopacheco.study.arrow.reactor

import com.paulopacheco.study.arrow.reactor.config.Application

/**
 * Starts the service, and waits until shutdown signal is received.
 */
fun main(args: Array<String>) {
    Application().startAndAwait()
}