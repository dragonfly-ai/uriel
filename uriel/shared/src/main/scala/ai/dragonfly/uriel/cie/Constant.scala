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

object Constant {

  // constants
  // http://www.brucelindbloom.com/index.html?LContinuity.html

  val ϵ: Double = 0.008856451679035631
  val `ϵ³`: Double = 6.946711652838615E-7
  val `∛ϵ`: Double = 0.20689655172413793

  val k: Double = 903.2962962962963
  val kϵ: Double = 8.0


  val `k/116`: Double = 7.787037037037037

  val `116/k`: Double = 0.12841854934601665
  val `16/k`: Double = 0.017712903358071262
  val `1/k`: Double = 0.0011070564598794539

  val `16/116`: Double = 0.13793103448275862
  val `1/116`: Double = 0.008620689655172414
  val `1/116³`: Double = 6.406576735413506E-7

  val Yn:  Double = 100.0
  val `1/Yn`:  Double = 0.01
}
