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

package ai.dragonfly.uriel.color.model.huesat

import narr.*
import ai.dragonfly.uriel.*
import ai.dragonfly.uriel.cie.WorkingSpace
import ai.dragonfly.uriel.color.model.*
import ai.dragonfly.mesh.*
import ai.dragonfly.mesh.shape.*
import slash.*
import slash.vectorf.*

trait HSV extends HueSaturation { self: WorkingSpace =>

  object HSV extends HueSaturationSpace[HSV] {

    opaque type HSV = VecF[3]

    override lazy val usableGamut: Gamut = new Gamut( Cylinder(capSegments = 6).toMeshF )

    def apply(values: NArray[Float]): HSV = dimensionCheck(values, 3).asInstanceOf[HSV]

    def clamp(values: NArray[Float]): HSV = {
      dimensionCheck(values.length, 3)
      clamp(values(0), values(1), values(2))
    }

    /**
     * HSV is the primary type for representing colors in HSV space.
     *
     * @constructor Create a new HSV object from three Float values.  This constructor does not validate
     *              input parameters.  For values taken from user input, sensors, or otherwise uncertain sources, consider using
     *              the factory method in the Color companion object.
     * @see [[ai.dragonfly.color.HSV.getIfValid]] for a method of constructing HSV objects that validates inputs.
     * @see [[https://en.wikipedia.org/wiki/HSL_and_HSV]] for more information about the HSV color space.
     * @param hue        an angle ranging from [0-360] degrees.  Values outside of this range may cause errors.
     * @param saturation a percentage ranging from [0-100].  Values outside of this range may cause errors.
     * @param value      a percentage ranging from [0-100].  Values outside of this range may cause errors.
     * @return an instance of the HSV case class.
     * @example {{{
     * val c = HSV(211f, 75f, 33.3333f)
     * c.toString()  // returns "HSV(211.000,75.000,33.333)"
     * }}}
     */

    def apply(hue: Float, saturation: Float, value: Float): HSV = {
      NArray[Float](hue, saturation, value).asInstanceOf[HSV]
    }

    def clamp(hue: Float, saturation: Float, value: Float): HSV = NArray[Float](
      clampHue(hue),
      clamp0to1(saturation),
      clamp0to1(value)
    ).asInstanceOf[HSV]

    /**
     * Factory method for creating instances of the HSV class.  This method validates input parameters and throws an exception
     * if one or more of them lie outside of their allowed ranges.
     *
     * @param saturation an angle ranging from [0-360] degrees.
     * @param hue        a percentage ranging from [0-100].
     * @param value      a percentage ranging from [0-100].
     * @return an instance of the HSV case class.
     */
    def getIfValid(hue: Float, saturation: Float, value: Float): Option[HSV] = {
      if (validHue(hue) && valid0to1(saturation) && valid0to1(saturation)) Some(apply(hue, saturation, value))
      else None
    }

    override def random(r: scala.util.Random = Random.defaultRandom): HSV = apply(
      NArray[Float](
        r.nextFloat() * 360f,
        r.nextFloat(),
        r.nextFloat()
      )
    )

    def fromRGB(rgb: RGB): HSV = {
      val values: NArray[Float] = hueMinMax(rgb.red, rgb.green, rgb.blue)
      values(1) = { // S
        if (values(2 /*MAX*/) == 0.0) 0.0
        else (values(2 /*MAX*/) - values(1 /*min*/)) / values(2 /*MAX*/)
      }
      values.asInstanceOf[HSV]
    }

    override def toRGB(hsv: HSV): RGB = {
      // https://www.rapidtables.com/convert/color/hsv-to-rgb.html
      val C = hsv.value * hsv.saturation
      HSV.hcxmToRGBvalues(hsv.hue, C, HSV.XfromHueC(hsv.hue, C), hsv.value - C).asInstanceOf[RGB]
    }

    override def toXYZ(c: HSV): XYZ = c.toXYZ

    override def toVec(hsv: HSV): VecF[3] = VecF[3](
      (hsv(1) * Math.cos(slash.degreesToRadians(hsv(0)))).toFloat,
      (hsv(1) * Math.sin(slash.degreesToRadians(hsv(0)))).toFloat,
      hsv(2)
    )

    def hue(hsv: HSV): Float = hsv(0)

    def saturation(hsv: HSV): Float = hsv(1)

    def value(hsv: HSV): Float = hsv(2)

    override def toString:String = "HSV"
  }

  type HSV = HSV.HSV

  given CylindricalColorModel[HSV] with {
    extension (hsv: HSV) {

      //case class HSV private(override val values: NArray[Float]) extends HueSaturation[HSV] {

      def hue: Float = HSV.hue(hsv)

      def saturation: Float = HSV.saturation(hsv)

      def value: Float = HSV.value(hsv)

      // https://www.rapidtables.com/convert/color/hsv-to-rgb.html
      def toRGB: RGB = HSV.toRGB(hsv)

      override def copy: HSV = NArray[Float](hue, saturation, value).asInstanceOf[HSV]

      override def similarity(that: HSV): Double = HSV.similarity(hsv, that)

      override def render: String = s"HSV($hue, $saturation, $value)"

      override def toXYZ: XYZ = toRGB.toXYZ
    }

  }
}
