package com.paulopacheco.study.arrow.reactor.repository

import arrow.fx.reactor.MonoK
import com.paulopacheco.study.arrow.reactor.model.Item
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import java.util.*

private const val ITEM_COLLECTION_NAME = "items"

class ItemRepository(private val mongoTemplate: ReactiveMongoTemplate) : ReactiveMongoOperations by mongoTemplate {

    fun getItem(itemId: UUID): MonoK<Item> = asMono {
        findById(
            itemId,
            Item::class.java,
            ITEM_COLLECTION_NAME
        )
            .errorIfEmpty(ITEM_COLLECTION_NAME, itemId.toString())
    }

    fun getAllItems(): MonoK<List<Item>> = asMono {
        findAll(
            Item::class.java,
            ITEM_COLLECTION_NAME
        )
            .collectList()
    }

    fun storeItem(order: Item): MonoK<Item> = asMono {
        insert(order, ITEM_COLLECTION_NAME)
            .mapToError(ITEM_COLLECTION_NAME, order.id.toString())
    }
}

