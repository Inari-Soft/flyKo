package com.inari.firefly.physics.movement

import com.inari.firefly.FFContext
import com.inari.firefly.INFINITE_SCHEDULER
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.control.Controller
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.firefly.external.FFTimer
import com.inari.util.geom.Vector2f

class EMovement private constructor() : EntityComponent(EMovement::class.java.name) {

    @JvmField internal var controllerRef = -1
    @JvmField internal var active = true
    @JvmField internal val velocity = Vector2f(0f, 0f)
    @JvmField internal val acceleration = Vector2f(0f, 0f)
    @JvmField internal var mass = 0f
    @JvmField internal var massFactor = 1f
    @JvmField internal var maxGravityVelocity = 200f
    @JvmField internal var onGround = false
    @JvmField internal var scheduler: FFTimer.Scheduler = INFINITE_SCHEDULER

    var ff_Active: Boolean
        get() = active
        set(value) { active = value }
    var ff_Velocity: Vector2f
        get() = velocity
        set(value) { velocity(value) }
    var ff_VelocityX: Float
        get() = velocity.dx
        set(value) { velocity.dx = value }
    var ff_VelocityY: Float
        get() = velocity.dy
        set(value) { velocity.dy = value }
    var ff_Acceleration: Vector2f
        get() = acceleration
        set(value) { acceleration(value) }
    var ff_AccelerationX: Float
        get() = acceleration.dx
        set(value) { acceleration.dx = value }
    var ff_AccelerationY: Float
        get() = acceleration.dy
        set(value) { acceleration.dy = value }
    var ff_Mass: Float
        get() = mass
        set(value) { mass = value }
    var ff_MassFactor: Float
        get() = massFactor
        set(value) { massFactor = value }
    var ff_MaxGravityVelocity: Float
        get() = maxGravityVelocity
        set(value) { maxGravityVelocity = value }
    var ff_OnGround: Boolean
        get() = onGround
        set(value) { onGround = value }
    var ff_UpdateResolution: Float
        get() = throw UnsupportedOperationException()
        set(value) { scheduler = FFContext.timer.createUpdateScheduler(value) }
    val ff_Controller = ComponentRefResolver(Controller) { index -> controllerRef = index }

    override fun reset() {
        active = false
        velocity.dx = 0f
        velocity.dy = 0f
        acceleration.dx = 0f
        acceleration.dy = 0f
        mass = 0f
        massFactor = 1f
        maxGravityVelocity = 200f
        onGround = false
        scheduler = INFINITE_SCHEDULER
    }

    override fun componentType() = Companion
    companion object : EntityComponentType<EMovement>(EMovement::class.java) {
        override fun createEmpty() = EMovement()
    }
}