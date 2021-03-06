package com.inari.firefly.physics.animation

import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.component.CompId
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityActivationEvent
import com.inari.firefly.physics.animation.entity.EAnimation
import com.inari.firefly.physics.animation.entity.EntityPropertyAnimation
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent
import com.inari.util.aspect.Aspects

object AnimationSystem : ComponentSystem {
    override val supportedComponents: Aspects =
        SystemComponent.SYSTEM_COMPONENT_ASPECTS.createAspects(Animation)

    @JvmField val animations = ComponentSystem.createComponentMapping(
        Animation,
        nameMapping = true,
        activationMapping = true
    )

    init {
        FFContext.registerListener(FFApp.UpdateEvent) {
                animations.forEachActive { it.update() }
        }

        FFContext.registerListener(EntityActivationEvent, object: EntityActivationEvent.Listener {
            override fun entityActivated(entity: Entity) =
                registerEntityAnimations(entity)
            override fun entityDeactivated(entity: Entity) =
                detachEntityAnimations(entity)
            override fun match(aspects: Aspects): Boolean =
                EAnimation in aspects
        })

        FFContext.loadSystem(this)
    }

    fun registerEntityAnimation(entityName: String, animationName: String) {
        val entity = FFContext[Entity, entityName]
        val eAnim = entity[EAnimation]
        val animProp: EntityPropertyAnimation = animations.getAs(animationName)
        eAnim.animations.set(animProp.index)
        animProp.applyToEntity(entity)
    }

    fun registerEntityAnimation(entityId: CompId, animationId: CompId) =
            registerEntityAnimation(entityId.index, animationId.index)

    fun registerEntityAnimation(entityId: Int, animationId: Int) {
        val entity = FFContext[Entity, entityId]
        val eAnim = entity[EAnimation]
        val animProp: EntityPropertyAnimation = animations.getAs(animationId)
        eAnim.animations.set(animationId)
        animProp.applyToEntity(entity)
    }

    fun detachEntityAnimation(entityName: String, animationName: String) {
        val entity = FFContext[Entity, entityName]
        val eAnim = entity[EAnimation]
        val animProp: EntityPropertyAnimation = animations.getAs(animationName)
        eAnim.animations.clear(animProp.index)
        animProp.detachFromEntity(entity)
    }

    fun detachEntityAnimation(entityId: CompId, animationId: CompId) =
            detachEntityAnimation(entityId.index, animationId.index)

    fun detachEntityAnimation(entityId: Int, animationId: Int) {
        val entity = FFContext[Entity, entityId]
        val eAnim = entity[EAnimation]
        val animProp: EntityPropertyAnimation = animations.getAs(animationId)
        eAnim.animations.clear(animationId)
        animProp.detachFromEntity(entity)
    }

    fun detachEntityAnimations(entityName: String) =
            detachEntityAnimations(FFContext[Entity, entityName])

    fun detachEntityAnimations(entityId: CompId) =
            detachEntityAnimations(entityId.index)

    fun detachEntityAnimations(entityId: Int) =
            detachEntityAnimations(FFContext[Entity, entityId])

    internal fun registerEntityAnimations(entity: Entity) {
        val eAnim = entity[EAnimation]
        var i = eAnim.animations.nextSetBit(0)
        while (i >= 0) {
            if (i in animations) {
                val animProp: EntityPropertyAnimation = animations.getAs(i)
                animProp.applyToEntity(entity)
            }
            i = eAnim.animations.nextSetBit(i + 1)
        }
    }

    internal fun detachEntityAnimations(entity: Entity) {
        val eAnim = entity[EAnimation]
        var i = eAnim.animations.nextSetBit(0)
        while (i >= 0) {
            if (i in animations) {
                val animProp: EntityPropertyAnimation = animations.getAs(i)
                animProp.detachFromEntity(entity)
            }
            i = eAnim.animations.nextSetBit(i + 1)
        }
    }

    override fun clearSystem() =
        animations.clear()
}