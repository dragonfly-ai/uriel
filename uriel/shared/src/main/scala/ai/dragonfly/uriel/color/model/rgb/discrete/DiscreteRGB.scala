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

package ai.dragonfly.uriel.color.model.rgb.discrete

import ai.dragonfly.uriel.*
import ai.dragonfly.uriel.cie.WorkingSpace

import slash.squareInPlace

trait DiscreteRGB {
  self: WorkingSpace =>
  
  trait DiscreteRGB[C] extends DiscreteColorModel[C] {
    extension (c: C) {
      def red: Int
      def green: Int
      def blue: Int
      def alpha: Int
    }
  }


  trait UtilDiscreteRGB[C:DiscreteRGB] extends DiscreteSpace[C] {
    val min: Int = 0
    val MAX: Int
    lazy val MAXF: Float = MAX.toFloat

    override lazy val maxDistanceSquared: Double = 3.0 * squareInPlace(MAXF)

    override def euclideanDistanceSquaredTo(c1: C, c2: C): Double = squareInPlace(c1.red - c2.red) + squareInPlace(c1.green - c2.green) + squareInPlace(c1.blue - c2.blue)

    inline def valid(intensity: Int): Boolean = intensity >= min && intensity <= MAX

    inline def valid(i0: Int, i1: Int, i2: Int): Boolean = valid(i0) && valid(i1) && valid(i2)

    inline def valid(i0: Int, i1: Int, i2: Int, i3: Int): Boolean = valid(i0) && valid(i1) && valid(i2) && valid(i3)

    /**
     * Generate an C instance from a single value, skipping all overhead and validation.  Not suited for intensity data
     * provided by users, sensors, or other unreliable sources.
     *
     * @param intensity the intensity of the desired gray value ranging from [0-65535].
     * @return an ARGB instance encoding the desired grayscale intensity.
     */
    def gray(intensity: Int): C = apply(intensity, intensity, intensity)

    lazy val Clear: C = apply(0, 0, 0, 0)
    lazy val Black: C = apply(0, 0, 0)
    lazy val White: C = apply(MAX, MAX, MAX)
    lazy val Gray: C = gray(MAX / 2)
    lazy val DarkGray: C = gray(MAX / 4)
    lazy val LightGray: C = gray((3 * MAX) / 4)

    def apply(red: Int, green: Int, blue: Int): C

    def apply(c1: Int, c2: Int, c3: Int, c4: Int): C

    override def fromXYZ(xyz: XYZ): C = fromRGB(xyz.toRGB)

    override def fromXYZA(xyza: XYZA): C = fromRGBA(xyza.toRGBA)
  }


  trait UtilRGB32[C: DiscreteRGB] extends UtilDiscreteRGB[C] {
    override val MAX: Int = 255

    inline def clamp(intensity: Float): Int = Math.round(Math.max(0f, Math.min(MAX.toFloat, intensity)))

    inline def clamp(c4: Float, c3: Float, c2: Float, c1: Float): Int = {
      (clamp(c4) << 24) | (clamp(c3) << 16) | (clamp(c2) << 8) | clamp(c1)
    }

    inline def clamp(red: Float, green: Float, blue: Float): Int
  }


  trait UtilDiscreteRGB64[C: DiscreteRGB] extends UtilDiscreteRGB[C] {
    override val MAX: Int = 65535

    inline def clamp(intensity: Float): Long = Math.round(Math.max(0f, Math.min(MAX.toFloat, intensity)))

    inline def clamp(c4: Float, c3: Float, c2: Float, c1: Float): Long = {
      (clamp(c4) << 48) | (clamp(c3) << 32) | (clamp(c2) << 16) | clamp(c1)
    }

    /**
     * Generate an ARGB instance from a single value.  This method validates the intensity parameter at some cost to performance.
     *
     * @param intensity the intensity of the desired gray value ranging from [0-65535].
     * @return an ARGB instance encoding the desired grayscale intensity.
     */
    def grayIfValid(intensity: Int): Option[C] = {
      if (valid(intensity)) Some(apply(intensity, intensity, intensity))
      else None
    }

    def clamp(red: Float, green: Float, blue: Float): Long
  }

}