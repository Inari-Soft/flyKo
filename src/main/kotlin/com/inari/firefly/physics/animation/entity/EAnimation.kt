package com.inari.firefly.physics.animation.entity

import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.control.Controller
import com.inari.firefly.physics.animation.AnimationSystem
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.java.types.BitSet

class EAnimation : EntityComponent(EAnimation::class.java.name) {

    @JvmField internal var controllerRef = -1
    @JvmField internal val animations: BitSet = BitSet()
    @JvmField internal val activeAnimations: BitSet = BitSet()

    val ff_Controller = ComponentRefResolver(Controller) { index-> controllerRef = index }

    fun <A : EntityPropertyAnimation> ff_WithAnimation(cBuilder: EntityPropertyAnimation.PropertyAnimationSubtype<A>, configure: (A.() -> Unit)): A {
        val animation = cBuilder.doBuild(configure)
        animations.set(animation.index)
        return animation
    }

    fun <A : EntityPropertyAnimation> ff_WithActiveAnimation(cBuilder: EntityPropertyAnimation.PropertyAnimationSubtype<A>, configure: (A.() -> Unit)): A {
        val animation = cBuilder.doBuild(configure)
        animations.set(animation.index)
        activeAnimations.set(animation.index)
        return animation
    }

    fun clearAnimations() {
        var i = animations.nextSetBit(0)
        while (i >= 0) {
            AnimationSystem.animations.delete(i)
            i = animations.nextSetBit(i + 1)
        }
        animations.clear()
        activeAnimations.clear()
    }

    override fun reset() {
        controllerRef = -1
        clearAnimations()
    }

    override fun componentType() = Companion
    companion object : EntityComponentType<EAnimation>(EAnimation::class.java) {
        override fun createEmpty() = EAnimation()
    }
}