package com.inari.firefly.graphics.rendering

import com.inari.commons.geom.Rectangle
import com.inari.firefly.FFContext
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.particle.EParticle
import com.inari.firefly.graphics.particle.SpriteParticle
import com.inari.firefly.system.component.SingletonComponent

class SpriteParticleRenderer private constructor() : Renderer() {

    override fun match(entity: Entity): Boolean =
        entity.components.include(MATCHING_ASPECTS) &&
            entity[EParticle].rendererRef == index

    override fun render(viewIndex: Int, layerIndex: Int, clip: Rectangle) {
        val toRender = getIfNotEmpty(viewIndex, layerIndex) ?: return

        val graphics = FFContext.graphics
        var i = 0
        while (i < toRender.capacity()) {
            val entity = toRender[i++] ?: continue
            val spriteParticle = entity[EParticle].getParticle<SpriteParticle>()
            val transform = entity[ETransform]

            var ii = 0
            while (ii < spriteParticle.capacity()) {
                val particle = spriteParticle[ii++] ?: continue
                transformCollector.set(transform)
                transformCollector.add(particle)
                graphics.renderSprite(particle, transformCollector)
            }
        }
    }

    companion object : SingletonComponent<SpriteParticleRenderer, Renderer>() {
        override val typeKey = Renderer.typeKey
        override fun subType() = SpriteParticleRenderer::class.java
        override fun create() = SpriteParticleRenderer()

        private val MATCHING_ASPECTS = EntityComponent.ENTITY_COMPONENT_ASPECTS.createAspects(
            ETransform, EParticle
        )
    }
}