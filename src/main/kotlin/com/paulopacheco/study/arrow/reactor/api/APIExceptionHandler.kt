package com.paulopacheco.study.arrow.reactor.api

import com.paulopacheco.study.arrow.reactor.model.ObjectAlreadyExistsException
import com.paulopacheco.study.arrow.reactor.model.ObjectNotFoundException
import com.paulopacheco.study.arrow.reactor.model.PropertyValidationException
import org.springframework.http.HttpStatus.*
import org.springframework.web.server.ResponseStatusException

val mappings: MutableMap<Class<*>, (Throwable) -> ResponseStatusException> = mutableMapOf()

fun registerMapping(p: Class<*>, mapTo: (Throwable) -> ResponseStatusException) = mappings.put(p, mapTo)

fun registerExceptions() {
    registerMapping(PropertyValidationException::class.java) { t ->
        ResponseStatusException(BAD_REQUEST, t.message, t)
    }
    registerMapping(ObjectAlreadyExistsException::class.java) { t ->
        ResponseStatusException(CONFLICT, t.message, t)
    }
    registerMapping(ObjectNotFoundException::class.java) { t ->
        ResponseStatusException(NOT_FOUND, t.message, t)
    }
}


