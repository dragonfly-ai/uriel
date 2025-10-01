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
import slash.matrixf.*

object ChromaticityPrimary {
  def inferThird(cp1: ChromaticityPrimary, cp2: ChromaticityPrimary): ChromaticityPrimary = ChromaticityPrimary(
    1f - (cp1.x + cp2.x),
    1f - (cp1.y + cp2.y),
    1f - (cp1.Y + cp2.Y)
  )
}

/**
 * Given two primaries: the third can be inferred.
 *
 * @param v Primary x, y with x between [0.0, 1.0] and y between [0.0, 1.0]
 * @param Y brightness of the primary
 */

case class ChromaticityPrimary(x:Float, y:Float, Y:Float)

/**
 * Assumes:
 * RED.x + GEEN.x + BLUE.x = 1.0
 * RED.y + GEEN.y + BLUE.y = 1.0
 * RED.Y + GEEN.Y + BLUE.Y = 1.0
 *
 * @param red red chromatic primary
 * @param green green chromatic primary
 * @param blue blue chromatic primary
 */

case class ChromaticityPrimaries(red: ChromaticityPrimary, green: ChromaticityPrimary, blue: ChromaticityPrimary) {

  // from http://www.brucelindbloom.com/index.html?Eqn_RGB_XYZ_Matrix.html

  def raw(S: NArray[Float] = NArray[Float](1f, 1f, 1f)):NArray[Float] = NArray[Float](
    S(0) * (red.x / red.y), S(1) * (green.x / green.y), S(2) * (blue.x / blue.y),
    S(0), S(1), S(2),
    S(0) * ((1f - red.x - red.y) / red.y), S(1) * ((1f - green.x - green.y) / green.y), S(2) * ((1f - blue.x - blue.y) / blue.y)
  )

  lazy val xyzXrgbInv:MatF[3,3] = MatF[3,3]( raw() ).inv

  def getM(illuminant: Illuminant):MatF[3,3] = MatF[3,3](raw((xyzXrgbInv * illuminant.asColumnMatrix).values))

}