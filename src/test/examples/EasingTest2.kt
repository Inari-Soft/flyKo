import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.inari.firefly.FFContext
import com.inari.firefly.SYSTEM_FONT
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.external.ShapeType
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.rendering.RenderingSystem
import com.inari.firefly.graphics.rendering.SimpleTextRenderer
import com.inari.firefly.graphics.shape.EShape
import com.inari.firefly.graphics.text.EText
import com.inari.firefly.libgdx.DesktopAppAdapter

import com.inari.firefly.physics.animation.AnimationSystem
import com.inari.firefly.physics.animation.easing.EasedProperty
import com.inari.firefly.physics.animation.entity.EAnimation
import com.inari.firefly.system.FFInfoSystem
import com.inari.firefly.system.FrameRateInfo
import com.inari.util.geom.Easing


class EasingTest2 : DesktopAppAdapter() {

    override val title: String = "EasingTest2"

    override fun init() {
        FFInfoSystem
            .addInfo(FrameRateInfo)
            .activate()
        RenderingSystem
        FFContext.loadSystem(EntitySystem)
        FFContext.loadSystem(AnimationSystem)

        for ((index, easingType) in Easing.Type.values().withIndex()) {
            createEasingAnimation(easingType, index)
        }
    }

    private fun createEasingAnimation(type: Easing.Type, position: Int) {
        val ypos = position.toFloat() * (20f + 10f) + 50f
        Entity.buildAndActivate {
            component(ETransform) {
                view(0)
                position(10f, ypos)
            }
            component(EText) {
                renderer(SimpleTextRenderer)
                fontAsset(SYSTEM_FONT)
                text.append(type.name.replace("_", "-"))
                blend = BlendMode.NORMAL_ALPHA
                tint(1f, 1f, 1f, 1f)
            }
        }
        Entity.buildAndActivate {
            component(ETransform) {
                view(0)
            }
            component(EShape) {
                this.shapeType = ShapeType.RECTANGLE
                fill = true
                color(1f, 0f, 0f, 1f)
                vertices = floatArrayOf(100f, ypos, 20f, 20f)
            }
            component(EAnimation) {
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 100f
                    endValue = 400f
                    duration = 5000
                    easing = type
                    propertyRef = ETransform.Property.POSITION_X
                }
            }
        }
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            try {
                val config = Lwjgl3ApplicationConfiguration()
                config.setResizable(true)
                config.setWindowedMode(800, 900)
                Lwjgl3Application(EasingTest2(), config)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }
}
