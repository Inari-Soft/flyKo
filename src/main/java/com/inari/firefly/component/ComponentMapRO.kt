package com.inari.firefly.component

import com.inari.firefly.IntFunction

interface ComponentMapRO<C : Component> {

    operator fun contains(index: Int): Boolean
    operator fun contains(name: String): Boolean
    operator fun contains(id: CompId): Boolean
    fun idForName(name: String): CompId
    fun indexForName(name: String): Int
    fun isActive(index: Int): Boolean
    fun isActive(name: String): Boolean
    fun isActive(id: CompId): Boolean
    operator fun get(index: Int): C
    operator fun get(name: String): C
    operator fun get(id: CompId): C
    fun <CC : C> getAs(index: Int): CC
    fun <CC : C> getAs(name: String): CC
    fun <CC : C> getAs(id: CompId): CC

}