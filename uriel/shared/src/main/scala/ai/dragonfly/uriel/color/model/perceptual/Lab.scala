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

import scala.util.Random

trait Lab { self: WorkingSpace =>

  object Lab extends PerceptualSpace[3, Lab] {

    opaque type Lab = VecF[3]

    override lazy val fullGamut: Gamut = Gamut.fromSpectralSamples(
      cmf,
      (v: VecF[3]) => fromXYZ(XYZ(whitePoint.x * v.x, whitePoint.y * v.y, whitePoint.z * v.z)).vec
    )

    override lazy val usableGamut: Gamut = Gamut.fromRGB( 32, (xyz: XYZ) => fromXYZ(xyz).vec )

    override def random(r: Random): Lab = usableGamut.random(r)

    override def maxDistanceSquared: Double = usableGamut.maxDistSquared

    def apply(values: NArray[Float]): Lab = dimensionCheck(values, 3).asInstanceOf[Lab]

    /**
     * @param L the L* component of the CIE L*a*b* color.
     * @param a the a* component of the CIE L*a*b* color.
     * @param b the b* component of the CIE L*a*b* color.
     * @return an instance of the LAB case class.
     * @example {{{ val c = LAB(72.872, -0.531, 71.770) }}}
     */
    def apply(L: Float, a: Float, b: Float): Lab = apply(NArray[Float](L, a, b))

    inline def f(t: Double): Double = if (t > ϵ) Math.cbrt(t) else (t * `k/116`) + `16/116`

    inline def fInverse(t: Double): Double = if (t > `∛ϵ`) cubeInPlace(t) else (`116/k` * t) - `16/k`

    def L(lab: Lab): Float = lab(0)

    def a(lab: Lab): Float = lab(1)

    def b(lab: Lab): Float = lab(2)

    override def fromVec(v: VecF[3]): Lab = v

    override def fromRGBA(rgba: RGBA): Lab = fromXYZ(rgba.toXYZ)

    /**
     * Requires a reference 'white' because although black provides a lower bound for XYZ values, they have no upper bound.
     *
     * @param xyz an xyz color
     * @return a Laab color
     */
    def fromXYZ(xyz: XYZ): Lab = {
      val fy: Double = f(illuminant.`1/yₙ` * xyz.y)
      apply(
        (116.0 * fy - 16.0).toFloat,
        (500.0 * (f(illuminant.`1/xₙ` * xyz.x) - fy)).toFloat,
        (200.0 * (fy - f(illuminant.`1/zₙ` * xyz.z))).toFloat
      )
    }

    override def fromXYZA(xyza: XYZA): Lab = fromXYZ(xyza.toXYZ)

    override def toString:String = "Lab"

  }

  type Lab = Lab.Lab

  given PerceptualColorModel[3, Lab] with {
    extension (lab: Lab) {

      override inline def copy: Lab = Lab(L, a, b)

      inline def L: Float = Lab.L(lab)

      inline def a: Float = Lab.a(lab)

      inline def b: Float = Lab.b(lab)

      override def similarity(that: Lab): Double = Lab.similarity(lab, that)

      override def vec: VecF[3] = lab.asInstanceOf[VecF[3]].copy

      override def toRGB: RGB = toXYZ.toRGB

      override def toRGBA: RGBA = {
        val rgb: RGB = toRGB
        RGBA(rgb.red, rgb.green, rgb.blue, 1f)
      }

      override def toRGBA(alpha: Float): RGBA = {
        val rgb: RGB = toRGB
        RGBA(rgb.red, rgb.green, rgb.blue, alpha)
      }

      def toXYZ: XYZ = {
        //val white: XYZ = whitePoint //XYZ(illuminant.whitePointValues)
        val fy: Double = `1/116` * (lab.L + 16.0)

        XYZ(
          (Lab.fInverse((0.002 * lab.a) + fy) * illuminant.xₙ).toFloat, // X
          (if (lab.L > kϵ) {
            val l = lab.L + 16.0
              `1/116³` * (l * l * l) * illuminant.yₙ
          } else `1/k` * lab.L * illuminant.yₙ).toFloat, // Y
          (Lab.fInverse(fy - (0.005 * lab.b)) * illuminant.zₙ).toFloat, // Z
        )
      }

      override def toXYZA: XYZA = {
        val xyz = toXYZ
        XYZA(xyz.x, xyz.y, xyz.z, 1f)
      }

      override def toXYZA(alpha: Float): XYZA = {
        val xyz = toXYZ
        XYZA(xyz.x, xyz.y, xyz.z, alpha)
      }

      override def render: String = s"L*a*b*($L,$a,$b)"

    }
  }

  // LabA
  object LabA extends PerceptualSpace[4, LabA] {

    opaque type LabA = VecF[4]

    override lazy val fullGamut: Gamut = Lab.fullGamut

    override lazy val usableGamut: Gamut = Lab.usableGamut

    override def random(r: Random): LabA = {
      val lab: Lab = Lab.random(r)
      LabA(lab.L, lab.a, lab.b, r.nextFloat())
    }

    override def maxDistanceSquared: Double = usableGamut.maxDistSquared + 1.0

    def apply(values: NArray[Float]): LabA = dimensionCheck(values, 4).asInstanceOf[LabA]

    /**
     * @param L the L* component of the CIE L*a*b* color.
     * @param a the a* component of the CIE L*a*b* color.
     * @param b the b* component of the CIE L*a*b* color.
     * @return an instance of the LAB case class.
     * @example {{{ val c = LAB(72.872, -0.531, 71.770) }}}
     */
    def apply(L: Float, a: Float, b: Float): LabA = apply(NArray[Float](L, a, b, 1f))

    /**
     * @param L the L* component of the CIE L*a*b* color.
     * @param a the a* component of the CIE L*a*b* color.
     * @param b the b* component of the CIE L*a*b* color.
     * @param alpha the alpha channel
     * @return an instance of the LAB case class.
     * @example {{{ val c = LAB(72.872, -0.531, 71.770) }}}
     */
    def apply(L: Float, a: Float, b: Float, alpha: Float): LabA = apply(NArray[Float](L, a, b, alpha))

    inline def f(t: Double): Double = if (t > ϵ) Math.cbrt(t) else (t * `k/116`) + `16/116`

    inline def fInverse(t: Double): Double = if (t > `∛ϵ`) cubeInPlace(t) else (`116/k` * t) - `16/k`

    def L(lab: LabA): Float = lab(0)

    def a(lab: LabA): Float = lab(1)

    def b(lab: LabA): Float = lab(2)

    def alpha(lab: LabA): Float = lab(3)

    override def fromVec(v: VecF[4]): LabA = v

    override def fromRGBA(rgba: RGBA): LabA = fromXYZA(rgba.toXYZA)

    /**
     * Requires a reference 'white' because although black provides a lower bound for XYZ values, they have no upper bound.
     *
     * @param xyz an xyz color
     * @return a Laab color
     */
    def fromXYZ(xyz: XYZ): LabA = {
      val lab: Lab = Lab.fromXYZ(xyz)
      LabA(lab.L, lab.a, lab.b)
    }

    override def fromXYZA(xyza: XYZA): LabA = {
      val lab: Lab = Lab.fromXYZA(xyza)
      LabA(lab.L, lab.a, lab.b, xyza.alpha)
    }

    override def toString:String = "LabA"

  }

  type LabA = LabA.LabA

  given PerceptualColorModel[4, LabA] with {
    extension (laba: LabA) {

      override inline def copy: LabA = LabA(a, b, L, alpha)

      inline def L: Float = LabA.L(laba)

      inline def a: Float = LabA.a(laba)

      inline def b: Float = LabA.b(laba)

      inline def alpha: Float = LabA.alpha(laba)

      override def similarity(that: LabA): Double = LabA.similarity(laba, that)

      override def vec: VecF[4] = laba.asInstanceOf[VecF[4]].copy

      override def toRGB: RGB = toXYZ.toRGB

      override def toRGBA: RGBA = {
        val rgb: RGB = toRGB
        RGBA(rgb.red, rgb.green, rgb.blue, alpha)
      }

      override def toRGBA(alpha: Float): RGBA = {
        val rgb: RGB = toRGB
        RGBA(rgb.red, rgb.green, rgb.blue, alpha)
      }

      def toXYZ: XYZ = {
        //val white: XYZ = whitePoint //XYZ(illuminant.whitePointValues)
        val fy: Double = `1/116` * (laba.L + 16.0)

        XYZ(
          (Lab.fInverse((0.002 * laba.a) + fy) * illuminant.xₙ).toFloat, // X
          (if (laba.L > kϵ) {
            val l = laba.L + 16.0
              `1/116³` * (l * l * l) * illuminant.yₙ
          } else `1/k` * laba.L * illuminant.yₙ).toFloat, // Y
          (Lab.fInverse(fy - (0.005 * laba.b)) * illuminant.zₙ).toFloat, // Z
        )
      }

      override def toXYZA: XYZA = {
        val xyz = toXYZ
        XYZA(xyz.x, xyz.y, xyz.z, alpha)
      }

      override def toXYZA(alpha: Float): XYZA = {
        val xyz = toXYZ
        XYZA(xyz.x, xyz.y, xyz.z, alpha)
      }

      override def render: String = s"L*a*b*A($L,$a,$b,$alpha)"

    }
  }
}
