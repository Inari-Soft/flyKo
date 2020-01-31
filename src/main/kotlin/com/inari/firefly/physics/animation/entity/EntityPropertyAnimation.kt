package com.inari.firefly.physics.animation.entity

import com.inari.firefly.FFContext
import com.inari.firefly.NO_PROPERTY_REF
import com.inari.firefly.physics.animation.Animation
import com.inari.firefly.physics.animation.AnimationSystem
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.property.VirtualPropertyRef
import com.inari.firefly.system.component.SystemComponentSubType

abstract class EntityPropertyAnimation protected constructor() : Animation() {

    @JvmField protected var propertyRef: VirtualPropertyRef = NO_PROPERTY_REF

    var ff_PropertyRef: VirtualPropertyRef
        get() = propertyRef
        set(value) { propertyRef = if (FFContext.isActive(componentId)) throw IllegalStateException() else value }

    internal fun compile(entity: Entity) =
        init(entity)

    protected abstract fun init(entity: Entity)

    abstract class PropertyAnimationSubtype<A : EntityPropertyAnimation> : SystemComponentSubType<Animation, EntityPropertyAnimation>(Animation, EntityPropertyAnimation::class.java) {
        internal fun doBuild(configure: A.() -> Unit): A {
            val result = createEmpty()
            result.also(configure)
            AnimationSystem.animations.receiver()(result)
            return result
        }
        abstract override fun createEmpty(): A
    }
}