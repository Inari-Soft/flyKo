package com.inari.firefly.system.component

import com.inari.firefly.FFContext
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentBuilder
import com.inari.util.aspect.Aspect

abstract class SystemComponentBuilder<out C : SystemComponent> : ComponentBuilder<C>() {

    abstract val compAspect: Aspect

    val build: (C.() -> Unit) -> CompId = { configure ->
        val comp: C = createEmpty()
        comp.also(configure)
        comp.internalInit()
        FFContext.mapper<C>(compAspect).receiver()(comp)
        comp.componentId
    }

    val buildAndGet: (C.() -> Unit) -> C = { configure ->
        val comp: C = createEmpty()
        comp.also(configure)
        comp.internalInit()
        FFContext.mapper<C>(compAspect).receiver()(comp)
        comp
    }

    val buildAndActivate: (C.() -> Unit) -> CompId = { configure ->
        val comp: C = createEmpty()
        comp.also(configure)
        comp.internalInit()
        FFContext.mapper<C>(compAspect).receiver()(comp)
        FFContext.activate(comp.componentId)
        comp.componentId
    }

    val buildActivateAndGet: (C.() -> Unit) -> C = { configure ->
        val comp: C = createEmpty()
        comp.also(configure)
        comp.internalInit()
        FFContext.mapper<C>(compAspect).receiver()(comp)
        FFContext.activate(comp.componentId)
        comp
    }

}