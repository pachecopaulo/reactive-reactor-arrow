package com.paulopacheco.study.arrow.reactor.api.routes

import arrow.fx.reactor.MonoK
import arrow.fx.reactor.extensions.fx
import arrow.fx.reactor.fix
import arrow.fx.reactor.k
import com.paulopacheco.study.arrow.reactor.api.getPathParam
import com.paulopacheco.study.arrow.reactor.api.okJson
import com.paulopacheco.study.arrow.reactor.api.okJsonList
import com.paulopacheco.study.arrow.reactor.config.Properties.BASE_ROUTE
import com.paulopacheco.study.arrow.reactor.model.Item
import com.paulopacheco.study.arrow.reactor.service.ItemService
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

class ItemRoutes(itemsRouteHandler: ItemsRouteHandler) {

    val route: RouterFunction<ServerResponse> = router {

        (path("$BASE_ROUTE/items") and accept(APPLICATION_JSON)).nest {
            POST("", itemsRouteHandler::createOrder)
            GET("", itemsRouteHandler::getAllOrders)
            GET("/{id}", itemsRouteHandler::getOrder)
        }
    }
}

class ItemsRouteHandler(private val itemService: ItemService) {

    fun getAllOrders(request: ServerRequest): Mono<ServerResponse> = okJsonList {
        itemService.getAllItems().fix()
    }

    fun getOrder(request: ServerRequest): Mono<ServerResponse> = okJson {
        MonoK.fx {
            val uuid = getPathParam(request, "id").bind()
            itemService.getItem(uuid).bind()
        }
    }

    fun createOrder(request: ServerRequest): Mono<ServerResponse> = okJson {
        MonoK.fx {
            val item = request.bodyToMono<Item>().k().bind()
            itemService.createItem(item).bind()
        }
    }
}



