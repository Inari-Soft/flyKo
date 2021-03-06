package com.inari.firefly.control.behavior

import com.inari.firefly.entity.Entity
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.util.OpResult


class BxParallel private constructor() : BxBranch() {
    var successThreshold: Int = 0

    override fun tick(entity: Entity, behavior: EBehavior): OpResult {
        val threshold = if (successThreshold > children.size)
                children.size
            else
                successThreshold

        var successCount = 0
        var failuresCount = 0
        var i = 0
        loop@ while (i < children.capacity) {
            when(children[i++]?.tick(entity, behavior) ?: continue@loop) {
                OpResult.RUNNING -> {}
                OpResult.SUCCESS -> successCount++
                OpResult.FAILED -> failuresCount++
            }
        }

        return when {
            successCount >= threshold -> OpResult.SUCCESS
            failuresCount > 0 -> OpResult.FAILED
            else -> OpResult.RUNNING
        }
    }

    companion object : SystemComponentSubType<BxNode, BxParallel>(BxNode, BxParallel::class) {
        override fun createEmpty() = BxParallel()
    }

}