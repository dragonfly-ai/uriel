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
import ai.dragonfly.uriel.color.spectral.SampleSet
import slash.matrix.ml.unsupervised.dimreduction.PCA
import slash.matrix.ml.data.*
import slash.stats.probability.distributions.Sampleable
import slash.stats.probability.distributions.stream.StreamingVectorStats
import slash.*
import slash.vectorf.*
import ai.dragonfly.mesh.*
import ai.dragonfly.mesh.shape.*
import ai.dragonfly.uriel.ColorContext
import slash.geometry.Tetrahedron

import scala.collection.mutable

trait Gamut { self: WorkingSpace =>
  object Gamut {

    val XYZtoARGB32: VecF[3] => ColorContext.sRGB.ARGB32 = {
      import ColorContext.sRGB
      if (self == sRGB.ARGB32) {
        (v: VecF[3]) => sRGB.ARGB32.fromXYZ(sRGB.XYZ(v.asNativeArray))
      } else {
        val chromaticAdapter: ChromaticAdaptation[self.type, sRGB.type] = ChromaticAdaptation[self.type, sRGB.type](self, sRGB)
        (v: VecF[3]) => sRGB.ARGB32.fromXYZ(chromaticAdapter(self.XYZ(v.asNativeArray)))
      }
    }

    def computeMaxDistSquared(points: NArray[VecF[3]], meanVec: slash.vector.Vec[3]): Double = {
      import slash.vector.*
      val vs: NArray[Vec[3]] = NArray.ofSize[Vec[3]](points.length)
      var i:Int = 0; while (i < points.length) {
        vs(i) = points(i).toVec - meanVec
        i += 1
      }

      val vecSpace = VectorFSpace(points.length)

      val pca = PCA(new StaticUnsupervisedData[vecSpace.N, 3](vs))

      val mode = pca.basisPairs.head.basisVector

      var min: Double = Double.MaxValue
      var minV: Vec[3] = meanVec
      var MAX: Double = Double.MinValue
      var vMAX: Vec[3] = meanVec

      points.foreach {
        p1 =>
          val p = p1.toVec
          val t: Double = mode dot p
          if (t < min) {
            min = t
            minV = p
          }
          if (t > MAX) {
            MAX = t
            vMAX = p
          }
      }

      minV.euclideanDistanceSquaredTo(vMAX)

    }

    def fromRGB(n: Int = 32, transform: XYZ => VecF[3] = (xyz: XYZ) => xyz.vec): Gamut = {

      val m1: MeshF = Cube(1.0, n).toMeshF

//      val m2: Mesh = Mesh(
//        NArray.tabulate[VecF[3]](m1.points.length)((i:Int) => m1.points(i) ), //* 255.0}),
//        m1.triangles
//      )

      val m3: MeshF = MeshF(
        m1.points.map((vRGB:VecF[3]) => transform(RGB.fromVec(vRGB).toXYZ)),
        m1.triangles
      )

//      val sg:Gaussian = Gaussian()
//
//      var i:Int = 0; while (i < m1.triangles.length) {
//        val t:Triangle = m1.triangles(i)
//        sg.observe( Math.sqrt( t.area(m3.points) / t.area(m2.points) ) )
//        i += 1
//      }
//
//      println(s"$ctx triangle stretch stats: ${sg.estimate}")

      new Gamut( m3 )
    }

    def fromSpectralSamples(spectralSamples: SampleSet, illuminant: Illuminant): Gamut = fromSpectralSamples(
      spectralSamples,
      (v: VecF[3]) => VecF[3](
        v.x * illuminant.xₙ,
        v.y * illuminant.yₙ,
        v.z * illuminant.zₙ,
      )
    )


    def fromSpectralSamples(spectralSamples: SampleSet, transform: VecF[3] => VecF[3] = (v: VecF[3]) => v): Gamut = {

      val points: NArray[VecF[3]] = NArray.tabulate[VecF[3]](spectralSamples.volumePoints.length)(
        (i:Int)=> transform(spectralSamples.volumePoints(i))
      )

      val triangles:mutable.HashSet[TriangleF] = mutable.HashSet[TriangleF]()

      var t:Int = 0
      def addTriangle(pi0:Int, pi1:Int, pi2:Int): Unit = {
        if (TriangleF.nonZeroArea(points(pi0), points(pi1), points(pi2))) {
          triangles += TriangleF(pi2, pi1, pi0)
        }
        t += 1
      }

      //addTriangle(Tetrahedron(mean, points(0), points(1), points(spectralSamples.sampleCount)))
      addTriangle(0, 1, spectralSamples.sampleCount)

      // black adjacent:
      while (t < spectralSamples.sampleCount) addTriangle(0, t + 1, t)

      val hEnd: Int = points.length - spectralSamples.sampleCount

      val end = (2 * (points.length - 1)) - spectralSamples.sampleCount
      while (t < end) {
        val i: Int = (t - spectralSamples.sampleCount) / 2
        if (i < hEnd) {
          val h: Int = i + spectralSamples.sampleCount
          if (t % 2 == 1) addTriangle(i, h, h - 1) // Tetrahedron(mean, points(i), points(h), points(h - 1))
          else addTriangle(i+1, h, i) // Tetrahedron(mean, points(i + 1), points(h), points(i))
        } else {
          val h: Int = points.length - 1
          addTriangle(i, h, h - 1) // Tetrahedron(mean, points(i), points(h), points(h - 1))
        }
      }

      // white adjacent:
      for (i <- (points.length - 1) - spectralSamples.sampleCount until points.length - 2) addTriangle(i, i + 1, points.length - 1)

      new Gamut(MeshF.fromPointsAndHashSet(points, triangles, "Spectral Samples Gamut"))
    }

//    println("defined Gamut object methods")
  }


  /**
   *
   * @param tetrahedra
   * @param cumulative
   */

  case class Gamut (volumeMesh:MeshF) extends Sampleable[VecF[3]] {

    val mean: slash.vector.Vec[3] = {
      val sv2:StreamingVectorStats[3] = new StreamingVectorStats[3]()
      volumeMesh.points.foreach((p:VecF[3]) => sv2(p.toVec))
      sv2.average()
    }

    val maxDistSquared: Double = Gamut.computeMaxDistSquared(volumeMesh.points, mean)

    val tetrahedra: NArray[Tetrahedron] = NArray.tabulate[Tetrahedron](volumeMesh.triangles.length)((i:Int) => {
      val t: TriangleF = volumeMesh.triangles(i)
      Tetrahedron(
        mean,
        volumeMesh.points(t.v1).toVec,
        volumeMesh.points(t.v2).toVec,
        volumeMesh.points(t.v3).toVec
      )
    })

    val cumulative: NArray[Double] = {
      var totalVolume: Double = 0.0
      val ca:NArray[Double] = NArray.tabulate[Double](volumeMesh.triangles.length)((i:Int) => {
        totalVolume += tetrahedra(i).volume
        totalVolume
      })
      var i:Int = 0; while (i < ca.length) {
        ca(i) = ca(i) / totalVolume
        i += 1
      }
      ca
    }

    private def getNearestIndex(target: Double): Int = {
      var left = 0
      var right = cumulative.length - 1
      while (left < right) {
        val mid = (left + right) / 2
        if (cumulative(mid) < target) left = mid + 1
        else if (cumulative(mid) > target) right = mid - 1
        else return mid
      }
      right
    }

    override def random(r: scala.util.Random = slash.Random.defaultRandom): VecF[3] = {
      val x = r.nextDouble()
      val i = getNearestIndex(x)
      if (i < 0 || i > tetrahedra.length) println(s"x = $x, i = $i, cumulative.length = ${cumulative.length}")
      VecF.fromVec[3](tetrahedra(i).random(r))
    }

  }
}