package com.inari.firefly.entity

import com.inari.firefly.NO_NAME
import com.inari.firefly.component.CompId
import com.inari.firefly.entity.EntityComponent.Companion.ENTITY_COMPONENT_ASPECTS
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentSingleType
import com.inari.util.aspect.*

class Entity internal constructor(): SystemComponent(Entity::class.simpleName!!), AspectAware {

    @JvmField internal val components: AspectSet<EntityComponent> = AspectSet.of(ENTITY_COMPONENT_ASPECTS)

    override val aspects: Aspects
        get() = components.aspects

    override var name: String  = NO_NAME
        set(value) {
            check(!(name !== NO_NAME)) { "An illegal reassignment of name: $value to: $name" }
            field = name
        }

    fun has(aspect: Aspect): Boolean =
        aspects.contains(aspect)

    operator fun <C : EntityComponent> get(type: EntityComponentType<C>): C =
        components.get(type, type.typeClass)!!

    fun <C : EntityComponent> component(cBuilder: EntityComponentBuilder<C>, configure: (C.() -> Unit)): CompId =
            cBuilder.builder { comp ->
                components.set(comp.componentType(), comp)
                comp
            } (configure)

    internal fun reset() {
        check(!EntitySystem.entities.isActive(index)) {
            "Entity: $index is still active and cannot be disposed"
        }

        for (aspect in components.aspects)
            EntityProvider.dispose(components.get(aspect)!!)

        components.clear()
        initialized = false
    }

    internal fun restore(): Entity {
        disposeIndex()
        return this
    }

    override fun toString(): String =
            "Entity(name=$name " +
            "components=$components)"

    override fun componentType() = Companion
    companion object : SystemComponentSingleType<Entity>(Entity::class) {
        public override fun createEmpty() = EntityProvider.get()
    }
}