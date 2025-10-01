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

import slash.{radiansToDegrees, squareInPlace}
import slash.Constant.π

import slash.vectorf.*

trait HueSaturation { self: WorkingSpace =>

  trait HueSaturationSpace[C: CylindricalColorModel] extends CylindricalSpace[C] {

    def apply(h: Float, s: Float, lv: Float): C

    inline def validHue(angle: Float): Boolean = angle >= 0f && angle <= 360f

    inline def clampHue(angle: Float): Float = ((angle % 360f) + 360f) % 360f // Aly Cerruti's angle sanitization function from nose

    inline def hueMinMax(red: Float, green: Float, blue: Float): NArray[Float] = {
      // hue extractor based on a scala implementation in project nose: https://gitlab.com/srnb/nose/-/blob/master/nose/src/main/scala/tf/bug/nose/space/rgb/StandardRGB.scala
      // provided by Aly Cerruti

      val min: Float = Math.min(red, Math.min(green, blue))
      val MAX: Float = Math.max(red, Math.max(green, blue))

      NArray[Float](
        clampHue(
          MAX match {
            case `min` => 0f
            case `red` => 60f * ((green - blue) / (MAX - min))
            case `green` => 60f * (2f + ((blue - red) / (MAX - min)))
            case `blue` => 60f * (4f + ((red - green) / (MAX - min)))
          }
        ),
        min,
        MAX
      )
    }

    override def fromVec(v:VecF[3]): C = {
      val r:Float = Math.sqrt(squareInPlace(v.x) + squareInPlace(v.y)).toFloat
      val θ:Float = (π + Math.atan2(v.y, v.x)).toFloat
      apply(
        radiansToDegrees(θ).toFloat,
        r,
        v.z
      )
    }

    override val maxDistanceSquared: Double = 6.0

    override def euclideanDistanceSquaredTo(c1: C, c2: C): Double = toVec(c1).euclideanDistanceSquaredTo(toVec(c2)).toFloat

    override def weightedAverage(c1: C, w1: Float, c2: C, w2: Float): C = fromVec(
      (toVec(c1) * w1) + (toVec(c2) * w2)
    )

    inline def hcxmToRGBvalues(hue: Float, c: Float, x: Float, m: Float): NArray[Float] = {
      val X = x + m
      val C = c + m

      if (hue < 60f) clamp0to1(C, X, m) // hue = 0 clamps to 360
      else if (hue < 120f) clamp0to1(X, C, m)
      else if (hue < 180f) clamp0to1(m, C, X)
      else if (hue < 240f) clamp0to1(m, X, C)
      else if (hue < 300f) clamp0to1(X, m, C)
      else clamp0to1(C, m, X)
    }

    inline def XfromHueC(H: Float, C: Float): Float = C * (1f - Math.abs(((H / 60f) % 2f) - 1f))

    override def fromXYZ(xyz: XYZ): C = fromRGB(xyz.toRGB)

  }
}