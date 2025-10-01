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

package ai.dragonfly.uriel.color.spectral

import narr.*
import slash.vectorf.*

trait SampleSet {

  def samples: NArray[Sample]

  def sampleCount: Int = samples.length

  lazy val volumePoints:NArray[VecF[3]] = {

    val points: NArray[VecF[3]] = NArray.ofSize[VecF[3]](
      2 + (samples.length * (samples.length - 1))
    )

    points(0) = VecF[3](0f, 0f, 0f)

    val `xyzʷ`:VecF[3] = {
      val v: VecF[3] = VecF[3](0f, 0f, 0f)
      for (s <- samples) v.add(s.xyz)
      v
    }

    var p: Int = 1

    points(points.length - 1) = VecF[3](1f, 1f, 1f)

    var i: Int = 0
    while (i < samples.length - 1) {
      var j: Int = 0
      while (j < samples.length) {
        val v: VecF[3] = VecF[3](0f, 0f, 0f)
        var k: Int = 0
        while (k <= i) {
          v.add(samples((j + k) % samples.length).xyz)
          k = k + 1
        }
        points(p) = VecF[3](v.x / `xyzʷ`.x, v.y / `xyzʷ`.y, v.z / `xyzʷ`.z)
        p += 1
        j = j + 1
      }
      i = i + 1
    }

    points
  }
}