/*
 * Copyright 2023 dragonfly.ai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.dragonfly.uriel.color.model.perceptual

import narr.*

import ai.dragonfly.uriel.cie.*
import ai.dragonfly.uriel.cie.Constant.*
import slash.*
import slash.vectorf.*

trait Luv { self: WorkingSpace =>

  object UV {

    //private inline def getWeight(xyz: XYZ): Float =

    def fromXYZ(xyz: XYZ): UV = {

      var w: Float = 1f / (xyz.x + xyz.y + xyz.z)

      val x: Float = w * xyz.x
      val y: Float = w * xyz.y
      w = 1f / (6f * y - x + 1.5f)
      if (w.isNaN) UV(0f, 0f)
      else UV(2f * x * w, 4.5f * y * w)
    }
  }

  case class UV(u: Double, v: Double) {
    def xy: slash.vector.Vec[2] = {
      val denominator: Double = (6.0 * u) - (16.0 * v) + 12.0
      slash.vector.Vec[2](
        (9.0 * u) / denominator, // X
        (9.0 * v) / denominator // y
      )
    }
  }

  object Luv extends PerceptualSpace[Luv] {

    opaque type Luv = VecF[3]

    override lazy val fullGamut: Gamut = Gamut.fromSpectralSamples(
      cmf,
      (v: VecF[3]) => toVec(fromXYZ(XYZ(whitePoint.x * v.x, whitePoint.y * v.y, whitePoint.z * v.z)))
    )

    override lazy val usableGamut: Gamut = Gamut.fromRGB(32, (xyz: XYZ) => toVec(fromXYZ(xyz)))
    
    def apply(values: NArray[Float]): Luv = dimensionCheck(values, 3).asInstanceOf[Luv]

    /**
     * @constructor Create a new SlowSlimLuv object from three float values.  This constructor does not validate input parameters.
     * @param L the L* component of the CIE L*u*v* color.
     * @param u the u* component of the CIE L*u*v* color.
     * @param v the v* component of the CIE L*u*v* color.
     * @return an instance of the SlowSlimLuv case class.
     * @example {{{ val c = SlowSlimLuv(14.756, -3.756, -58.528) }}}
     */

    def apply(L: Float, u: Float, v: Float): Luv = apply(NArray[Float](u, v, L))

    // XYZ to LUV and helpers:

    val UV(uₙ: Double, vₙ: Double) = UV.fromXYZ(XYZ(illuminant.whitePointValues))

    def fL(t: Double): Double = if (t > ϵ) 116.0 * Math.cbrt(t) - 16.0 else k * t

    override def toRGB(c: Luv): RGB = c.toRGB

    override def toXYZ(c: Luv): XYZ = c.toXYZ

    def fromXYZ(xyz: XYZ): Luv = {

      val `Y/Yₙ`: Double = xyz.y / illuminant.yₙ.toDouble

      val `L⭑`:Double = fL(`Y/Yₙ`)

      val uv: UV = UV.fromXYZ(xyz)

      val `u⭑`:Double = 13.0 * `L⭑` * (uv.u - uₙ)
      val `v⭑`:Double = 13.0 * `L⭑` * (uv.v - vₙ)

      apply(
        `L⭑`.toFloat,
        if (`u⭑`.isNaN) 0f else `u⭑`.toFloat,
        if (`v⭑`.isNaN) 0f else `v⭑`.toFloat
      )
    }

    def L(luv: Luv): Float = luv(2)

    def u(luv: Luv): Float = luv(0)

    def v(luv: Luv): Float = luv(1)

    override def fromVec(v: VecF[3]): Luv = v

    override def toVec(luv: Luv): VecF[3] = luv.asInstanceOf[VecF[3]].copy

    override def toString:String = "Luv"
  }

  /**
   * LUV is the primary type to represent colors from the CIE L*u*v* color space.
   *
   * @see [[https://en.wikipedia.org/wiki/CIELUV]] for more information on CIE L*u*v*.
   */

  type Luv = Luv.Luv

  given PerceptualColorModel[Luv] with {
    extension (luv: Luv) {

      def L: Float = Luv.L(luv)

      def u: Float = Luv.u(luv)

      def v: Float = Luv.v(luv)

      override def render: String = s"L⭑u⭑v⭑($L,$u,$v)"

      override def copy: Luv = Luv(u, v, L)

      // LUV to XYZ and helpers:
      def flInverse(t: Float): Double = if (t > kϵ) {
        cubeInPlace(`1/116` * (t + 16.0)) //`1/116³` * cubeInPlace(t + 16f) // ((L+16)/116)^3 = (L + 16)^3 / 116^3 = (L + 16)^3 / 1560896f
      } else `1/k` * t

      def toXYZ: XYZ = {

        val uₓ: Double = (u / (13.0 * L)) + Luv.uₙ
        val vₓ: Double = (v / (13.0 * L)) + Luv.vₙ

        val Y: Double = flInverse(L)
        val X: Double = 9.0 * Y * uₓ / (4.0 * vₓ)
        val Z: Double = (3.0 * Y / vₓ) - (5.0 * Y) - (X / 3.0)

        XYZ(X.toFloat, Y.toFloat, Z.toFloat)
      }

      override def toRGB: RGB = toXYZ.toRGB

      override def similarity(that: Luv): Double = Luv.similarity(luv, that)

    }
  }

}
