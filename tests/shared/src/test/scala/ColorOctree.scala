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

import scala.language.implicitConversions
import narr.*
import ai.dragonfly.spatial.PROctreeFMap
import ai.dragonfly.uriel.ColorContext
import ColorContext.sRGB.*
import slash.vectorf.*

import scala.reflect.ClassTag

class ColorOctree extends munit.FunSuite {

  test("ColorOctree") {
    val octree: PROctreeFMap[NArray[Int]] = new PROctreeFMap[NArray[Int]]( 1f, VecF[3](0.5f, 0.5f, 0.5f), 64 )
    val c:RGB = RGB.random()
    val ci: Int = ARGB32.fromRGB(c)
    octree.insert(c.vec, NArray.fill[Int](1)(ci))
    octree.nearestNeighbor(RGB.random().vec) match {
      case (v, c) => assertEquals(ci, c(0))
    }
  }

}
