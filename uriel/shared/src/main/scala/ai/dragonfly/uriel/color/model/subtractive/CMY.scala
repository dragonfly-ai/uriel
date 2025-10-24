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

package ai.dragonfly.uriel.color.model.subtractive

import narr.*
import ai.dragonfly.uriel.cie.WorkingSpace
import ai.dragonfly.mesh.shape.*
import ai.dragonfly.uriel.*
import slash.*
import slash.vectorf.*

trait CMY { self: WorkingSpace =>

  object CMY extends VectorSpace[3, CMY] {

    opaque type CMY = VecF[3]

    override lazy val usableGamut: Gamut = new Gamut( Cube(1.0, 32).toMeshF )

    override val maxDistanceSquared: Double = 3.0

    def apply(values: NArray[Float]): CMY = dimensionCheck(values, 3).asInstanceOf[CMY]

    def apply(cyan: Float, magenta: Float, yellow: Float): CMY = apply(NArray[Float](cyan, magenta, yellow))

    /**
     * Factory method for creating instances of the CMY class.
     * This method validates input parameters at the cost of some performance.
     *
     * @param cyan    a value between [0-1]
     * @param magenta a value between [0-1]
     * @param yellow  a value between [0-1]
     * @return an instance of the CMY class.
     */
    def getIfValid(cyan: Float, magenta: Float, yellow: Float): Option[CMY] = {
      if (valid0to1(cyan, magenta, yellow)) Some(apply(cyan, magenta, yellow))
      else None
    }

    override def random(r: scala.util.Random = Random.defaultRandom): CMY = apply(
      NArray[Float](
        r.nextFloat(),
        r.nextFloat(),
        r.nextFloat()
      )
    )

    def cyan(cmy: CMY): Float = cmy(0)

    def magenta(cmy: CMY): Float = cmy(1)

    def yellow(cmy: CMY): Float = cmy(2)

    override def euclideanDistanceSquaredTo(cmy1: CMY, cmy2: CMY): Double = cmy1.euclideanDistanceSquaredTo(cmy2)

    override def fromVec(v: VecF[3]): CMY = v.copy

    def fromRGB(rgb: RGB): CMY = apply(
      clamp0to1(
        1f - rgb.red,
        1f - rgb.green,
        1f - rgb.blue
      )
    )

    override def fromRGBA(rgba: RGBA): CMY = apply(
      clamp0to1(
        1f - rgba.red,
        1f - rgba.green,
        1f - rgba.blue
      )
    )

    override def fromXYZ(xyz: XYZ): CMY = fromRGB(xyz.toRGB)

    override def fromXYZA(xyza: XYZA): CMY = fromRGB(xyza.toRGB)

    override def toString:String = "CMY"
  }

  type CMY = CMY.CMY

  /**
   * CMY is the primary type for representing colors in CMY space.
   *
   * @constructor Create a new CMY object from three Float values.  This constructor does not validate input parameters.
   *              For values taken from user input, sensors, or otherwise uncertain sources, consider using the factory method in the Color companion object.
   * @see [[ai.dragonfly.color.CMY.getIfValid]] for a method of constructing CMY objects that validates inputs.
   * @see [[https://en.wikipedia.org/wiki/CMY_color_model]] for more information about the CMY color space.
   * @param cyan    a value ranging from [0-1].  Values outside of this range may cause errors.
   * @param magenta a value ranging from [0-1].  Values outside of this range may cause errors.
   * @param yellow  a value ranging from [0-1].  Values outside of this range may cause errors.
   * @return an instance of the CMY type.
   * @example {{{
   * val c = CMY(1f, 0.25f, 0.5f, 0f)
   * c.toString()  // returns "CMY(1.000,0.250,0.500,0.000)"
   * }}}
   */

  given VectorColorModel[3, CMY] with {
    extension (cmy: CMY) {

      def cyan: Float = CMY.cyan(cmy)

      def magenta: Float = CMY.magenta(cmy)

      def yellow: Float = CMY.yellow(cmy)

      override def similarity(that: CMY): Double = CMY.similarity(cmy, that)

      override def vec: VecF[3] = cmy.asInstanceOf[VecF[3]].copy

      override def copy: CMY = cmy.asInstanceOf[VecF[3]].copy.asInstanceOf[CMY]

      override def toRGB: RGB = RGB.apply(
        clamp0to1(
          1f - cmy.cyan,
          1f - cmy.magenta,
          1f - cmy.yellow
        )
      )

      override def toRGBA: RGBA = {
        val rgb: RGB = toRGB
        RGBA(rgb.red, rgb.green, rgb.blue, 1f)
      }

      override def toRGBA(alpha: Float): RGBA = {
        val rgb: RGB = toRGB
        RGBA(rgb.red, rgb.green, rgb.blue, alpha)
      }

      override def toXYZ: XYZ = toRGB.toXYZ

      override def toXYZA: XYZA = {
        val xyz: XYZ = toXYZ
        XYZA(xyz.x, xyz.y, xyz.z, 1f)
      }

      override def toXYZA(alpha:Float): XYZA = {
        val xyz: XYZ = toXYZ
        XYZA(xyz.x, xyz.y, xyz.z, alpha)
      }

      override def render: String = s"CMY($cyan, $magenta, $yellow)"

    }
  }

}
