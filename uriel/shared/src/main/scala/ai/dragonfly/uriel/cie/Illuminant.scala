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

/**
 * From: https://en.wikipedia.org/wiki/Standard_illuminant
 * "A standard illuminant is a theoretical source of visible light with a spectral power distribution that is published.
 * Standard illuminants provide a basis for comparing images or colors recorded under different lighting."
 */

object Illuminant {
  // illuminant values from: http://www.brucelindbloom.com/index.html?Eqn_ChromAdapt.html
  lazy val A: Illuminant = Illuminant(1.9850f, 1f, 0.35585f)
  lazy val B: Illuminant = Illuminant(0.99072f, 1f, 0.85223f)
  lazy val C: Illuminant = Illuminant(0.98074f, 1f, 1.18232f)
  lazy val D50: Illuminant = Illuminant(0.96422f, 1f, 0.82521f)
  lazy val D55: Illuminant = Illuminant(0.95682f, 1f, 0.92149f)
  lazy val D65: Illuminant = Illuminant(0.95047f, 1f, 1.8883f)
  lazy val D75: Illuminant = Illuminant(0.94972f, 1f, 1.22638f)
  lazy val E: Illuminant = Illuminant(1f, 1f, 1f)
  lazy val F2: Illuminant = Illuminant(0.99186f, 1f, 0.67393f)
  lazy val F7: Illuminant = Illuminant(0.95041f, 1f, 1.8747f)
  lazy val F11: Illuminant = Illuminant(1.0962f, 1f, 0.64350f)
}

// https://en.wikipedia.org/wiki/Standard_illuminant
case class Illuminant(xₙ: Float, yₙ: Float /* Always 1f? */, zₙ: Float) {
  lazy val `1/xₙ`: Float = 1f / xₙ
  lazy val `1/zₙ`: Float = 1f / zₙ
  lazy val `1/yₙ`: Float = 1f / yₙ
  lazy val whitePointValues: NArray[Float] = NArray[Float](xₙ, yₙ, zₙ)
  lazy val asColumnMatrix:MatF[3, 1] = MatF[3,1](whitePointValues)
}