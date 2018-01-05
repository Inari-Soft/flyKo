package com.inari.firefly.graphics.rendering

import com.inari.commons.geom.Rectangle
import com.inari.commons.graphics.RGBColor
import com.inari.firefly.FFContext
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.external.SpriteRenderable
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.text.EText
import com.inari.firefly.graphics.text.FontAsset
import com.inari.firefly.setFrom
import com.inari.firefly.system.component.SingletonComponent


class SimpleTextRenderer private constructor() : Renderer() {

    override fun match(entity: Entity): Boolean =
        entity.components.include(MATCHING_ASPECTS) &&
            entity[EText].rendererRef == index

    override fun render(viewIndex: Int, layerIndex: Int, clip: Rectangle) {
        val toRender = getIfNotEmpty(viewIndex, layerIndex) ?: return

        val graphics = FFContext.graphics
        var i = 0
        while (i < toRender.capacity()) {
            val entity = toRender.get(i++) ?: continue

            val text = entity[EText]
            val transform = entity[ETransform]
            val font = FFContext[FontAsset, text.fontRef]
            val chars = text.textBuffer

            textRenderable.shader = text.shaderRef
            textRenderable.tint.setFrom(text.tint)
            textRenderable.blend = text.blend
            renderingTransform.set(transform)
            val horizontalStep = (font.charWidth + font.charSpace) * transform.scale.dx
            val verticalStep = (font.charHeight + font.lineSpace) * transform.scale.dy

            var j = 0
            while (j < chars.length) {
                val char = chars[j++]
                if (char == '\n') {
                    renderingTransform.xOffset = transform.position.x
                    renderingTransform.yOffset += verticalStep
                    continue
                }

                if ( char == ' ' ) {
                    renderingTransform.xOffset += horizontalStep
                    continue
                }

                textRenderable.sprite = font.charSpriteMap[char.toInt()]
                graphics.renderSprite(textRenderable, renderingTransform)
                renderingTransform.xOffset += horizontalStep
            }
        }
    }

    private val renderingTransform = ExactTransformDataCollector()
    private val textRenderable = object : SpriteRenderable {

        internal var sprite = -1
        internal var shader = -1
        internal val tint = RGBColor()
        internal var blend = BlendMode.NONE

        override val tintColor: RGBColor
            get() = tint
        override val blendMode: BlendMode
            get() = blend
        override val shaderId: Int
            get() = shader
        override val spriteId: Int
            get() = sprite

    }

    companion object : SingletonComponent<SimpleTextRenderer, Renderer>() {
        override val typeKey = Renderer.typeKey
        override fun subType() = SimpleTextRenderer::class.java
        override fun create() = SimpleTextRenderer()

        private val MATCHING_ASPECTS = EntityComponent.ENTITY_COMPONENT_ASPECTS.createAspects(
            ETransform, EText
        )
    }
}