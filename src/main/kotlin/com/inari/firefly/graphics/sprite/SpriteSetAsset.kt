package com.inari.firefly.graphics.sprite

import com.inari.firefly.FFContext
import com.inari.firefly.NO_NAME
import com.inari.firefly.asset.Asset
import com.inari.firefly.component.ArrayAccessor
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.external.SpriteData
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.util.collection.DynArray


class SpriteSetAsset private constructor() : Asset() {

    private val int_spriteData: DynArray<ProtoSprite> = DynArray.of(30)
    private val tmpSpriteData = SpriteData()

    val spriteData = ArrayAccessor(int_spriteData)
    var texture = ComponentRefResolver(Asset) { index -> dependingRef = setIfNotInitialized(index, "TextureAsset") }
    val protoSprite: (ProtoSprite.() -> Unit) -> Unit = { configure ->
        val sprite = ProtoSprite()
        sprite.also(configure)
        spriteData.add(sprite)
    }

    override fun instanceId(index: Int): Int =
        int_spriteData[index]?.instanceId ?: -1

    fun instanceId(name: String): Int {
        if (name == NO_NAME)
            return -1

        int_spriteData
            .filter { name == it.name }
            .forEach { return it.instanceId }

        return -1
    }

    override fun load() {
        val graphics = FFContext.graphics
        tmpSpriteData.textureId = FFContext.assetInstanceId(dependingRef)
        for (i in 0 until int_spriteData.capacity) {
            val sprite = int_spriteData[i] ?: continue
            tmpSpriteData.region(sprite.textureBounds)
            tmpSpriteData.isHorizontalFlip = sprite.hFlip
            tmpSpriteData.isVerticalFlip = sprite.vFlip
            sprite.instId = graphics.createSprite(tmpSpriteData)
        }
        tmpSpriteData.reset()
    }

    override fun unload() {
        val graphics = FFContext.graphics
        for (i in 0 until int_spriteData.capacity) {
            val sprite = int_spriteData[i] ?: continue
            graphics.disposeSprite(sprite.instanceId)
            sprite.instId = -1
        }
    }

    companion object : SystemComponentSubType<Asset, SpriteSetAsset>(Asset, SpriteSetAsset::class) {
        override fun createEmpty() = SpriteSetAsset()
    }
}