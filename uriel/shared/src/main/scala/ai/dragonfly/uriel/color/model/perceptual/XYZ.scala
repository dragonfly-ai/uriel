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

  object XYZ extends PerceptualSpace[XYZ] {

    opaque type XYZ = VecF[3]

    override lazy val fullGamut: Gamut = Gamut.fromSpectralSamples(cmf, illuminant)

    override lazy val usableGamut: Gamut = Gamut.fromRGB(transform = (xyz:XYZ) => xyz)

    def apply(values: NArray[Float]): XYZ = dimensionCheck(values, 3).asInstanceOf[XYZ]

    def apply(x: Float, y: Float, z: Float): XYZ = VecF[3](x, y, z)

    def x(xyz: XYZ): Float = xyz(0)

    def y(xyz: XYZ): Float = xyz(1)

    def z(xyz: XYZ): Float = xyz(2)

    override def toRGB(xyz: XYZ): RGB = {
      val temp: NArray[Float] = (M_inverse * xyz.asColumnMatrix).values
      var i: Int = 0
      while (i < temp.length) {
        temp(i) = transferFunction.encode(temp(i))
        i += 1
      }
      RGB(temp)
    }

    override def toXYZ(c: XYZ): XYZ = c.copy
    override def fromXYZ(xyz: XYZ): XYZ = copy(xyz)

    def copy(xyz:XYZ):XYZ = {
      val v: VecF[3] = xyz
      v.copy
    }

    override def fromVec(v: VecF[3]): XYZ = v.copy

    override def toVec(xyz: XYZ): VecF[3] = copy(xyz)

    override def toString:String = "XYZ"
  }

  type XYZ = XYZ.XYZ

  given PerceptualColorModel[XYZ] with {
    extension (xyz: XYZ) {
      override inline def copy: XYZ = XYZ.copy(xyz)

      inline def x: Float = XYZ.x(xyz)

      inline def y: Float = XYZ.y(xyz)

      inline def z: Float = XYZ.z(xyz)

      override def similarity(that: XYZ): Double = XYZ.similarity(xyz, that)

      override def toXYZ: XYZ = XYZ(x, y, z)

      override def vec:VecF[3] = XYZ.asInstanceOf[VecF[3]].copy

      override def toRGB: RGB = XYZ.toRGB(xyz)

      override def render: String = s"XYZ($x,$y,$z)"

    }
  }
}
