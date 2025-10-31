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

import slash.matrix
import matrix.*

import scala.language.implicitConversions

object ChromaticAdaptation {

  // Chromatic Adaptation Matrices from  http://www.brucelindbloom.com/index.html?Eqn_ChromAdapt.html

  lazy val XYZ_Scaling: Mat[3,3] = Mat.identity[3,3]
  lazy val XYZ_Scaling_Inverse: Mat[3,3] = XYZ_Scaling

  lazy val Bradford: Mat[3,3] = Mat[3,3](
    0.8951f, 0.2664f, -0.1614f,
    -0.7502f, 1.7135f, 0.0367f,
    0.0389f, -0.0685f, 1.0296f
  )

  lazy val Bradford_Inverse: Mat[3,3] = Mat[3,3](
    0.9869929f, -0.1470543f, 0.1599627f,
    0.4323053f, 0.5183603f, 0.0492912f,
    -0.0085287f, 0.0400428f, 0.9684867f
  )

  lazy val Von_Kries: Mat[3,3] = Mat[3,3](
    0.40024f, 0.7076f, -0.08081f,
    -0.2263f, 1.16532f, 0.0457f,
    0f, 0f, 0.91822f
  )
  lazy val Von_Kries_Inverse: Mat[3,3] = Mat[3,3](
    1.8599364f, -1.1293816f, 0.2198974f,
    0.3611914f, 0.6388125f, -0.0000064f,
    0f, 0f, 1.0890636f
  )

  lazy val XYZ_Scaling_Alpha: Mat[4,4] = Mat.identity[4,4]
  lazy val XYZ_Scaling_Inverse_Alpha: Mat[4,4] = XYZ_Scaling_Alpha

  lazy val Bradford_Alpha: Mat[4,4] = Mat[4,4](
    0.8951f, 0.2664f, -0.1614f, 0f,
    -0.7502f, 1.7135f, 0.0367f, 0f,
    0.0389f, -0.0685f, 1.0296f, 0f,
    0f, 0f, 0f, 1f
  )

  lazy val Bradford_Inverse_Alpha: Mat[4,4] = Mat[4,4](
    0.9869929f, -0.1470543f, 0.1599627f, 0f,
    0.4323053f, 0.5183603f, 0.0492912f, 0f,
    -0.0085287f, 0.0400428f, 0.9684867f, 0f,
    0f, 0f, 0f, 1f
  )

  lazy val Von_Kries_Alpha: Mat[4,4] = Mat[4,4](
    0.40024f, 0.7076f, -0.08081f, 0f,
    -0.2263f, 1.16532f, 0.0457f, 0f,
    0f, 0f, 0.91822f, 0f,
    0f, 0f, 0f, 1f
  )

  lazy val Von_Kries_Inverse_Alpha: Mat[4,4] = Mat[4,4](
    1.8599364f, -1.1293816f, 0.2198974f, 0f,
    0.3611914f, 0.6388125f, -0.0000064f, 0f,
    0f, 0f, 1.0890636f, 0f,
    0f, 0f, 0f, 1f
  )


}

case class ChromaticAdaptation[S <: WorkingSpace, T <: WorkingSpace](source:S, target:T, m:Mat[3,3] = Bradford) {

  private def toFloatNArray(m:Mat[3, 1]):NArray[Float] = {
    slash.vector.Vec[3](m.values).toVecF.asInstanceOf[NArray[Float]]
  }

  val s:NArray[Float] = toFloatNArray(m * source.illuminant.asColumnMatrix)

  val t:NArray[Float] = toFloatNArray(m * target.illuminant.asColumnMatrix)

  val M:Mat[3,3] = m.inverse.times(
    Mat[3,3](
      t(0) / s(0), 0f, 0f,
      0f, t(1) / s(1), 0f,
      0f, 0f, t(2) / s(2)
    ).times(m)
  )

  def apply(xyz:source.XYZ):target.XYZ = target.XYZ.fromVec((M * xyz.vec.toVec).toVecF)

}

case class ChromaticAdaptationAlpha[S <: WorkingSpace, T <: WorkingSpace](source:S, target:T, m:Mat[4,4] = ChromaticAdaptation.Bradford_Alpha) {

  private def toFloatNArray(m: Mat[4, 1]): NArray[Float] = {
    slash.vector.Vec[4](m.values).toVecF.asInstanceOf[NArray[Float]]
  }

  val s:NArray[Float] = toFloatNArray(m * source.illuminant.asColumnMatrixAlpha)

  val t:NArray[Float] = toFloatNArray(m * target.illuminant.asColumnMatrixAlpha)

  val M:Mat[4,4] = m.inverse.times(
    Mat[4,4](
      t(0) / s(0), 0.0, 0.0, 0.0,
      0.0, t(1) / s(1), 0.0, 0.0,
      0.0, 0.0, t(2) / s(2), 0.0,
      0.0, 0.0, 0.0, 1.0
    ).times(m)
  )

  def apply(xyz:source.XYZA):target.XYZA = target.XYZA.fromVec((M * xyz.vec.toVec).toVecF)

}