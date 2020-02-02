package com.paulopacheco.study.arrow.reactor.model

data class PropertyValidationException(
    val invalidProperty: String,
    val invalidValue: String? = null,
    override val message: String? = null,
    override val cause: Throwable? = null
) : Throwable()

data class ObjectAlreadyExistsException(
    val objectType: String,
    val objectId: String = "",
    override val message: String? = "Object of type: $objectType, already exists for value: $objectId",
    override val cause: Throwable?
) : Throwable()

data class ObjectNotFoundException(
    val objectType: String,
    val objectId: String = "",
    override val message: String? = "Object of type: $objectType, not found for value: $objectId",
    override val cause: Throwable? = null
) : Throwable()