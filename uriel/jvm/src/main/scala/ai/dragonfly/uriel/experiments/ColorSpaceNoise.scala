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

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import scala.language.implicitConversions

object ColorSpaceNoise extends App {

  println("Starting ColorSpaceNoise")

  val (w: Int, h: Int) = (512, 512)
  val bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)

  val contexts = ai.dragonfly.uriel.ColorContext.knownContexts

  for (context <- contexts) {
    import context.*

    println(s"\t$context:")

    def noisyImage[C:ColorModel](space:ColorSpace[C]):Unit = {

      for (y <- 0 until h) {
        for (x <- 0 until w) {
          bi.setRGB(x, y, space.random().to_sRGB_ARGB32.asInstanceOf[Int])
        }
      }

      val fileName = s"./docs/image/$context$space.png"
      if (ImageIO.write(bi, "PNG", new File(fileName))) println(s"\t\tWrote $fileName")
      else println(s"\t\tFailed to write $fileName")
    }

    noisyImage(RGB)
    noisyImage(RGBA)

    noisyImage(LRGB)
    noisyImage(LRGBA)

    noisyImage(CMY)
    noisyImage(CMYA)

    noisyImage(CMYK)
    noisyImage(CMYKA)

    noisyImage(HSV)
    noisyImage(HSVA)

    noisyImage(HSL)
    noisyImage(HSLA)

    noisyImage(XYZ)
    noisyImage(XYZA)

    noisyImage(Lab)
    noisyImage(LabA)

    noisyImage(Oklab)
    noisyImage(OklabA)

    noisyImage(Luv)
    noisyImage(LuvA)
  }
}