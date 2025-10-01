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
import slash.vectorf.*

import slash.matrixf
import matrixf.*
import matrixf.MatF.*

import scala.language.implicitConversions

object ChromaticAdaptation {

  // Chromatic Adaptation Matrices from  http://www.brucelindbloom.com/index.html?Eqn_ChromAdapt.html

  lazy val XYZ_Scaling: MatF[3,3] = MatF.identity[3,3]
  lazy val XYZ_Scaling_Inverse: MatF[3,3] = XYZ_Scaling

  lazy val Bradford: MatF[3,3] = MatF[3,3](
    0.8951f, 0.2664f, -0.1614f,
    -0.7502f, 1.7135f, 0.0367f,
    0.0389f, -0.0685f, 1.0296f
  )

  lazy val Bradford_Inverse: MatF[3,3] = MatF[3,3](
    0.9869929f, -0.1470543f, 0.1599627f,
    0.4323053f, 0.5183603f, 0.0492912f,
    -0.0085287f, 0.0400428f, 0.9684867f
  )

  lazy val Von_Kries: MatF[3,3] = MatF[3,3](
    0.40024f, 0.7076f, -0.08081f,
    -0.2263f, 1.16532f, 0.0457f,
    0f, 0f, 0.91822f
  )
  lazy val Von_Kries_Inverse: MatF[3,3] = MatF[3,3](
    1.8599364f, -1.1293816f, 0.2198974f,
    0.3611914f, 0.6388125f, -0.0000064f,
    0f, 0f, 1.0890636f
  )

}

case class ChromaticAdaptation[S <: WorkingSpace, T <: WorkingSpace](source:S, target:T, m:MatF[3,3] = Bradford) {

  val s:NArray[Float] = (m * source.illuminant.asColumnMatrix).values

  val t:NArray[Float] = (m * target.illuminant.asColumnMatrix).values

  val M:MatF[3,3] = m.inv.times(
    MatF[3,3](
      t(0) / s(0), 0f, 0f,
      0f, t(1) / s(1), 0f,
      0f, 0f, t(2) / s(2)
    ).times(m)
  )

  def apply(xyz:source.XYZ):target.XYZ = target.XYZ((M * (xyz.asInstanceOf[VecF[3]]).asColumnMatrix).values)

}