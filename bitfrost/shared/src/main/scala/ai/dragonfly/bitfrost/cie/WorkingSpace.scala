package ai.dragonfly.bitfrost.cie

import narr.*
import slash.*
import vector.*
import matrix.*
import matrix.util.*
import slash.matrix.ml.data.*
import ai.dragonfly.bitfrost.color.model.*
import ai.dragonfly.bitfrost.color.model.perceptual.XYZ
import ai.dragonfly.bitfrost.color.model.rgb.discrete.{ARGB32, RGBA32}
import ai.dragonfly.bitfrost.color.model.rgb.RGB
import ai.dragonfly.bitfrost.color.spectral.*
import slash.stats.probability.distributions.Sampleable
import slash.stats.probability.distributions.stream.StreamingVectorStats
import ai.dragonfly.mesh.*
import ai.dragonfly.mesh.shape.*

import java.io.PrintWriter
import scala.collection.{immutable, mutable}
import scala.util.Random


trait WorkingSpace extends XYZ with RGB with Gamut {

  val transferFunction: TransferFunction
  val primaries: ChromaticityPrimaries
  val illuminant: Illuminant

  val cmf: SampleSet = DEFAULT

  lazy val whitePoint:XYZ = XYZ(illuminant.whitePointValues)

  lazy val M: Mat[3,3] = primaries.getM(illuminant)

  lazy val M_inverse: Mat[3,3] = M.inverse

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
    extension (c:C) def vec: Vec[3] = c.asInstanceOf[Vec[3]]
  }

  trait DiscreteColorModel[C] extends ColorModel[C]

  trait CylindricalColorModel[C] extends VectorColorModel[C]

  trait HueSaturationModel[C] extends CylindricalColorModel[C] {
    extension (c: C) {
      def hue: Double
      def saturation: Double
      override def toXYZ: XYZ = c.toRGB.toXYZ
    }
  }

  trait PerceptualColorModel[C] extends VectorColorModel[C]

  /**
   * Space traits for companion objects of Color Models.
   */

  trait Space[C: ColorModel](using ctx:WorkingSpace) extends Sampleable[C] {

    /**
     * Computes a weighted average of two colors in C color space.
     * @param c1 the first color.
     * @param w1 the weight of the first color in the range of [0-1].
     * @param c2 the second color.
     * @param w2 the weight of the second color in the range of [0-1].
     * @return the weighted average: c1 * w1 + c2 * w2.
     */
    def weightedAverage(c1: C, w1: Double, c2: C, w2: Double): C

    def maxDistanceSquared:Double

    def euclideanDistanceSquaredTo(c1: C, c2: C):Double
    def euclideanDistanceTo(c1: C, c2: C):Double = Math.sqrt(euclideanDistanceSquaredTo(c1, c2))
    def similarity(c1: C, c2: C): Double = 1.0 - Math.sqrt(euclideanDistanceSquaredTo(c1, c2) / maxDistanceSquared)

    def fromRGB(rgb:RGB):C
    def fromXYZ(xyz:XYZ):C

  }

  trait DiscreteSpace[C: DiscreteColorModel] extends Space[C] {

  }

  trait VectorSpace[C: VectorColorModel] extends Space[C] {

    /**
     * Computes a weighted average of two colors in C color space.
     * @param c1 the first color.
     * @param w1 the weight of the first color in the range of [0-1].
     * @param c2 the second color.
     * @param w2 the weight of the second color in the range of [0-1].
     * @return the weighted average: c1 * w1 + c2 * w2.
     */
    def weightedAverage(c1: C, w1: Double, c2: C, w2: Double): C = fromVec((toVec(c1) * w1) + (toVec(c2) * w2))

    def apply(values:NArray[Double]):C

    override def euclideanDistanceSquaredTo(c1: C, c2: C): Double //= c1.euclideanDistanceSquaredTo(c2)

    def fromVec(v: Vec[3]): C
    def toVec(c: C): Vec[3]
  }

  trait CylindricalSpace[C: CylindricalColorModel] extends VectorSpace[C] {

  }

  trait PerceptualSpace[C: PerceptualColorModel] extends VectorSpace[C] {

    def apply(c1: Double, c2: Double, c3: Double): C

    override def fromRGB(rgb: RGB): C = fromXYZ(rgb.toXYZ)

    lazy val gamut:Gamut = Gamut.fromRGB(transform = (xyz:XYZ) => fromXYZ(xyz).asInstanceOf[Vec[3]])

    override lazy val maxDistanceSquared:Double = gamut.maxDistSquared

    override def random(r: Random = slash.Random.defaultRandom): C = {
      val v = gamut.random(r)
      apply(v.asNativeArray)
    }

    override def euclideanDistanceSquaredTo(c1: C, c2: C): Double = c1.asInstanceOf[Vec[3]].euclideanDistanceSquaredTo(c1.asInstanceOf[Vec[3]])
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