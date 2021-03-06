package com.inari.firefly.control.action

import com.inari.firefly.FFContext
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent
import com.inari.util.aspect.Aspects

typealias ActionCall = (Int, Int, Int, Int) -> Unit
object ActionSystem : ComponentSystem {

    override val supportedComponents: Aspects =
            SystemComponent.SYSTEM_COMPONENT_ASPECTS.createAspects(Action)

    @JvmField val actions = ComponentSystem.createComponentMapping(
            Action,
            nameMapping = true
    )

    init {
        FFContext.loadSystem(this)
    }

    override fun clearSystem() {
        actions.clear()
    }

}