package com.inari.firefly.control.behavior

import com.inari.firefly.entity.Entity
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.util.OpResult

class BxSequence private constructor() : BxBranch() {

    override fun tick(entity: Entity, behavior: EBehavior): OpResult {
        var i = 0
        loop@ while (i < children.capacity) {
            when(children[i++]?.tick(entity, behavior) ?: continue@loop) {
                OpResult.RUNNING -> return OpResult.RUNNING
                OpResult.FAILED -> return OpResult.FAILED
                OpResult.SUCCESS -> {}
            }
        }
        return OpResult.SUCCESS
    }

    companion object : SystemComponentSubType<BxNode, BxSequence>(BxNode, BxSequence::class) {
        override fun createEmpty() = BxSequence()
    }
}