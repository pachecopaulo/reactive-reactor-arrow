package com.paulopacheco.study.arrow.reactor.api

import arrow.fx.reactor.MonoK
import arrow.fx.reactor.k
import com.paulopacheco.study.arrow.reactor.model.PropertyValidationException
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono
import java.util.*

fun getPathParam(request: ServerRequest, param: String): MonoK<UUID> = Mono
    .just(request.pathVariable(param))
    .map { UUID.fromString(it) }
    .onErrorMap { t -> PropertyValidationException(
        invalidProperty = param,
        cause = t,
        message = "Invalid Parameter $param")
    }.k()