package com.paulopacheco.study.arrow.reactor.api

import arrow.fx.reactor.FluxK
import arrow.fx.reactor.MonoK
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

inline fun <reified T>okJson(p: () -> MonoK<T>) = ServerResponse.ok()
    .contentType(MediaType.APPLICATION_JSON)
    .body(BodyInserters.fromPublisher(p().mono, T::class.java))

inline fun <reified T>okJsonFlux(p: () -> FluxK<T>) = ServerResponse.ok()
    .contentType(MediaType.APPLICATION_JSON)
    .body(BodyInserters.fromPublisher(p().flux, T::class.java))

inline fun <reified T>okJsonList(p: () -> MonoK<List<T>>) : Mono<ServerResponse> {
    val pType = object: ParameterizedTypeReference<List<T>>() {}
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
        BodyInserters.fromPublisher(
            p().mono,
            pType
        )
    )
}