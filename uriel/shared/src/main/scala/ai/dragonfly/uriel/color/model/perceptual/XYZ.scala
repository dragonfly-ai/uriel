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
import ai.dragonfly.uriel.cie.WorkingSpace
import slash.*
import slash.vectorf.*
import slash.matrixf.*

import scala.language.implicitConversions
import scala.util.Random

/**
 * From: https://en.wikipedia.org/wiki/CIE_1931_color_space
 * "The CIE XYZ color space encompasses all color sensations that are visible to a person with average eyesight.
 * That is why CIE XYZ (Tristimulus values) is a device-invariant representation of color."
 *
 * "In the CIE 1931 model, Y is the luminance, Z is quasi-equal to blue (of CIE RGB), and X is a mix of the three CIE RGB
 * curves chosen to be non-negative."
 *
 * "... the Z value is solely made up of the S cone response, the Y value a mix of L and M responses, and X value a mix
 * of all three. This fact makes XYZ values analogous to, but different from, the LMS cone responses of the human eye."
 */

trait XYZ { self:WorkingSpace =>

  object XYZ extends PerceptualSpace[3, XYZ] {

    opaque type XYZ = VecF[3]

    override lazy val fullGamut: Gamut = Gamut.fromSpectralSamples(cmf, illuminant)

    override lazy val usableGamut: Gamut = Gamut.fromRGB(transform = (xyz:XYZ) => xyz)

    override lazy val maxDistanceSquared: Double = usableGamut.maxDistSquared

    def apply(values: NArray[Float]): XYZ = dimensionCheck(values, 3).asInstanceOf[XYZ]

    def apply(x: Float, y: Float, z: Float): XYZ = VecF[3](x, y, z)

    def x(xyz: XYZ): Float = xyz(0)

    def y(xyz: XYZ): Float = xyz(1)

    def z(xyz: XYZ): Float = xyz(2)

    override def random(r: Random):XYZ = usableGamut.random(r)

    override def fromVec(v: VecF[3]): XYZ = v.copy

    override def fromRGBA(rgba: RGBA): XYZ = rgba.toXYZ

    override def fromXYZ(xyz: XYZ): XYZ = copy(xyz)

    override def fromXYZA(xyza: XYZA): XYZ = apply(xyza.x, xyza.y, xyza.z)

    def copy(xyz:XYZ):XYZ = {
      val v: VecF[3] = xyz
      v.copy
    }

    override def toString:String = "XYZ"
  }

  type XYZ = XYZ.XYZ

  given PerceptualColorModel[3, XYZ] with {
    extension (xyz: XYZ) {
      override inline def copy: XYZ = XYZ.copy(xyz)

      inline def x: Float = XYZ.x(xyz)

      inline def y: Float = XYZ.y(xyz)

      inline def z: Float = XYZ.z(xyz)

      override def similarity(that: XYZ): Double = XYZ.similarity(xyz, that)

      override def vec:VecF[3] = xyz.asInstanceOf[VecF[3]].copy

      override def toRGB: RGB = {
        val temp: NArray[Float] = (M_inverse * vec.asColumnMatrix).values
        var i: Int = 0
        while (i < temp.length) {
          temp(i) = transferFunction.encode(temp(i))
          i += 1
        }
        RGB(temp)
      }

      override def toRGBA: RGBA = {
        val rgb: RGB = toRGB
        RGBA(rgb.red, rgb.green, rgb.blue, 1f)
      }

      override def toRGBA(alpha: Float): RGBA = {
        val rgb: RGB = toRGB
        RGBA(rgb.red, rgb.green, rgb.blue, alpha)
      }

      override def toXYZ: XYZ = XYZ(x, y, z)
      override def toXYZA: XYZA = XYZA(xyz.x, xyz.y, xyz.z, 1f)
      override def toXYZA(alpha: Float): XYZA = XYZA(xyz.x, xyz.y, xyz.z, alpha)

      override def render: String = s"XYZ($x,$y,$z)"

    }
  }


  object XYZA extends PerceptualSpace[4, XYZA] {

    opaque type XYZA = VecF[4]

    override lazy val fullGamut: Gamut = Gamut.fromSpectralSamples(cmf, illuminant)

    override lazy val usableGamut: Gamut = XYZ.usableGamut

    override lazy val maxDistanceSquared: Double = usableGamut.maxDistSquared + 1.0

    def apply(values: NArray[Float]): XYZA = dimensionCheck(values, 4).asInstanceOf[XYZA]

    override def apply(x: Float, y: Float, z: Float): XYZA = VecF[4](x, y, z, 1f)

    def apply(x: Float, y: Float, z: Float, alpha: Float): XYZA = VecF[4](x, y, z, alpha)

    def x(xyz: XYZA): Float = xyz(0)

    def y(xyz: XYZA): Float = xyz(1)

    def z(xyz: XYZA): Float = xyz(2)

    def alpha(xyz: XYZA): Float = xyz(3)

    def copy(xyza:XYZA):XYZA = {
      val v: VecF[4] = xyza
      v.copy
    }

    override def fromVec(v: VecF[4]): XYZA = v.copy


    override def euclideanDistanceSquaredTo(c1: XYZA, c2: XYZA): Double = c1.vec.euclideanDistanceSquaredTo(c2.vec)

//    override def vecTo_sRGB_ARGB(v: VecF[4]): ai.dragonfly.mesh.sRGB.ARGB32 = {
//      val c: ai.dragonfly.mesh.sRGB.ARGB32 = Gamut.XYZtoARGB32(
//        fromVec(v).toXYZ.asInstanceOf[VecF[3]]
//      ).asInstanceOf[ai.dragonfly.mesh.sRGB.ARGB32]
//      ai.dragonfly.mesh.sRGB.ARGB32((255 * v(3)).toInt, c.red, c.green, c.blue)
//    }

    def fromRGB(rgb:RGB, alpha:Float):XYZA = {
      val xyz: XYZ = rgb.toXYZ
      XYZA(xyz.x, xyz.y, xyz.z, alpha)
    }

    override def fromRGBA(rgba: RGBA): XYZA = fromRGB(RGB(rgba.red, rgba.green, rgba.blue), rgba.alpha)

    override def fromXYZ(xyz: XYZ): XYZA = apply(xyz.x, xyz.y, xyz.z, 1f)

    override def fromXYZA(xyza: XYZA): XYZA = copy(xyza)

    def fromXYZ(xyz: XYZ, alpha: Float = 1f): XYZA = apply(xyz.x, xyz.y, xyz.z, alpha)

    override def random(r: Random): XYZA = fromXYZ(XYZ.random(r), r.nextFloat)

    override def toString:String = "XYZA"

  }

  type XYZA = XYZA.XYZA

  given PerceptualColorModel[4, XYZA] with {
    extension (xyza: XYZA) {
      override inline def copy: XYZA = XYZA.copy(xyza)

      inline def x: Float = XYZA.x(xyza)

      inline def y: Float = XYZA.y(xyza)

      inline def z: Float = XYZA.z(xyza)

      inline def alpha: Float = XYZA.alpha(xyza)

      override def similarity(that: XYZA): Double = XYZA.similarity(xyza, that)

      override def vec:VecF[4] = XYZA.asInstanceOf[VecF[4]].copy

      override def toRGB: RGB = XYZ(x, y, z).toRGB

      override def toRGBA: RGBA = {
        val rgb: RGB = toRGB
        RGBA(rgb.red, rgb.green, rgb.blue, alpha)
      }

      override def toRGBA(alpha: Float): RGBA = {
        val rgb: RGB = toRGB
        RGBA(rgb.red, rgb.green, rgb.blue, alpha)
      }

      override def toXYZ: XYZ = XYZ(x, y, z)

      override def toXYZA: XYZA = XYZA.copy(xyza)

      override def toXYZA(alpha: Float): XYZA = XYZA(xyza.x, xyza.y, xyza.z, alpha)

      override def render: String = s"XYZA($x,$y,$z, $alpha)"

    }
  }
}
