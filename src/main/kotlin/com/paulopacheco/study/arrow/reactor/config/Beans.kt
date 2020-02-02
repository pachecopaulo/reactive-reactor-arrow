package com.paulopacheco.study.arrow.reactor.config

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.boot.autoconfigure.web.ErrorProperties
import org.springframework.boot.autoconfigure.web.ResourceProperties
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import org.springframework.context.support.beans
import org.springframework.http.codec.support.DefaultServerCodecConfigurer
import org.springframework.web.reactive.function.server.ServerRequest
import com.mongodb.MongoClientSettings
import com.mongodb.ServerAddress
import com.paulopacheco.study.arrow.reactor.api.mappings
import com.paulopacheco.study.arrow.reactor.api.registerExceptions
import com.paulopacheco.study.arrow.reactor.api.routes.ItemRoutes
import com.paulopacheco.study.arrow.reactor.api.routes.ItemsRouteHandler
import com.paulopacheco.study.arrow.reactor.config.Properties.DATABASE_HOST
import com.paulopacheco.study.arrow.reactor.config.Properties.DATABASE_NAME
import com.paulopacheco.study.arrow.reactor.config.Properties.DATABASE_PORT
import com.paulopacheco.study.arrow.reactor.repository.ItemRepository
import com.paulopacheco.study.arrow.reactor.service.ItemService

fun allBeans() = beans {
    routeBeans(this)
    filterBeans(this)
    serviceBeans(this)
    repoBeans(this)
    errorHandlerBeans(this)
}

fun filterBeans(ctx: BeanDefinitionDsl) = with (ctx) {
    bean { CorsWebFilter { CorsConfiguration().applyPermitDefaultValues() } }
}

fun routeBeans(ctx: BeanDefinitionDsl) = with (ctx) {
    bean(WebHttpHandlerBuilder.WEB_HANDLER_BEAN_NAME) {
        RouterFunctions.toWebHandler(ref<ItemRoutes>().route, HandlerStrategies.withDefaults())
    }

    bean<ItemRoutes>()
    bean<ItemsRouteHandler>()
}

fun serviceBeans(ctx: BeanDefinitionDsl) = with (ctx) {
    bean<ItemService>()
}

fun repoBeans(ctx: BeanDefinitionDsl) = with (ctx) {
    bean { ReactiveMongoTemplate(ref(), DATABASE_NAME) }
    bean<MongoClient> { MongoClients
        .create(
            MongoClientSettings.builder()
                .applyToClusterSettings {it.hosts(listOf(ServerAddress(
                    DATABASE_HOST,
                    DATABASE_PORT
                )))}
                .build())
    }
    bean<ItemRepository>()
}

fun errorHandlerBeans(ctx: BeanDefinitionDsl) = with (ctx) {
    bean<ErrorWebFluxAutoConfiguration>()
    bean<ErrorWebExceptionHandler> {
        ref<ErrorWebFluxAutoConfiguration>().errorWebExceptionHandler(ref<DefaultErrorAttributes>())
    }

    bean<DefaultErrorAttributes> {
        registerExceptions()
        object: DefaultErrorAttributes() {
             override fun getError(request: ServerRequest?): Throwable {
                 val originalError = super.getError(request)
                 val originalErrorClass = originalError.javaClass

                 return mappings[originalErrorClass]?.invoke(originalError) ?: originalError
            }
        }
    }
    bean<ResourceProperties>()
    bean<ErrorProperties>()
    bean<ServerProperties>()
    bean<DefaultServerCodecConfigurer>()
}
