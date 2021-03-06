package com.inari.firefly.asset

import com.inari.firefly.component.CompId
import com.inari.firefly.FFContext
import com.inari.util.event.Event

object AssetEvent : Event<AssetEvent.Listener>(EVENT_ASPECTS.createAspect(AssetEvent::class.simpleName!!)) {

    enum class Type {
        ASSET_CREATED,
        ASSET_LOADED,
        ASSET_DISPOSED,
        ASSET_DELETED
    }

    private lateinit var assetId: CompId
    private lateinit var type: Type

    override fun notify(listener: Listener) =
        listener(assetId, type)

    fun send(id: CompId, type: Type) {
        assetId = id
        this.type = type
        FFContext.notify(this)
    }

    interface Listener {
        operator fun invoke(viewId: CompId, type: Type)
    }

}