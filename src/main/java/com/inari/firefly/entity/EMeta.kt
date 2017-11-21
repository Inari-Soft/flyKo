package com.inari.firefly.entity

import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.commons.lang.list.IntBag
import com.inari.firefly.component.NamedComponent

data class EMeta private constructor (
        var ff_Name: String?,
        var ff_Controller: IntBag,
        var ff_Parent: Int,
        var ff_ZPos: Int
) : EntityComponent(), NamedComponent {


    private constructor() : this(null, IntBag(5, -1, 5), -1, -1)

    override fun indexedTypeKey(): IIndexedTypeKey = typeKey
    override fun name(): String = ff_Name!!
    override fun reset() {
        this.also {
            ff_Name = null
            ff_Controller.clear()
            ff_Parent = -1
            ff_ZPos = -1
        }
    }

    companion object : EntityComponentBuilder<EMeta> {
        override val typeKey = EntityComponent.createTypeKey(EMeta::class.java)
        override fun builder(registry: (EMeta) -> EMeta): (EMeta.() -> Unit) -> Int = {
            configure -> build(EMeta(), configure, registry)
        }
    }
}