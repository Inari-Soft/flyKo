package com.inari.firefly.graphics.text

import com.inari.firefly.component.ArrayAccessor
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.util.collection.DynArray

class ETextMeta private constructor() : EntityComponent(ETextMeta::class.simpleName!!) {

    @JvmField internal val metaData: DynArray<CharacterMetaData> = DynArray.of()

    var data = ArrayAccessor(metaData)
    var resolver: (Int) -> CharacterMetaData? = { index -> metaData[index] }

    override fun reset() {
        metaData.clear()
    }

    override fun componentType() = Companion
    companion object : EntityComponentType<ETextMeta>(ETextMeta::class) {
        override fun createEmpty() = ETextMeta()
    }
}