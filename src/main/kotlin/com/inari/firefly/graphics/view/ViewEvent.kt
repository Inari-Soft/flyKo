package com.inari.firefly.graphics.view

import com.inari.firefly.component.CompId
import com.inari.firefly.FFContext
import com.inari.firefly.external.ViewData
import com.inari.util.event.Event

object ViewEvent : Event<ViewEvent.Listener>(EVENT_ASPECTS.createAspect("ViewEvent")) {

    enum class Type {
        VIEW_CREATED,
        VIEW_ACTIVATED,
        VIEW_DISPOSED,
        VIEW_DELETED
    }

    private lateinit var id: CompId
    private lateinit var data: ViewData
    private lateinit var type: Type

    override fun notify(listener: Listener) =
        listener(id, data, type)

    internal fun send(id: CompId, data: ViewData, type: Type) {
        this.id = id
        this.data = data
        this.type = type
        FFContext.notify(this)
    }

    interface Listener {
        operator fun invoke(id: CompId, viewPort: ViewData, type: Type)
    }
}