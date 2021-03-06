package com.inari.util.geom

/** A simple one dimensional vector within float precision
 * @param d The vector value
 * */
data class Vector1f constructor(
    @JvmField var d: Float = 1.0f
){

    operator fun invoke(d: Float) { this.d = d }
    operator fun invoke(v: Vector1f) { this.d = v.d }
    operator fun plus(dd: Float): Vector1f {
        this.d += dd
        return this
    }
    operator fun plus(v: Vector1f): Vector1f {
        this.d += v.d
        return this
    }
    operator fun minus(dd: Float): Vector1f {
        this.d -= dd
        return this
    }
    operator fun minus(v: Vector1f): Vector1f {
        this.d -= v.d
        return this
    }
    operator fun times(dd: Float): Vector1f {
        this.d *= dd
        return this
    }
    operator fun div(dd: Float): Vector1f {
        this.d /= dd
        return this
    }
    override fun toString(): String = "[d=$d]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this::class != other::class) return false
        other as Vector1f
        if (d != other.d) return false
        return true
    }
    override fun hashCode(): Int = d.hashCode()
}

/** A simple one dimensional vector within integer precision
 * @param d The vector value
 * */
data class Vector1i constructor(
    @JvmField var d: Int = 1
) {

    operator fun invoke(d: Int) { this.d = d }
    operator fun invoke(v: Vector1i) { this.d = v.d }
    operator fun plus(dd: Int): Vector1i {
        this.d += dd
        return this
    }
    operator fun plus(v: Vector1i): Vector1i {
        this.d += v.d
        return this
    }
    operator fun minus(dd: Int): Vector1i {
        this.d -= dd
        return this
    }
    operator fun minus(v: Vector1i): Vector1i {
        this.d -= v.d
        return this
    }
    operator fun times(dd: Int): Vector1i {
        this.d *= dd
        return this
    }
    operator fun div(dd: Int): Vector1i {
        this.d /= dd
        return this
    }
    override fun toString(): String = "[d=$d]"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this::class != other::class) return false
        other as Vector1i
        if (d != other.d) return false
        return true
    }
    override fun hashCode(): Int = d
}

/** A simple two-dimensional (x/y) vector within float precision
 * @param dx the vectors value on x-axis
 * @param dy the vectors value on y-axis
 * */
data class Vector2f constructor(
    @JvmField var dx: Float = 1.0f,
    @JvmField var dy: Float = 1.0f
) {
    operator fun invoke(dx: Float, dy: Float) {
        this.dx = dx
        this.dy = dy
    }
    operator fun invoke(v: Vector2f) {
        this.dx = v.dx
        this.dy = v.dy
    }
    operator fun plus(v: Vector2f): Vector2f {
        this.dx += v.dx
        this.dy += v.dy
        return this
    }
    operator fun minus(v: Vector2f): Vector2f {
        this.dx -= v.dx
        this.dy -= v.dy
        return this
    }
    operator fun times(dd: Float): Vector2f {
        this.dx *= dd
        this.dy *= dd
        return this
    }
    operator fun times(v: Vector2f): Vector2f {
        this.dx *= v.dx
        this.dy *= v.dy
        return this
    }
    operator fun div(dd: Float): Vector2f {
        this.dx /= dd
        this.dy /= dd
        return this
    }
    operator fun div(v: Vector2f): Vector2f {
        this.dx /= v.dx
        this.dy /= v.dy
        return this
    }

    override fun toString(): String = "[dx=$dx,dy=$dy]"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this::class != other::class) return false
        other as Vector2f
        if (dx != other.dx && dy != other.dy) return false
        return true
    }
    override fun hashCode(): Int {
        var result = dx.hashCode()
        result = 31 * result + dy.hashCode()
        return result
    }

}

/** A simple two-dimensional (x/y) vector within integer precision
 * @param dx the vectors value on x-axis
 * @param dy the vectors value on y-axis
 * */
data class Vector2i constructor(
    @JvmField var dx: Int = 1,
    @JvmField var dy: Int = 1
){

    operator fun invoke(dx: Int, dy: Int) {
        this.dx = dx
        this.dy = dy
    }
    operator fun invoke(v: Vector2i) {
        this.dx = v.dx
        this.dy = v.dy
    }
    operator fun plus(v: Vector2i): Vector2i {
        this.dx += v.dx
        this.dy += v.dy
        return this
    }
    operator fun minus(v: Vector2i): Vector2i {
        this.dx -= v.dx
        this.dy -= v.dy
        return this
    }
    operator fun times(dd: Int): Vector2i {
        this.dx *= dd
        this.dy *= dd
        return this
    }
    operator fun times(v: Vector2i): Vector2i {
        this.dx *= v.dx
        this.dy *= v.dy
        return this
    }
    operator fun div(dd: Int): Vector2i {
        this.dx /= dd
        this.dy /= dd
        return this
    }
    operator fun div(v: Vector2i): Vector2i {
        this.dx /= v.dx
        this.dy /= v.dy
        return this
    }

    override fun toString(): String = "[dx=$dx,dy=$dy]"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this::class != other::class) return false
        other as Vector2i
        if (dx != other.dx && dy != other.dy) return false
        return true
    }
    override fun hashCode(): Int {
        var result = dx
        result = 31 * result + dy
        return result
    }
}