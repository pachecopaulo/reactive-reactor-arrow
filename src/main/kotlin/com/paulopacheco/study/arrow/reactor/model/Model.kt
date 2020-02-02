package com.paulopacheco.study.arrow.reactor.model

import java.util.*

data class Item(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String
)