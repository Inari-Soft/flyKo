package com.inari.firefly.control.behavior

import com.inari.firefly.control.behavior.BehaviorSystem.TRUE_CONDITION
import com.inari.firefly.entity.Entity
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.util.OpResult

class BxCondition private constructor() : BxNode() {

    var condition: BxConditionOp = TRUE_CONDITION

    override fun tick(entity: Entity, behavior: EBehavior): OpResult =
            when (condition(entity, behavior) ) {
                true -> OpResult.SUCCESS
                false -> OpResult.FAILED
            }

    companion object : SystemComponentSubType<BxNode, BxCondition>(BxNode, BxCondition::class) {
        override fun createEmpty() = BxCondition()
    }

}