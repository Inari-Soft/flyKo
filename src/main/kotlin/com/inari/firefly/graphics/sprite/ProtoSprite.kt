package com.inari.firefly.graphics.sprite

import com.inari.firefly.NO_NAME
import com.inari.firefly.asset.IndexedInstantiable
import com.inari.firefly.component.ComponentDSL
import com.inari.firefly.graphics.particle.SpriteParticle
import com.inari.util.geom.Rectangle

@ComponentDSL
class ProtoSprite internal constructor() : IndexedInstantiable {

    @JvmField internal var instId = -1
    override val instanceId: Int get() = instId

    var name: String = NO_NAME
    val textureBounds: Rectangle = Rectangle()
    var hFlip: Boolean = false
    var vFlip: Boolean = false

    companion object {

        val of: (ProtoSprite.() -> Unit) -> ProtoSprite = { configure ->
            val instance = ProtoSprite()
            instance.also(configure)
            instance
        }
    }

}