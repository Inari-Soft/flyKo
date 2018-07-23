package com.inari.firefly.external

interface FFAudio {

    fun createSound(resourceName: String, streaming: Boolean): Int

    fun disposeSound(soundId: Int)

    fun playSound(soundId: Int, channel: Int, looping: Boolean, volume: Float, pitch: Float, pan: Float): Long

    fun changeSound(soundId: Int, instanceId: Long, volume: Float, pitch: Float, pan: Float)

    fun stopSound(soundId: Int, instanceId: Long)

    fun playMusic(soundId: Int, looping: Boolean, volume: Float, pan: Float)

    fun changeMusic(soundId: Int, volume: Float, pan: Float)

    fun stopMusic(soundId: Int)

}
