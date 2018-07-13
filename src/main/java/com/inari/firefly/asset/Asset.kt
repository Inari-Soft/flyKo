package com.inari.firefly.asset

import com.inari.firefly.FFContext
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentType
import com.inari.util.indexed.Indexer

abstract class Asset protected constructor(): SystemComponent() {

    final override val indexer: Indexer =
        Indexer(Asset::class.java.name)

    @JvmField protected var dependingRef: Int = -1
    fun dependingIndex(): Int = dependingRef
    fun dependsOn(index: Int): Boolean = dependingRef == index

    fun instanceId(): Int = instanceId(0)
    abstract fun instanceId(index: Int): Int

    internal fun activate() = load()
    internal fun deactivate() = unload()

    protected abstract fun load()
    protected abstract fun unload()

    fun loaded():Boolean =
        FFContext.isActive(componentId)

    override fun componentType() =
        Asset.Companion

    companion object : SystemComponentType<Asset>(Asset::class.java)
}