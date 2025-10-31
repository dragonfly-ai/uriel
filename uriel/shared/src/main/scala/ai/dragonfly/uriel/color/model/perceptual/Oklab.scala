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

import ai.dragonfly.uriel.cie.*
import narr.*
import slash.*
import slash.vectorf.*
import slash.matrix.*

import scala.util.Random

trait Oklab { self: WorkingSpace =>

  // https://bottosson.github.io/posts/oklab/
  object Oklab extends PerceptualSpace[3, Oklab] {

    opaque type Oklab = VecF[3]

    val m1: Mat[3, 3] = Mat[3,3](
      0.8189330101, 0.3618667424,-0.1288597137,
      0.0329845436, 0.9293118715, 0.0361456387,
      0.0482003018, 0.2643662691, 0.6338517070
    )

    val m2: Mat[3, 3] = Mat[3, 3](
      0.2104542553, 0.7936177850,-0.0040720468,
      1.9779984951,-2.4285922050, 0.4505937099,
      0.0259040371, 0.7827717662,-0.8086757660
    )

    val m1Inv: Mat[3, 3] = m1.inverse

    val m2Inv: Mat[3, 3] = m2.inverse

    override lazy val fullGamut: Gamut = Gamut.fromRGB( 32, (xyz: XYZ) => fromXYZ(xyz).vec )
      //Gamut.fromSpectralSamples(cmf, (v: VecF[3]) => fromXYZ(XYZ(whitePoint.x * v.x, whitePoint.y * v.y, whitePoint.z * v.z)).vec)

    override lazy val usableGamut: Gamut = Gamut.fromRGB( 32, (xyz: XYZ) => fromXYZ(xyz).vec )

    override def random(r: Random): Oklab = usableGamut.random(r)

    override def maxDistanceSquared: Double = usableGamut.maxDistSquared

    override def apply(values: NArray[Float]): Oklab = dimensionCheck(values, 3).asInstanceOf[VecF[3]]

    /**
     * @param L the L* component of the CIE L*a*b* color.
     * @param a the a* component of the CIE L*a*b* color.
     * @param b the b* component of the CIE L*a*b* color.
     * @return an instance of the LAB case class.
     * @example {{{ val c = LAB(72.872, -0.531, 71.770) }}}
     */
    def apply(L: Float, a: Float, b: Float): Oklab = VecF[3](L, a, b)

    def L(lab: Oklab): Float = lab(0)

    def a(lab: Oklab): Float = lab(1)

    def b(lab: Oklab): Float = lab(2)

    override def fromVec(v: VecF[3]): Oklab = v

    override def fromRGBA(rgba: RGBA): Oklab = fromXYZ(rgba.toXYZ)

    /**
     * Requires a reference 'white' because although black provides a lower bound for XYZ values, they have no upper bound.
     *
     * @param xyz an xyz color
     * @return a Laab color
     */
    def fromXYZ(xyz: XYZ): Oklab = {
      val lms: slash.vector.Vec[3] = m1 * xyz.vec.toVec
      val temp: VecF[3] = (m2 * slash.vector.Vec[3](`∛`(lms(0)), `∛`(lms(1)), `∛`(lms(2)))).toVecF
      Oklab( temp(0), temp(1), temp(2) )
    }

    override def fromXYZA(xyza: XYZA): Oklab = fromXYZ( xyza.toXYZ )

    override def toString:String = "Oklab"

  }

  type Oklab = Oklab.Oklab

  given PerceptualColorModel[3, Oklab] with {
    extension (oklab: Oklab) {

      override inline def copy: Oklab = Oklab(L, a, b)

      inline def L: Float = Oklab.L(oklab)

      inline def a: Float = Oklab.a(oklab)

      inline def b: Float = Oklab.b(oklab)

      override def similarity(that: Oklab): Double = Oklab.similarity(oklab, that)

      override def vec: VecF[3] = oklab.asInstanceOf[VecF[3]].copy

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
        import slash.vector.*
        import Oklab.m1Inv
        import Oklab.m2Inv

        val lmsPrime: Vec[3] = m2Inv * oklab.vec.toVec
        val temp: Vec[3] = m1Inv * Vec[3](
          cubeInPlace(lmsPrime(0)),
          cubeInPlace(lmsPrime(1)),
          cubeInPlace(lmsPrime(2))
        )
        XYZ(temp.x.toFloat, temp.y.toFloat, temp.z.toFloat)
      }

      override def toXYZA: XYZA = {
        val xyz = toXYZ
        XYZA(xyz.x, xyz.y, xyz.z, 1f)
      }

      override def toXYZA(alpha: Float): XYZA = {
        val xyz = toXYZ
        XYZA(xyz.x, xyz.y, xyz.z, alpha)
      }

      override def render: String = s"Oklab($L,$a,$b)"

    }
  }

  // OklabA

  object OklabA extends PerceptualSpace[4, OklabA] {

    opaque type OklabA = VecF[4]

    val m1: Mat[4, 4] = Mat[4,4](
      0.8189330101, 0.3618667424,-0.1288597137, 0,
      0.0329845436, 0.9293118715, 0.0361456387, 0,
      0.0482003018, 0.2643662691, 0.6338517070, 0,
      0, 0, 0, 1.0
    )

    val m2: Mat[4, 4] = Mat[4, 4](
      0.2104542553, 0.7936177850,-0.0040720468, 0,
      1.9779984951,-2.4285922050, 0.4505937099, 0,
      0.0259040371, 0.7827717662,-0.8086757660, 0,
      0, 0, 0, 1.0
    )

    val m1Inv: Mat[4, 4] = m1.inverse

    val m2Inv: Mat[4, 4] = m2.inverse

    override lazy val fullGamut: Gamut = Oklab.fullGamut

    override lazy val usableGamut: Gamut = Oklab.usableGamut

    override def random(r: Random): OklabA = {
      val oklab:Oklab = Oklab.random(r)
      OklabA(oklab.L, oklab.a, oklab.b, r.nextFloat())
    }

    override def maxDistanceSquared: Double = 1.0 + Oklab.maxDistanceSquared

    override def apply(values: NArray[Float]): OklabA = dimensionCheck(values, 4).asInstanceOf[VecF[4]]

    /**
     * @param L the L* component of the CIE L*a*b* color.
     * @param a the a* component of the CIE L*a*b* color.
     * @param b the b* component of the CIE L*a*b* color.
     * @return an instance of the LAB case class.
     * @example {{{ val c = LAB(72.872, -0.531, 71.770) }}}
     */
    def apply(L: Float, a: Float, b: Float): OklabA = VecF[4](L, a, b, 1f)

    /**
     * @param L the L* component of the CIE L*a*b* color.
     * @param a the a* component of the CIE L*a*b* color.
     * @param b the b* component of the CIE L*a*b* color.
     * @param alpha the alpha channel
     * @return an instance of the LAB case class.
     * @example {{{ val c = LAB(72.872, -0.531, 71.770) }}}
     */
    def apply(L: Float, a: Float, b: Float, alpha:Float): OklabA = VecF[4](L, a, b, alpha)

    def L(lab: OklabA): Float = lab(0)

    def a(lab: OklabA): Float = lab(1)

    def b(lab: OklabA): Float = lab(2)

    def alpha(lab: OklabA): Float = lab(3)

    override def fromVec(v: VecF[4]): OklabA = v

    override def fromRGBA(rgba: RGBA): OklabA = fromXYZ(rgba.toXYZ)

    /**
     * Requires a reference 'white' because although black provides a lower bound for XYZ values, they have no upper bound.
     *
     * @param xyz an xyz color
     * @return a Laab color
     */
    def fromXYZ(xyz: XYZ): OklabA = {
      val oklab:Oklab = Oklab.fromXYZ(xyz)
      OklabA(oklab.L, oklab.a, oklab.b, 1f)
    }

    override def fromXYZA(xyza: XYZA): OklabA = {
      val lms: slash.vector.Vec[4] = m1 * xyza.vec.toVec
      val temp: VecF[4] = (m2 * slash.vector.Vec[4](`∛`(lms(0)), `∛`(lms(1)), `∛`(lms(2)))).toVecF
      OklabA( temp(0), temp(1), temp(2), temp(3) )
    }

    override def toString:String = "OklabA"

  }

  type OklabA = OklabA.OklabA

  given PerceptualColorModel[4, OklabA] with {
    extension (oklaba: OklabA) {

      override inline def copy: OklabA = OklabA(L, a, b)

      inline def L: Float = OklabA.L(oklaba)

      inline def a: Float = OklabA.a(oklaba)

      inline def b: Float = OklabA.b(oklaba)

      inline def alpha: Float = OklabA.alpha(oklaba)

      override def similarity(that: OklabA): Double = OklabA.similarity(oklaba, that)

      override def vec: VecF[4] = oklaba.asInstanceOf[VecF[4]].copy

      override def toRGB: RGB = toXYZ.toRGB

      override def toRGBA: RGBA = {
        val rgb: RGB = toRGB
        RGBA(rgb.red, rgb.green, rgb.blue, alpha)
      }

      override def toRGBA(alpha: Float): RGBA = {
        val rgb: RGB = toRGB
        RGBA(rgb.red, rgb.green, rgb.blue, alpha)
      }

      def toXYZ: XYZ = Oklab(L, a, b).toXYZ

      override def toXYZA: XYZA = {
        val xyz = toXYZ
        XYZA(xyz.x, xyz.y, xyz.z, alpha)
      }

      override def toXYZA(alpha: Float): XYZA = {
        val xyz = toXYZ
        XYZA(xyz.x, xyz.y, xyz.z, alpha)
      }

      override def render: String = s"OklabA($L,$a,$b,$alpha)"

    }
  }

}