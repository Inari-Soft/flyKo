package com.inari.firefly.graphics.tile

import com.inari.firefly.FFContext
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentMap.MapAction.CREATED
import com.inari.firefly.component.ComponentMap.MapAction.DELETED
import com.inari.firefly.entity.EMultiplier
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityActivationEvent
import com.inari.firefly.external.ViewData
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.view.ViewEvent
import com.inari.firefly.graphics.view.ViewEvent.Type.VIEW_DELETED
import com.inari.firefly.graphics.view.ViewLayerAware
import com.inari.firefly.graphics.view.ViewLayerMapping
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent
import com.inari.util.aspect.Aspects


object TileGridSystem : ComponentSystem {

    override val supportedComponents: Aspects =
        SystemComponent.SYSTEM_COMPONENT_ASPECTS.createAspects(TileGrid)

    @JvmField val viewLayerMapping: ViewLayerMapping<TileGrid> = ViewLayerMapping.of()
    @JvmField val grids = ComponentSystem.createComponentMapping(
        TileGrid,
        listener = { grid, action -> when (action) {
            CREATED -> viewLayerMapping.add(grid)
            DELETED -> viewLayerMapping.delete(grid)
            else -> {}
        } }
    )

    init {
        FFContext.registerListener(
            ViewEvent,
            object : ViewEvent.Listener {
                override fun invoke(id: CompId, viewPort: ViewData, type: ViewEvent.Type) {
                    when(type) {
                        VIEW_DELETED -> viewLayerMapping[id.instanceId]
                            .forEach { grid -> grids.delete(grid.index) }
                        else -> {}
                    }
                }
            }
        )

        FFContext.registerListener(
            EntityActivationEvent,
            object : EntityActivationEvent.Listener {
                override fun entityActivated(entity: Entity) =
                    addEntity(entity)
                override fun entityDeactivated(entity: Entity) =
                    removeEntity(entity)
                override fun match(aspects: Aspects): Boolean =
                    aspects.contains(ETile)
            }
        )

        FFContext.loadSystem(this)
    }

    fun exists(viewIndex: Int, layerIndex: Int): Boolean =
        viewIndex in viewLayerMapping && layerIndex in viewLayerMapping[viewIndex]

    fun exists(viewLayer: ViewLayerAware): Boolean =
        exists(viewLayer.viewIndex, viewLayer.layerIndex)

    operator fun get(viewLayer: ViewLayerAware): TileGrid? =
        this[viewLayer.viewIndex, viewLayer.layerIndex]

    operator fun get(viewIndex: Int, layerIndex: Int): TileGrid? =
            viewLayerMapping[viewIndex][layerIndex]

    override fun clearSystem() {
        grids.clear()
    }

    private fun addEntity(entity: Entity) {
        val tileGrid = this[entity[ETransform]] ?: return

        if (entity.has(EMultiplier)) {
            val multiplier = entity[EMultiplier]
            val pi = multiplier.positions.iterator()
            while (pi.hasNext()) {
                val x: Int = pi.nextFloat().toInt()
                val y: Int = pi.nextFloat().toInt()
                tileGrid[x, y] = entity.index
            }
        } else {
            tileGrid[entity[ETile].position] = entity.index
        }
    }

    private fun removeEntity(entity: Entity) {
        val tileGrid = this[entity[ETransform]] ?: return

        if (entity.has(EMultiplier)) {
            val multiplier = entity[EMultiplier]
            val pi = multiplier.positions.iterator()
            while (pi.hasNext()) {
                val x: Int = pi.nextFloat().toInt()
                val y: Int = pi.nextFloat().toInt()
                tileGrid.resetIfMatch(entity.index, x, y)
            }
        } else {
            tileGrid.resetIfMatch(entity.index, entity[ETile].position)
        }
    }
}