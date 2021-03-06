package com.inari.util.geom

import org.junit.Assert.assertEquals
import org.junit.Test

class Vector2iTest {

    @Test
    fun testCreation() {
        var v = Vector2i()
        assertEquals("1", v.dx.toString())
        assertEquals("1", v.dy.toString())

        v = Vector2i(3, 56)
        assertEquals("3", v.dx.toString())
        assertEquals("56", v.dy.toString())
    }

}
