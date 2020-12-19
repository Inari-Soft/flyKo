package com.inari.firefly.graphics.view

import com.inari.firefly.asset.AssetInstanceRefResolver
import com.inari.firefly.component.CompId
import com.inari.util.geom.PositionF
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.control.Controller
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.external.ViewData
import com.inari.firefly.system.component.SystemComponentBuilder
import com.inari.firefly.system.component.SystemComponentSingleType
import com.inari.util.geom.Rectangle
import com.inari.util.graphics.RGBColor

class View private constructor (
    @JvmField internal var baseView: Boolean = false
) : SystemComponent(View::class.java.name) {

    @JvmField internal var controllerRef = -1
    @JvmField internal val data = object : ViewData() {
        override val index: Int
            get() = super@View.index
        override val isBase: Boolean
            get() = baseView
    }

    var bounds: Rectangle
        get() = data.bounds
        set(value) { data.bounds(value) }
    var worldPosition: PositionF
        get() = data.worldPosition
        set(value) { data.worldPosition(value) }
    var clearColor: RGBColor
        get() = data.clearColor
        set(value) { data.clearColor(value) }
    var tintColor: RGBColor
        get() = data.tintColor
        set(value) { data.tintColor(value) }
    var blendMode: BlendMode
        get() = data.blendMode
        set(value) { data.blendMode = value }
    val shader = AssetInstanceRefResolver(
        { index -> data.shaderId = index },
        { data.shaderId })
    var zoom: Float
        get() = data.zoom
        set(value) { data.zoom = value }
    var fboScale: Float
        get() = data.fboScale
        set(value) { data.fboScale = value }
    var controller =
        ComponentRefResolver(Controller) { index-> controllerRef = setIfNotInitialized(index, "controllerRef") }
    fun <C : Controller> controller(builder: SystemComponentBuilder<C>, configure: (C.() -> Unit)): CompId {
        val id = builder. build(configure)
        controllerRef = id.index
        return id
    }
    fun <C : Controller> activeController(builder: SystemComponentBuilder<C>, configure: (C.() -> Unit)): CompId {
        val id = builder.buildAndActivate(configure)
        controllerRef = id.index
        return id
    }

    override fun toString(): String {
        return "View(baseView=$baseView, " +
            "controllerRef=$controllerRef, " +
            "bounds=${data.bounds}, " +
            "worldPosition=${data.worldPosition}, " +
            "clearColor=${data.clearColor}, " +
            "tintColor=${data.tintColor}, " +
            "blendMode=${data.blendMode}, " +
            "shaderId=${data.shaderId}, " +
            "zoom=${data.zoom}, " +
            "fboScale=${data.fboScale})"
    }

    override fun componentType() = Companion
    companion object : SystemComponentSingleType<View>(View::class.java) {
        override fun createEmpty() = View()
    }
}