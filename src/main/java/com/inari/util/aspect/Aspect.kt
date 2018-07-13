package com.inari.util.aspect

import com.inari.commons.lang.list.DynArray
import com.inari.util.indexed.AbstractIndexed
import com.inari.util.indexed.Indexer

interface Aspect {
    val aspectName: String
    val aspectType: AspectType
    val aspectIndex: Int
}

interface AspectAware {
    val aspects: Aspects
}

interface AspectType {
    val name: String
    fun createAspects(): Aspects
    fun createAspects(vararg aspects: Aspect): Aspects
    fun typeCheck(aspect: Aspect): Boolean
    operator fun get(name: String): Aspect?
    operator fun get(index: Int): Aspect?
}

class IndexedAspectType(
    override val name: String
) : AspectType {

    private val aspects = DynArray.create(Aspect::class.java, 10, 10)
    private val indexer = Indexer(name)

    override fun createAspects(): Aspects =
        Aspects(this)

    override fun createAspects(vararg aspects: Aspect): Aspects {
        val result = Aspects(this)
        for (aspect in aspects)
            result + aspect
        return result
    }

    override fun get(name: String): Aspect? {
        for (aspect in aspects)
            if (name == aspect.aspectName)
                return aspect
        return null
    }

    override fun get(index: Int): Aspect =
        aspects[index]

    override fun typeCheck(aspect: Aspect) =
        aspect.aspectType === this


    fun createAspect(name: String): Aspect {
        val aspect = get(name)
        if (aspect != null)
            return aspect

        val newAspect = IndexedAspect(name, this)
        aspects[newAspect.aspectIndex] = newAspect
        return newAspect
    }

    class IndexedAspect(
        override val aspectName: String,
        override val aspectType: IndexedAspectType
    ) : AbstractIndexed(), Aspect {
        override val aspectIndex: Int
            get() = index
        override val indexer: Indexer
            get() = aspectType.indexer
    }
}

