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
import ai.dragonfly.mesh
import ai.dragonfly.mesh.shape.*
import slash.*
import slash.vectorf.*

trait HSL extends HueSaturation { self: WorkingSpace =>

  object HSL extends HueSaturationSpace[HSL] {

    opaque type HSL = VecF[3]

    override lazy val usableGamut: Gamut = new Gamut( Cylinder(sideSegments = 64).toMeshF )

    def apply(values: NArray[Float]): HSL = dimensionCheck(values, 3).asInstanceOf[HSL]

    def clamp(values: NArray[Float]): HSL = {
      dimensionCheck(values, 3)
      clamp(values(0), values(1), values(2))
    }

    /**
     * HSL is the primary type for representing colors in HSL space.
     *
     * @constructor Create a new HSV object from three Float values.  This constructor does not validate input parameters.
     *              For values taken from user input, sensors, or otherwise uncertain sources, consider using the factory method in the Color companion object.
     * @see [[ai.dragonfly.color.ColorVector.hsl]] for a method of constructing HSL objects that validates inputs.
     * @see [[https://en.wikipedia.org/wiki/HSL_and_HSV]] for more information about the HSL color space.
     * @param hue        an angle ranging from [0-360] degrees.  Values outside of this range may cause errors.
     * @param saturation a percentage ranging from [0-100].  Values outside of this range may cause errors.
     * @param lightness  a percentage ranging from [0-100].  Values outside of this range may cause errors.
     * @return an instance of the HSL case class.
     * @example {{{
     * val c = HSL(211f, 75f, 33.3333f)
     * c.toString()  // returns "HSL(211.000,75.000,33.333)"
     * }}}
     */
    def apply(hue: Float, saturation: Float, lightness: Float): HSL = {
      NArray[Float](hue, saturation, lightness).asInstanceOf[HSL]
    }

    def clamp(hue: Float, saturation: Float, lightness: Float): HSL = NArray[Float](
      clampHue(hue),
      clamp0to1(saturation),
      clamp0to1(lightness)
    ).asInstanceOf[HSL]

    /**
     * Factory method for creating instances of the HSL class.  This method validates input parameters and throws an exception
     * if one or more of them lie outside of their allowed ranges.
     *
     * @param saturation an angle ranging from [0-360] degrees.
     * @param hue        a percentage ranging from [0-100].
     * @param lightness  a percentage ranging from [0-100].
     * @return an instance of the HSL case class.
     */
    def getIfValid(hue: Float, saturation: Float, lightness: Float): Option[HSL] = {
      if (validHue(hue) && valid0to1(saturation) && valid0to1(lightness)) Some(apply(hue, saturation, lightness))
      else None
    }

    //inline def toHSL(red: Float, green: Float, blue: Float): HSL =

    override def random(r: scala.util.Random = Random.defaultRandom): HSL = apply(
      NArray[Float](
        r.nextFloat() * 360f,
        r.nextFloat(),
        r.nextFloat()
      )
    )

    override def toVec(hsl: HSL): VecF[3] = VecF[3](
      (hsl(1) * Math.cos(slash.degreesToRadians(hsl(0)))).toFloat,
      (hsl(1) * Math.sin(slash.degreesToRadians(hsl(0)))).toFloat,
      hsl(2)
    )

    def hue(hsl: HSL): Float = hsl(0)

    def saturation(hsl: HSL): Float = hsl(1)

    def lightness(hsl: HSL): Float = hsl(2)

    def fromRGB(rgb: RGB): HSL = {
      val values: NArray[Float] = hueMinMax(rgb.red, rgb.green, rgb.blue)

      val delta: Float = values(2 /*MAX*/) - values(1 /*min*/)
      val L: Float = values(1 /*min*/) + values(2 /*MAX*/)

      HSL.apply(
        values(0),
        if (delta == 0.0) 0f else delta / (1f - Math.abs(L - 1f)),
        0.5f * L, // (min + max) / 2
      )
    }

    override def toRGB(hsl: HSL): RGB = {
      // https://www.rapidtables.com/convert/color/hsl-to-rgb.html
      val C: Float = (1f - Math.abs((2f * hsl.lightness) - 1f)) * hsl.saturation
      RGB.apply(
        HSL.hcxmToRGBvalues(
          hsl.hue,
          C,
          HSL.XfromHueC(hsl.hue, C), // X
          hsl.lightness - (0.5f * C) // m
        )
      )
    }

    override def toXYZ(c: HSL): XYZ = c.toXYZ

    override def toString:String = "HSL"
  }

  type HSL = HSL.HSL

  given CylindricalColorModel[HSL] with {
    extension (hsl: HSL) {
      def hue: Float = HSL.hue(hsl)

      def saturation: Float = HSL.saturation(hsl)

      def lightness: Float = HSL.lightness(hsl)

      override def similarity(that: HSL): Double = HSL.similarity(hsl, that)

      override def copy: HSL = NArray[Float](hue, saturation, lightness).asInstanceOf[HSL]

      def toRGB: RGB = HSL.toRGB(hsl)

      override def toXYZ: XYZ = toRGB.toXYZ

      override def render: String = s"HSL($hue, $saturation, $lightness)"

      /**
       * @return a string representing the color in an SVG friendly way.
       * @example {{{
       * val c = HSL(211f, 75f, 33.3333f)
       * c.svg() // returns "hsl(211.000,75.0%,33.3%)"
       * }}}
       */
      def svg(): String = s"hsl(${f"$hue%1.3f"}, ${f"$saturation%1.1f"}%, ${f"$lightness%1.1f"}%)"
    }
  }
}
