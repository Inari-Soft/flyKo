package com.inari.firefly.graphics.tile.set

import com.inari.firefly.FFContext
import com.inari.firefly.NO_COMP_ID
import com.inari.firefly.asset.AssetSystem
import com.inari.firefly.component.ComponentDSL
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.composite.Composite
import com.inari.firefly.entity.Entity
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.sprite.SpriteSetAsset
import com.inari.firefly.graphics.text.FontAsset
import com.inari.firefly.graphics.tile.ETile
import com.inari.firefly.graphics.view.Layer
import com.inari.firefly.graphics.view.View
import com.inari.firefly.physics.animation.entity.EAnimation
import com.inari.firefly.physics.animation.timeline.IntTimelineProperty
import com.inari.firefly.physics.contact.ContactSystem
import com.inari.firefly.physics.contact.EContact
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.util.collection.DynArray
import com.inari.util.graphics.RGBColor

@ComponentDSL
class TileSet : Composite() {

    @JvmField internal var textureAssetRef: Int = -1
    private var spriteSetAssetId = NO_COMP_ID
    private var viewRef = -1
    private var layerRef = -1
    private val tiles = DynArray.of<ProtoTile>()
    private var active = false

    val texture = ComponentRefResolver(FontAsset) { index -> textureAssetRef = index }
    var view = ComponentRefResolver(View) { index -> viewRef = index }
    var layer = ComponentRefResolver(Layer) { index -> layerRef = index }
    var blend: BlendMode? = null
    var tint: RGBColor? = null

    val tile: (ProtoTile.() -> Unit) -> ProtoTile = { configure ->
        val tile = ProtoTile()
        tile.also(configure)
        tiles.add(tile)
        tile
    }

    override fun load() {
        if (loaded)
            return

        spriteSetAssetId = SpriteSetAsset.build {
            name = super.name
            texture(this@TileSet.textureAssetRef)
            this@TileSet.tiles.forEach {
                if (it.int_animation != null)
                    spriteData.addAll(it.int_animation!!.sprites.values)
                spriteData.add(it.spriteData)
            }
        }
    }

    override fun activate() {
        if (active)
            return

        if (!loaded)
            load()

        if (spriteSetAssetId == NO_COMP_ID)
            throw IllegalStateException()

        FFContext.activate(spriteSetAssetId)
        if (activateInternal())
            active = true
    }

    private fun activateInternal(): Boolean {
        if (layerRef == -1)
            return false

        var it = 0
        while (it < tiles.capacity) {
            val tile = tiles[it++] ?: continue

            if (tile === TileSetContext.EMPTY_PROTO_TILE)
                continue

            val spriteId = tile.spriteData.instanceId
            if (spriteId < 0)
                return false

            val entityId = Entity.buildAndActivate {
                component(ETransform) {
                    view(this@TileSet.viewRef)
                    layer(this@TileSet.layerRef)
                }
                component(ETile) {
                    sprite.instanceId = spriteId
                    tint = tile.tintColor ?: this@TileSet.tint ?: tint
                    blend = tile.blendMode ?: this@TileSet.blend ?: blend
                }

                if (tile.hasContactComp) {
                    component(EContact) {
                        if (tile.contactType !== ContactSystem.UNDEFINED_CONTACT_TYPE) {
                            bounds(0,0,
                                tile.spriteData.textureBounds.width,
                                tile.spriteData.textureBounds.height)
                            contactType = tile.contactType
                            material = tile.material
                            mask = tile.contactMask ?: mask
                        }
                        material = tile.material
                    }
                }

                if (tile.int_animation != null) {
                    component(EAnimation) {
                        activeAnimation(IntTimelineProperty) {
                            looping = true
                            timeline = tile.int_animation!!.frames.toArray()
                            propertyRef = ETile.Property.SPRITE_REFERENCE
                        }
                    }
                }
            }

            tile.entityRef = entityId.instanceId
            TileSetContext.addActiveTileEntityId(entityId.instanceId, layerRef)
        }
        return true
    }

    override fun deactivate() {
        if (!active)
            return

        var i = 0
        while (i < tiles.capacity) {
            val tile = tiles[i++] ?: continue

            if (tile === TileSetContext.EMPTY_PROTO_TILE)
                continue

            if (tile.entityRef < 0)
                continue

            deactivateTile(tile)
        }

        TileSetContext.updateActiveTileEntityRefs(layerRef)
        FFContext.deactivate(spriteSetAssetId)
        this.active = false
    }

    private fun deactivateTile(protoTile: ProtoTile) {
        TileSetContext.removeActiveTileEntityId(protoTile.entityRef, layerRef)
        FFContext.delete(Entity, protoTile.entityRef)
    }

    override fun unload() {
        FFContext.delete(spriteSetAssetId)
        spriteSetAssetId = NO_COMP_ID
    }

    companion object : SystemComponentSubType<Composite, TileSet>(Composite, TileSet::class.java) {
        override fun createEmpty() = TileSet()
    }
}