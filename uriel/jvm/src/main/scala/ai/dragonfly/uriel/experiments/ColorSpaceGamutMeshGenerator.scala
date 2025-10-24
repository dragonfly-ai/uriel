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

import slash.vectorf.*

import ai.dragonfly.mesh.sRGB
import ai.dragonfly.mesh.io.PLY

import java.io.File

object ColorSpaceGamutMeshGenerator extends App {

  println("Starting ColorSpaceGamutMeshGenerator")

  val contexts = ai.dragonfly.uriel.ColorContext.knownContexts

  for (context <- contexts) {
    import context.*

    println(s"Context \t$context:")

    def writeMesh(g: Gamut, name: String, toARGB: VecF[3] => sRGB.ARGB32 ):Unit = {
      PLY.writeMeshF(
        g.volumeMesh,
        new java.io.FileOutputStream(new File(s"./docs/ply/$context${name}Gamut.ply")),
        toARGB
      )
    }

    writeMesh(RGB.usableGamut, "RGB_Usable", (cv:VecF[3]) => ARGB32.fromRGB(RGB.fromVec(cv)).asInstanceOf[sRGB.ARGB32])
    writeMesh(LRGB.usableGamut, "LRGB_Usable", (cv:VecF[3]) => ARGB32.fromRGB(LRGB.fromVec(cv).toRGB).asInstanceOf[sRGB.ARGB32])

    writeMesh(CMY.usableGamut, "CMY_Usable", (cv:VecF[3]) => ARGB32.fromRGB(CMY.fromVec(cv).toRGB).asInstanceOf[sRGB.ARGB32])
    //writeMesh(CMYK.usableGamut, "CMYK_Usable", (cv:VecF[4]) => ARGB32.fromRGB(CMYK.fromVec(cv).toRGB).asInstanceOf[sRGB.ARGB32])

    writeMesh(HSL.usableGamut, "HSL_Usable", (cv:VecF[3]) => ARGB32.fromRGB(HSL.fromVec(cv).toRGB).asInstanceOf[sRGB.ARGB32])
    writeMesh(HSV.usableGamut, "HSV_Usable", (cv:VecF[3]) => ARGB32.fromRGB(HSV.fromVec(cv).toRGB).asInstanceOf[sRGB.ARGB32])

    writeMesh(XYZ.fullGamut, "XYZ_Full", (cv:VecF[3]) => Gamut.XYZtoARGB32(cv).asInstanceOf[sRGB.ARGB32])
    writeMesh(XYZ.usableGamut, "XYZ_Usable", (cv:VecF[3]) => Gamut.XYZtoARGB32(cv).asInstanceOf[sRGB.ARGB32])

    writeMesh(Lab.fullGamut, "Lab_Full", (cv:VecF[3]) => Gamut.XYZtoARGB32(Lab.fromVec(cv).toXYZ.asInstanceOf[VecF[3]]).asInstanceOf[sRGB.ARGB32])
    writeMesh(Lab.usableGamut, "Lab_Usable", (cv:VecF[3]) => Gamut.XYZtoARGB32(Lab.fromVec(cv).toXYZ.asInstanceOf[VecF[3]]).asInstanceOf[sRGB.ARGB32])

    writeMesh(Luv.fullGamut, "Luv_Full", (cv:VecF[3]) => Gamut.XYZtoARGB32(Luv.fromVec(cv).toXYZ.asInstanceOf[VecF[3]]).asInstanceOf[sRGB.ARGB32])
    writeMesh(Luv.usableGamut, "Luv_Usable", (cv:VecF[3]) => Gamut.XYZtoARGB32(Luv.fromVec(cv).toXYZ.asInstanceOf[VecF[3]]).asInstanceOf[sRGB.ARGB32])

  }

}