package com.inari.firefly.control.behavior

import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.control.behavior.BehaviorSystem.BEHAVIOR_STATE_ASPECT_GROUP
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.util.OpResult
import com.inari.util.aspect.Aspects

class EBehavior private constructor() : EntityComponent(EBehavior::class.simpleName!!){

    @JvmField internal var treeRef = -1
    @JvmField internal var actionsDone: Aspects = BEHAVIOR_STATE_ASPECT_GROUP.createAspects()

    val behaviorTree = ComponentRefResolver(BxNode) { index-> treeRef = index }
    var repeat: Boolean = true
    var active: Boolean = true
    var treeState: OpResult = OpResult.SUCCESS
        internal set

    override fun reset() {
        treeRef = -1
        repeat = true
        treeState = OpResult.SUCCESS
        actionsDone.clear()
    }

    override fun componentType() = Companion
    companion object : EntityComponentType<EBehavior>(EBehavior::class) {
        override fun createEmpty() = EBehavior()
    }
}