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

package ai.dragonfly.uriel.cie

import narr.*
import slash.*
import slash.vectorf.*
import matrixf.*

import ai.dragonfly.uriel.color.model.*
import ai.dragonfly.uriel.color.model.perceptual.XYZ
import ai.dragonfly.uriel.color.model.rgb.RGB
import ai.dragonfly.uriel.color.spectral.*
import slash.stats.probability.distributions.Sampleable

import scala.collection.{immutable, mutable}
import scala.util.Random


trait WorkingSpace extends XYZ with RGB with Gamut {

  val transferFunction: TransferFunction
  val primaries: ChromaticityPrimaries
  val illuminant: Illuminant

  val cmf: SampleSet = DEFAULT

  lazy val whitePoint:XYZ = XYZ(illuminant.whitePointValues)

  lazy val M: MatF[3,3] = primaries.getM(illuminant)

  lazy val M_inverse: MatF[3,3] = M.inv

  given ctx:WorkingSpace = this

  /**
   * base trait from which all color model types inherit.
   */

  trait ColorModel[C] {
    extension (c: C) {
      def toRGB: RGB
      def toXYZ: XYZ
      def render: String
      def similarity(thatColor:C): Double
      def copy: C
    }
  }

  trait VectorColorModel[C] extends ColorModel[C] {
    extension (c:C) def vec: VecF[3] = c.asInstanceOf[VecF[3]]
  }

  trait DiscreteColorModel[C] extends ColorModel[C]

  trait CylindricalColorModel[C] extends VectorColorModel[C]

  trait HueSaturationModel[C] extends CylindricalColorModel[C] {
    extension (c: C) {
      def hue: Float
      def saturation: Float
      override def toXYZ: XYZ = c.toRGB.toXYZ
    }
  }

  trait PerceptualColorModel[C] extends VectorColorModel[C]

  /**
   * ColorSpace traits for companion objects of Color Models.
   */

  trait ColorSpace[C: ColorModel] extends Sampleable[C] {

    /**
     * Computes a weighted average of two colors in C color space.
     * @param c1 the first color.
     * @param w1 the weight of the first color in the range of [0-1].
     * @param c2 the second color.
     * @param w2 the weight of the second color in the range of [0-1].
     * @return the weighted average: c1 * w1 + c2 * w2.
     */
    def weightedAverage(c1: C, w1: Float, c2: C, w2: Float): C

    def maxDistanceSquared:Double

    def euclideanDistanceSquaredTo(c1: C, c2: C):Double
    def euclideanDistanceTo(c1: C, c2: C):Double = Math.sqrt(euclideanDistanceSquaredTo(c1, c2))
    def similarity(c1: C, c2: C): Double = 1.0 - Math.sqrt(euclideanDistanceSquaredTo(c1, c2) / maxDistanceSquared)

    def toRGB(c:C):RGB
    def fromRGB(rgb:RGB):C

    def toXYZ(c:C):XYZ
    def fromXYZ(xyz:XYZ):C

  }

  trait DiscreteSpace[C: DiscreteColorModel] extends ColorSpace[C] {

  }

  trait VectorSpace[C: VectorColorModel] extends ColorSpace[C] {

    def usableGamut:Gamut

    /**
     * Computes a weighted average of two colors in C color space.
     * @param c1 the first color.
     * @param w1 the weight of the first color in the range of [0-1].
     * @param c2 the second color.
     * @param w2 the weight of the second color in the range of [0-1].
     * @return the weighted average: c1 * w1 + c2 * w2.
     */
    def weightedAverage(c1: C, w1: Float, c2: C, w2: Float): C = fromVec((toVec(c1) * w1) + (toVec(c2) * w2))

    def apply(values:NArray[Float]):C

    override def euclideanDistanceSquaredTo(c1: C, c2: C): Double //= c1.euclideanDistanceSquaredTo(c2)

    def fromVec(v: VecF[3]): C
    def toVec(c: C): VecF[3]

    def fromVec2sRGB_ARGB(v: VecF[3]): ai.dragonfly.mesh.sRGB.ARGB32 = {
      Gamut.XYZtoARGB32(
        toXYZ(
          fromVec(v)
        ).asInstanceOf[VecF[3]]
      ).asInstanceOf[ai.dragonfly.mesh.sRGB.ARGB32]
    }
  }

  trait CylindricalSpace[C: CylindricalColorModel] extends VectorSpace[C] {

  }

  trait PerceptualSpace[C: PerceptualColorModel] extends VectorSpace[C] {

    def apply(c1: Float, c2: Float, c3: Float): C

    override def fromRGB(rgb: RGB): C = fromXYZ(rgb.toXYZ)

    def fullGamut:Gamut

    override lazy val maxDistanceSquared:Double = usableGamut.maxDistSquared

    override def random(r: Random = slash.Random.defaultRandom): C = fromVec(usableGamut.random(r))

    override def euclideanDistanceSquaredTo(c1: C, c2: C): Double = c1.asInstanceOf[VecF[3]].euclideanDistanceSquaredTo(c1.asInstanceOf[VecF[3]])
  }


  override def toString: String = this.getClass.getSimpleName.replace('$', '_')


  object ColorPalette {

    /**
     * apply method to create a ColorPalette object from a color frequency histogram.
     *
     * @param hist a map with color objects as Keys and Integer values as frequencies.
     * @return an instance of the ColorPalette class.
     * @example {{{ val cp = ColorPalette(histogram) }}}
     */
    def apply[C: ColorModel](hist: Map[C, Int]): ColorPalette[C] = {
      // Normalize
      val frequencyTotal: Double = hist.values.sum
      new ColorPalette[C](
        immutable.TreeSet.from[ColorFrequency[C]](
          hist.map { (c: C, f: Int) =>
            val cf = ColorFrequency[C](c, f / frequencyTotal)
            cf
          }
        )(Ordering.by[ColorFrequency[C], Double](_.frequency)).toArray
      )
    }

  }

  /**
   * ColorPalette organizes a sorted array of color frequencies, ranked from highest to lowest.
   *
   * @param colorFrequencies an array of ColorFrequency objects.
   */

  class ColorPalette[C: ColorModel](val colorFrequencies: Array[ColorFrequency[C]]) {
    /**
     * Search the palette for the closest match to a query color.
     *
     * @tparam T encodes the color space to compute the color euclideanDistanceTo in.
     * @param color a color object to query with, e.g. L*a*b*, XYZ, or RGB.
     * @return an instance of the ColorFrequency class which is nearest match to the query color.
     */

    def nearestMatch(color: C): ColorFrequency[C] = {
      var similarity = 0.0
      var colorMatch: ColorFrequency[C] = null
      for (m <- colorFrequencies) {
        val tempSimilarity = color.similarity(m.color)
        if (tempSimilarity > similarity) {
          similarity = tempSimilarity
          colorMatch = m
        }
      }
      colorMatch
    }

    override def toString(): String = {
      val sb = new StringBuilder(colorFrequencies.length * 30)
      sb.append("ColorPalette(")
      for (cf <- colorFrequencies) {
        sb.append(cf).append(" ")
      }
      sb.append(")")
      sb.toString()
    }
  }

  /**
   * ColorFrequency couples a color object to a frequency.
   *
   * @constructor Create a new RGBA object from an Int.
   * @param color     a color object.
   * @param frequency a frequency normalized between 0 and 1.  This encodes the prominence of a color relative to others in a ColorPalette.
   * @return an instance of the ColorFrequency class.
   */

  case class ColorFrequency[C:ColorModel](color: C, frequency: Double) {

    //
    //  /**
    //    * Compares this color's frequency to that color's frequency.
    //    * @param cf a map with color objects as Keys and Integer values as frequencies.
    //    * @return Returns x where: x < 0 when this < that, x == 0 when this == that, x > 0 when this > that
    //   */
    //  override def compare(cf: ColorFrequency[C]) = {
    //    if (frequency < cf.frequency ) -1
    //    else if (frequency > cf.frequency) 1
    //    else 0
    //  }
  }
  
}