package com.inari.firefly.graphics.rendering

import com.inari.firefly.FFContext
import com.inari.firefly.entity.EChild
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.external.SpriteRenderable
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.text.EText
import com.inari.firefly.graphics.text.FontAsset
import com.inari.firefly.system.component.SingletonComponent
import com.inari.util.geom.Rectangle


class SimpleTextRenderer private constructor() : Renderer() {

    override fun match(entity: Entity): Boolean =
        entity.components.include(MATCHING_ASPECTS) &&
            entity[EText].rendererRef == index

    override fun render(viewIndex: Int, layerIndex: Int, clip: Rectangle) {
        val toRender = getIfNotEmpty(viewIndex, layerIndex) ?: return

        val graphics = FFContext.graphics
        var i = 0
        while (i < toRender.capacity) {
            val entity = toRender[i++] ?: continue

            val text = entity[EText]
            val transform = entity[ETransform]
            val font = FFContext[FontAsset, text.fontAssetRef]
            val chars = text.textBuffer

            textRenderable.shaderId = text.shaderRef
            textRenderable.tintColor(text.tint)
            textRenderable.blendMode = text.blend
            transformCollector(transform.data)
            if (EChild in entity.aspects)
                collectTransformData(entity[EChild].parent, transformCollector)

            val horizontalStep = (font.charWidth + font.charSpace) * transform.data.scale.dx
            val verticalStep = (font.charHeight + font.lineSpace) * transform.data.scale.dy
            val newLinePos = transformCollector.data.position.x
            var j = 0
            while (j < chars.length) {
                val char = chars[j++]
                if (char == '\n') {
                    transformCollector.data.position.x = newLinePos
                    transformCollector.data.position.y += verticalStep
                    continue
                }

                if (char == ' ') {
                    transformCollector.data.position.x += horizontalStep
                    continue
                }

                textRenderable.spriteId = font.charSpriteMap[char.toInt()]

                if (textRenderable.spriteId < 0) {
                    transformCollector.data.position.x += horizontalStep
                    continue
                }

                graphics.renderSprite(textRenderable, transformCollector.data)
                transformCollector.data.position.x += horizontalStep
            }
        }
    }

    private val textRenderable = SpriteRenderable()

    companion object : SingletonComponent<Renderer, SimpleTextRenderer>(Renderer, SimpleTextRenderer::class.java) {
        override fun create() = SimpleTextRenderer()
        private val MATCHING_ASPECTS = EntityComponent.ENTITY_COMPONENT_ASPECTS.createAspects(
            ETransform, EText
        )
    }
}