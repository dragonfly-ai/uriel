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

package ai.dragonfly.uriel.experiments

import ai.dragonfly.uriel.ColorContext.sRGB
import slash.stats.probability.distributions.stream.StreamingVectorStats

object LuvStats extends App {

  val pts = sRGB.Luv.usableGamut.volumeMesh.points

  val stats: StreamingVectorStats[3] = new StreamingVectorStats[3]()

  var i = 0
  while (i < pts.length) {
    stats(pts(i).toVec)
    i = i + 1
  }

  println(stats)
  println(stats.bounds())
}
