package com.inari.firefly.asset

import com.inari.firefly.NO_NAME
import com.inari.firefly.FFContext
import com.inari.firefly.TestApp
import com.inari.firefly.component.CompId
import com.inari.util.indexed.Indexer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class AssetSystemTest {

    @Before
    fun init() {
        TestApp
        AssetSystem.clearSystem()
    }

    @Test
    fun assetCreation() {

        assertTrue(AssetSystem.assets.map.isEmpty)
        assertTrue(AssetSystem.assets == FFContext.mapper(Asset))

        val emptyAssetId = TestAsset.build {}

        assertEquals(
            "CompId(instanceId=0, componentType=SystemComponentType:Asset)",
            emptyAssetId.toString()
        )

        assertFalse(AssetSystem.assets.map.isEmpty)

        val emptyAsset = FFContext.get<Asset>(emptyAssetId)
        assertNotNull(emptyAsset)
        assertEquals(
            "TestAsset(name='[[NO_NAME]]', Param1='', Param2=0.0, instanceId=-1) dependsOn=-1",
            emptyAsset.toString()
        )

        try {
            AssetSystem.assets.get(NO_NAME)
            fail("Exception expected here")
        } catch (e: Exception) {
            assertEquals("Component: SystemComponentType:Asset for name: [[NO_NAME]] not found", e.message)
        }

        val assetId = TestAsset.build {
            name = "testName"
            Param1 = "param1"
            Param2 = 1.45f
        }

        assertNotNull(assetId)
        val assetById = FFContext.get<Asset>(assetId)
        val assetByName = FFContext.get(Asset, "testName")
        assertEquals(assetById, assetByName)
    }

    @Test
    fun simpleLifeCycle() {
        val testEvents = StringBuilder()
        FFContext.registerListener(
            AssetEvent,
            object : AssetEvent.Listener{
                override fun invoke(viewId: CompId, type: AssetEvent.Type) {
                    testEvents.append("|").append(type.toString())
                }
            }
        )
        assertEquals("", testEvents.toString())

        val assetId = TestAsset.build {
            name = "testName"
            Param1 = "param1"
            Param2 = 1.45f
        }

        assertEquals(
            "CompId(instanceId=0, componentType=SystemComponentType:Asset)",
            assetId.toString()
        )
        assertEquals(
            "|ASSET_CREATED",
            testEvents.toString()
        )

        FFContext.activate(assetId)
        assertEquals(
            "|ASSET_CREATED|ASSET_LOADED",
            testEvents.toString()
        )
        FFContext.deactivate(assetId)
        assertEquals(
            "|ASSET_CREATED|ASSET_LOADED|ASSET_DISPOSED",
            testEvents.toString()
        )
        FFContext.activate(assetId)
        assertEquals(
            "|ASSET_CREATED|ASSET_LOADED|ASSET_DISPOSED|ASSET_LOADED",
            testEvents.toString()
        )
        FFContext.delete(assetId)
        assertEquals(
            "|ASSET_CREATED|ASSET_LOADED|ASSET_DISPOSED|ASSET_LOADED|ASSET_DISPOSED|ASSET_DELETED",
            testEvents.toString()
        )
    }

    @Test
    fun lifeCycleWithDependentAssets() {
        val testEvents = StringBuilder()
        FFContext.registerListener<AssetEvent.Listener>(
            AssetEvent,
            object : AssetEvent.Listener{
                override fun invoke(viewId: CompId, type: AssetEvent.Type) {
                    testEvents.append("|id=").append(viewId.instanceId).append(":").append(type.toString())
                }
            }
        )
        assertEquals("", testEvents.toString())

        val asset1 = TestAsset.build {
            name = "parentAsset"
            Param1 = "parent"
            Param2 = 1.45f
        }
        assertEquals(
            "|id=0:ASSET_CREATED",
            testEvents.toString()
        )

        val asset2 = TestAsset.build {
            DependsOn("parentAsset")
            name = "childAsset"
            Param1 = "child"
            Param2 = 1.45f
        }
        assertEquals(
            "|id=0:ASSET_CREATED" +
            "|id=1:ASSET_CREATED",
            testEvents.toString()
        )
        assertEquals(
            "TestAsset(name='childAsset', Param1='child', Param2=1.45, instanceId=-1) dependsOn=0",
            FFContext.get<Asset>(asset2).toString()
        )

        FFContext.activate(asset2)
        assertEquals(
            "|id=0:ASSET_CREATED" +
            "|id=1:ASSET_CREATED" +
            "|id=0:ASSET_LOADED" +
            "|id=1:ASSET_LOADED",
            testEvents.toString()
        )
        FFContext.deactivate(asset1)
        assertEquals(
            "|id=0:ASSET_CREATED" +
            "|id=1:ASSET_CREATED" +
            "|id=0:ASSET_LOADED" +
            "|id=1:ASSET_LOADED" +
            "|id=1:ASSET_DISPOSED" +
            "|id=0:ASSET_DISPOSED",
            testEvents.toString()
        )
    }
}