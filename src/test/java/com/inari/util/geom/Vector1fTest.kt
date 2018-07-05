package com.inari.util.geom

import org.junit.Assert.assertEquals


import org.junit.Test

class Vector1fTest {

    @Test
    fun testCreation() {
        var v = com.inari.commons.geom.Vector1f()
        assertEquals("1.0", v.d.toString())

        v = com.inari.commons.geom.Vector1f(3.45f)
        assertEquals("3.45", v.d.toString())
    }

}
