package com.inari.firefly.apptests

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.inari.firefly.FFContext
import com.inari.firefly.OpResult
import com.inari.firefly.control.behavior.*
import com.inari.firefly.control.behavior.BehaviorSystem.ACTION_DONE_CONDITION
import com.inari.firefly.control.behavior.BehaviorSystem.BEHAVIOR_STATE_ASPECT_GROUP
import com.inari.firefly.control.task.TaskSystem
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.external.ShapeType
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.rendering.RenderingSystem
import com.inari.firefly.graphics.shape.EShape
import com.inari.firefly.libgdx.GDXAppAdapter
import com.inari.firefly.physics.movement.EMovement
import com.inari.firefly.physics.movement.MovementSystem
import com.inari.firefly.system.FFInfoSystem
import com.inari.firefly.system.FrameRateInfo
import com.inari.util.aspect.Aspect
import kotlin.random.Random

class BehaviorTest : GDXAppAdapter() {

    override val title: String = "BehaviorTest"

    override fun init() {
        FFInfoSystem
                .addInfo(FrameRateInfo)
                .activate()
        RenderingSystem
        FFContext.loadSystem(EntitySystem)
        FFContext.loadSystem(BehaviorSystem)
        FFContext.loadSystem(TaskSystem)
        MovementSystem

        val goRightState: Aspect = BEHAVIOR_STATE_ASPECT_GROUP.createAspect("goRight")
        val goLeftState: Aspect = BEHAVIOR_STATE_ASPECT_GROUP.createAspect("goLeft")
        val goDownState: Aspect = BEHAVIOR_STATE_ASPECT_GROUP.createAspect("goDown")
        val goUpState: Aspect = BEHAVIOR_STATE_ASPECT_GROUP.createAspect("goUp")

        BxSequence.build {
            ff_Name = "Root"
            ff_WithNode(BxParallel) {
                ff_Name = "parallel"
                ff_SuccessThreshold = 2
                ff_WithNode(BxSequence) {
                    ff_Name = "X"
                    ff_WithNode(BxSelection) {
                        ff_Name = "right"
                        ff_WithNode(BxCondition) {
                            ff_Name = "GoRight done?"
                            ff_Condition = ACTION_DONE_CONDITION(goRightState)
                        }
                        ff_WithNode(BxAction) {
                            ff_Name="GoRight"
                            ff_State = goRightState
                            ff_TickOp = { entityId, _ ->
                                val mov = EntitySystem[entityId][EMovement]
                                if (mov.ff_VelocityX <= 0f)
                                    mov.ff_VelocityX = Random.nextInt(1, 5).toFloat()
                                if (EntitySystem[entityId][ETransform].ff_Position.x < 800f)
                                    OpResult.RUNNING
                                else
                                    OpResult.SUCCESS
                            }
                        }
                    }
                    ff_WithNode(BxAction) {
                        ff_Name="GoLeft"
                        ff_State = goLeftState
                        ff_TickOp = { entityId, bx ->
                            val mov = EntitySystem[entityId][EMovement]
                            if (mov.ff_VelocityX >= 0f)
                                mov.ff_VelocityX = Random.nextInt(-5, -1).toFloat()
                            if (EntitySystem[entityId][ETransform].ff_Position.x < 10f) {
                                bx.actionsDone - goRightState
                                OpResult.SUCCESS
                            }
                            else
                                OpResult.RUNNING
                        }
                    }
                }
                ff_WithNode(BxSequence) {
                    ff_Name = "Y"
                    ff_WithNode(BxSelection) {
                        ff_Name = "down"
                        ff_WithNode(BxCondition) {
                            ff_Name = "GoDown done?"
                            ff_Condition = ACTION_DONE_CONDITION(goDownState)
                        }
                        ff_WithNode(BxAction) {
                            ff_Name="GoDown"
                            ff_State = goDownState
                            ff_TickOp = { entityId, _ ->
                                val mov = EntitySystem[entityId][EMovement]
                                if (mov.ff_VelocityY <= 0f)
                                    mov.ff_VelocityY = Random.nextInt(1, 5).toFloat()
                                if (EntitySystem[entityId][ETransform].ff_Position.y < 600)
                                    OpResult.RUNNING
                                else
                                    OpResult.SUCCESS
                            }
                        }
                    }
                    ff_WithNode(BxAction) {
                        ff_Name="GoUp"
                        ff_State = goUpState
                        ff_TickOp = { entityId, bx ->
                            val mov = EntitySystem[entityId][EMovement]
                            if (mov.ff_VelocityY >= 0f)
                                mov.ff_VelocityY = Random.nextInt(-5, -1).toFloat()
                            if (EntitySystem[entityId][ETransform].ff_Position.y < 10) {
                                bx.actionsDone - goDownState
                                OpResult.SUCCESS
                            }
                            else
                                OpResult.RUNNING
                        }
                    }
                }
            }
        }

        for (i in 1..2000) {
            Entity.buildAndActivate {
                ff_With(ETransform) {
                    ff_View(0)
                    ff_Position(Random.nextInt(0,800), Random.nextInt(0,600))
                }
                ff_With(EShape) {
                    ff_Type = ShapeType.RECTANGLE
                    ff_Fill = true
                    ff_Color(1f, 0f, 0f, 1f)
                    ff_Vertices = floatArrayOf(0f, 0f, 3f, 3f)
                }
                ff_With(EMovement) {
                    ff_VelocityX = 0f
                }
                ff_With(EBehavior) {
                    ff_BehaviorTree("Root")
                    ff_Repeat = true
                }
            }
        }



    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            try {
                val config = LwjglApplicationConfiguration()
                config.resizable = true
                config.width = 800
                config.height = 600
                LwjglApplication(BehaviorTest(), config)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }
}