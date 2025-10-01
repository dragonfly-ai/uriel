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

import ai.dragonfly.uriel.cie.Constant.*

/**
 * The TransferFunction handles companding.
 */

trait TransferFunction {
  def decode(V: Float): Float
  def encode(v: Float): Float
}

case class Gamma(gamma: Float) extends TransferFunction {
  val Γ: Float = gamma
  val `1/Γ`: Float = 1f / gamma

  override inline def decode(V: Float): Float = Math.pow(V, gamma).toFloat

  override inline def encode(v: Float): Float = Math.pow(v, `1/Γ`).toFloat
}

object sRGB_Constants {
  val `1/2.4`: Float = 0.4166666666666667f
  val `1/1.055`: Float = 0.9478672985781991f
  val `1/12.9232102`: Float = 0.077380154352051f
}

object sRGB_ICC_V2 extends TransferFunction {
  import sRGB_Constants.*
  // https://en.wikipedia.org/wiki/SRGB#From_sRGB_to_CIE_XYZ

  override inline def decode(i: Float): Float = if (i > 0.04045f) {
    Math.pow(`1/1.055` * (i + 0.055), 2.4).toFloat // Math.pow((V + 0.055) / 1.055, 2.4)
  } else {
    `1/12.9232102` * i
  }


  override inline def encode(v: Float): Float = if (v > 0.0031308f) {
    (1.055 * Math.pow(v, `1/2.4`) - 0.055).toFloat
  } else {
    12.9232102f * v
  }

}

//
//object sRGB_ICC_V4 extends TransferFunction {
//  import sRGB_Constants.*
//  // https://www.color.org/sRGB.pdf
////  val `(79.8 / 12.9232102) / 80.0`:Double = 0.07718670396617087
////  //val `1/((79.8 / 12.9232102) / 80.0)`:Double = 12.955599197994989
////
////  val `80/79.8`:Double = 1.0025062656641603
////  val `0.2/79.8`:Double = 0.0025062656641604013
////
////  val A:Double = 0.8772169227486248 // (79.8 / Math.pow(1.055, 2.4)) / 80.0
////
////  override inline def decode(i: Float): Float = if (i > 0.04045) {
////    A * Math.pow(i + 0.055, 2.4)
////  } else {
////    (`(79.8 / 12.9232102) / 80.0` * i) + 0.0025
////  }
////
////  override inline def encode(i: Float): Float = if (i > 0.0031308) {
////    (1.055 * Math.pow((`80/79.8` * i) - `0.2/79.8`, `1/2.4`)) - 0.055
////  } else {
////    //`1/((79.8 / 12.9232102) / 80.0)` * (i - 0.0025)
////    12.955599197994987 * (i - 0.0025)
////  }
//
//  override inline def decode(i: Float): Float = if (i > 0.04045) {
//    Math.pow(0.946879 * i + 0.0520784, 2.4) + 0.0025
//  } else {
//    (0.0772059 * i) + 0.0025
//  }
//
//  override inline def encode(i: Float): Float = sRGB_ICC_V2.encode(i)
////  if (i > 0.0031308) {
////    (Math.pow(i - 0.0025, `1/2.4`) - 0.0520784) / 0.0946879
////  } else {
////    (i - 0.0025) / 0.0772059
////  }
//
//}

object Lstar extends TransferFunction {
  val `1/1.16`: Float = 0.862068966f // 1.0/1.16
  val `k/100`: Float = 9.03296296296f
  val `100/k`: Float = 0.110705646f

  override inline def decode(V: Float): Float = if (V > 0.08) {
    Math.pow((`1/1.16` * V) + 0.137931034, 3).toFloat
  } else {
    V * `100/k`
  }

  override inline def encode(v: Float): Float = if (v > ϵ) {
    ((1.16 * Math.cbrt(v)) - 0.16).toFloat
  } else {
    v * `k/100`
  }
}

// todo: NTSC Video's SMPTE-170M

// todo: HD Video's SMPTE-240M