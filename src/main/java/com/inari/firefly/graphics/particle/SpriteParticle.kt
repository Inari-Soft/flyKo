package com.inari.firefly.graphics.particle

import com.inari.firefly.external.SpriteRenderable
import com.inari.firefly.graphics.BlendMode
import com.inari.util.graphics.RGBColor

class SpriteParticle(
    @JvmField internal val spriteRef: Int,
    @JvmField internal val blend: BlendMode = BlendMode.NONE,
    @JvmField internal val tint: RGBColor = RGBColor(1f, 1f, 1f, 1f),
    x: Float = 0f,
    y: Float = 0f,
    xScale: Float = 1f,
    yScale: Float = 1f,
    xPivot: Float = 0f,
    yPivot: Float = 0f,
    rot: Float = 0f,
    xVelocity: Float = 0f,
    yVelocity: Float = 0f,
    mass: Float = 1f
) : Particle(xVelocity, yVelocity, mass) {

    @JvmField internal val spriteRenderable = SpriteRenderable()

    init {
        super.transformData.position.x = x
        super.transformData.position.y = y
        super.transformData.scale.dx = xScale
        super.transformData.scale.dy = yScale
        super.transformData.pivot.x = xPivot
        super.transformData.pivot.y = yPivot
        super.transformData.rotation = rot
        spriteRenderable.spriteId = spriteRef
        spriteRenderable.tintColor = tint
        spriteRenderable.blendMode = blend
    }
}