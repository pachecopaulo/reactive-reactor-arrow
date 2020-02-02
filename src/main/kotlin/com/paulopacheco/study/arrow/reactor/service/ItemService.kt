package com.paulopacheco.study.arrow.reactor.service

import arrow.fx.reactor.MonoK
import arrow.fx.reactor.extensions.fx
import com.paulopacheco.study.arrow.reactor.model.Item
import com.paulopacheco.study.arrow.reactor.repository.ItemRepository
import java.util.*

class ItemService(private val itemRepository: ItemRepository) {

    fun updateItemIExistsAndReturnAllItems(toUpdate: UUID, description: String): MonoK<List<Item>> = MonoK.fx {
        val existingItem = itemRepository.getItem(toUpdate).bind()
        val updatedItem = existingItem.copy(description = description)
        itemRepository.storeItem(updatedItem).bind()

        itemRepository.getAllItems().bind()
    }

    fun getAllItems(): MonoK<List<Item>> = itemRepository.getAllItems()
    fun createItem(item: Item): MonoK<Item> = itemRepository.storeItem(item)
    fun getItem(itemId: UUID): MonoK<Item> = itemRepository.getItem(itemId)
}