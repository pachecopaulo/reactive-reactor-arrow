package com.paulopacheco.study.arrow.reactor.repository

import arrow.fx.reactor.FluxK
import arrow.fx.reactor.MonoK
import arrow.fx.reactor.extensions.fluxk.monadThrow.monadThrow
import arrow.fx.reactor.extensions.monok.monadThrow.monadThrow
import arrow.fx.reactor.fix
import arrow.fx.reactor.k
import com.paulopacheco.study.arrow.reactor.model.ObjectAlreadyExistsException
import com.paulopacheco.study.arrow.reactor.model.ObjectNotFoundException
import org.springframework.dao.DuplicateKeyException

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Mono extension which throws an exception if an object isn't found
 */
fun <R> Mono<R>.errorIfEmpty(objectType: String, objectId: String): Mono<R> {
    return this.switchIfEmpty(Mono.defer { Mono.error<R>(
        ObjectNotFoundException(objectType = objectType, objectId = objectId))
    })
}

/**
 * Mono extensions to map supported standard mongo errors to our own domain
 */
fun <R> Mono<R>.mapToError(objectType: String, objectId: String = "undefined") = this.onErrorMap {
    when (it) {
        is DuplicateKeyException -> ObjectAlreadyExistsException(
                objectType = objectType,
                objectId = objectId,
                cause = it.cause)
        else -> it
    }
}

/**
 * Wrap a Mono<T> in a MonoK<T> using a bindingCatch. This prevents
 * errors bubbling up directly from the thunk to the onError subscriber, and allows
 * us to use monad comprehensions on the resulting MonoK<T>
 */
fun <T> asMono(thunk: () -> Mono<T>): MonoK<T> {
    return MonoK.monadThrow().fx.monadThrow {
        thunk().k().bind()
    }.fix()
}

/**
 * Wrap a Flux<T> in a FluxK<T> using a bindingCatch. This prevents
 * errors bubbling up directly from the thunk to the onError subscriber, and allows
 * us to use monad comprehensions on the resulting FluxK<T>
 */
fun <T> asFlux(thunk: () -> Flux<T>): FluxK<T> {
    return FluxK.monadThrow().fx.monadThrow {
        thunk().k().bind()
    }.fix()
}

