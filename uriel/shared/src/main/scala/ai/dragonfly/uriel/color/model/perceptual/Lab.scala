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

trait Lab { self: WorkingSpace =>

  object Lab extends PerceptualSpace[Lab] {

    opaque type Lab = VecF[3]

    override lazy val fullGamut: Gamut = Gamut.fromSpectralSamples(
      cmf,
      (v: VecF[3]) => toVec(fromXYZ(XYZ(whitePoint.x * v.x, whitePoint.y * v.y, whitePoint.z * v.z)))
    )

    override lazy val usableGamut: Gamut = Gamut.fromRGB( 32, (xyz: XYZ) => toVec(fromXYZ(xyz)) )

    def apply(values: NArray[Float]): Lab = dimensionCheck(values, 3).asInstanceOf[Lab]

    /**
     * @param L the L* component of the CIE L*a*b* color.
     * @param a the a* component of the CIE L*a*b* color.
     * @param b the b* component of the CIE L*a*b* color.
     * @return an instance of the LAB case class.
     * @example {{{ val c = LAB(72.872, -0.531, 71.770) }}}
     */
    def apply(L: Float, a: Float, b: Float): Lab = apply(NArray[Float](a, b, L))

    inline def f(t: Double): Double = if (t > ϵ) Math.cbrt(t) else (t * `k/116`) + `16/116`

    inline def fInverse(t: Double): Double = if (t > `∛ϵ`) cubeInPlace(t) else (`116/k` * t) - `16/k`

    def L(lab: Lab): Float = lab(2)

    def a(lab: Lab): Float = lab(0)

    def b(lab: Lab): Float = lab(1)

    override def fromVec(v: VecF[3]): Lab = v

    override def toVec(lab: Lab): VecF[3] = lab.asInstanceOf[VecF[3]].copy

    override def toRGB(lab: Lab): RGB = lab.toXYZ.toRGB

    override def toXYZ(lab: Lab): XYZ = {
      //val white: XYZ = whitePoint //XYZ(illuminant.whitePointValues)
      val fy: Double = `1/116` * (lab.L + 16.0)

      XYZ(
        (fInverse((0.002 * lab.a) + fy) * illuminant.xₙ).toFloat, // X
        (if (lab.L > kϵ) {
          val l = lab.L + 16.0
          `1/116³` * (l * l * l) * illuminant.yₙ
        } else `1/k` * lab.L * illuminant.yₙ).toFloat, // Y
        (fInverse(fy - (0.005 * lab.b)) * illuminant.zₙ).toFloat, // Z
      )
    }

    /**
     * Requires a reference 'white' because although black provides a lower bound for XYZ values, they have no upper bound.
     *
     * @param xyz
     * @param illuminant
     * @return
     */
    def fromXYZ(xyz: XYZ): Lab = {
      val fy: Double = f(illuminant.`1/yₙ` * xyz.y)

      apply(
        (116.0 * fy - 16.0).toFloat,
        (500.0 * (f(illuminant.`1/xₙ` * xyz.x) - fy)).toFloat,
        (200.0 * (fy - f(illuminant.`1/zₙ` * xyz.z))).toFloat
      )
    }

    override def toString:String = "Lab"
  }

  type Lab = Lab.Lab

  given PerceptualColorModel[Lab] with {
    extension (lab: Lab) {

      override inline def copy: Lab = Lab(a, b, L)

      def L: Float = Lab.L(lab)

      def a: Float = Lab.a(lab)

      def b: Float = Lab.b(lab)

      def toXYZ: XYZ = Lab.toXYZ(lab)

      override def similarity(that: Lab): Double = Lab.similarity(lab, that)

      override def toRGB: RGB = Lab.toRGB(lab)

      override def render: String = s"L*a*b*($L,$a,$b)"
    }
  }
}
