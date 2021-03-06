package com.inari.firefly.external

import com.inari.firefly.FFApp
import com.inari.util.collection.DynArray

abstract class FFTimer protected constructor() : FFApp.SystemTimer() {

    protected var lastUpdateTime: Long = 0

    var time: Long = 0
        protected set
    var timeElapsed: Long = 0
        protected set


    private val schedulers: DynArray<UpdateScheduler> = DynArray.of( 20)

    internal fun updateSchedulers() {
        var i = 0
        val to = schedulers.capacity
        while ( i < to) {
            val updateScheduler = schedulers[i++] ?: continue
            updateScheduler.update()
        }
    }

    fun createUpdateScheduler(resolution: Float): UpdateScheduler {
        val updateScheduler = UpdateScheduler(resolution)
        schedulers.add(updateScheduler)
        return updateScheduler
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("FFTimer [lastUpdateTime=")
        builder.append(lastUpdateTime)
        builder.append(", time=")
        builder.append(time)
        builder.append(", timeElapsed=")
        builder.append(timeElapsed)
        builder.append("]")
        return builder.toString()
    }

    interface Scheduler {
        fun needsUpdate(): Boolean
    }

    inner class UpdateScheduler internal constructor(resolution: Float) : Scheduler {
        private val delayMillis: Long = (1000 / resolution).toLong()
        private var lastUpdate: Long = -1
        var tick: Long = 0
            private set
        private var needsUpdate: Boolean = false
        private var updated: Boolean = false

        internal fun update() {
            if (lastUpdateTime - lastUpdate >= delayMillis) {
                lastUpdate = lastUpdateTime
                tick++
                needsUpdate = true
            } else if (updated) {
                needsUpdate = false
                updated = false
            }
        }

        override fun needsUpdate(): Boolean {
            if (needsUpdate) {
                updated = true
            }
            return needsUpdate
        }

        fun reset() {
            lastUpdate = -1
            tick = 0
        }
    }
}
