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

import ai.dragonfly.uriel.ColorContext

class Conversion extends munit.FunSuite {

  test("Random Conversions") {

    for (ctx <- ColorContext.knownContexts) {
      import ctx.*

      var i: Int = 0
      while (i < 1000) {
        val c = ARGB32.random()
        // ARGB -> RGB -> ARGB
        val rgb = c.toRGB
        var err = 1.0 - c.similarity(ARGB32.fromRGB(rgb))
        if (err != 0.0) println(s"$ctx${c.render} ${ARGB32.fromRGB(rgb).render} $err")
        assertEquals(err, 0.0)

        // ARGB -> RGB -> ARGB
        val rgba: RGBA32 = RGBA32.fromRGB(rgb)
        err = 1.0 - c.similarity(ARGB32.fromRGB(rgba.toRGB))
        if (err != 0.0) println(s"$ctx${c.render} $err")
        assertEquals(err, 0.0)

        // ARGB -> RGB -> CMY -> RGB -> ARGB
        val cmy: CMY = CMY.fromRGB(rgb)
        var cT = ARGB32.fromRGB(cmy.toRGB)
        err = 1.0 - c.similarity(cT)
        if (err != 0.0) println(s"$ctx${c.render} -> ${cmy.render} -> ${cT.render}: $err")
        assertEquals(err, 0.0)

        // ARGB -> RGB -> CMYK -> RGB -> ARGB
        val cmyk: CMYK = CMYK.fromRGB(rgb)
        cT = ARGB32.fromRGB(cmyk.toRGB)
        err = 1.0 - c.similarity(cT)
        if (err != 0.0) println(s"$ctx${c.render} -> ${cmyk.render} -> ${cT.render}: $err")
        assertEquals(err, 0.0)

        // ARGB -> RGB -> HSV -> RGB -> ARGB
        val hsv: HSV = HSV.fromRGB(rgb)
        cT = ARGB32.fromRGB(hsv.toRGB)
        err = 1.0 - c.similarity(cT)
        if (err != 0.0) {
          println(rgb.render)
          println(hsv.toRGB.render)
          println(s"$ctx${c.render} -> ${hsv.render} -> ${cT.render}: $err")
        }
        assertEquals(err, 0.0)

        // ARGB -> RGB -> HSL -> RGB -> ARGB
        val hsl: HSL = HSL.fromRGB(rgb)
        cT = ARGB32.fromRGB(hsl.toRGB)
        err = 1.0 - c.similarity(cT)
        if (err != 0.0) {
          println(rgb)
          println(hsl.toRGB)
          println(s"$ctx${c.render} -> ${hsl.render} -> ${cT.render}: $err")
        }
        assertEquals(err, 0.0)

        // ARGB -> RGB -> XYZ -> RGB -> ARGB
        val xyz: XYZ = rgb.toXYZ
        cT = ARGB32.fromRGB(RGB.fromXYZ(xyz))
        err = 1.0 - c.similarity(cT)
        if (err != 0.0) println(s"$ctx${c.render} -> ${xyz.render} -> ${cT.render}: $err")
        assertEquals(err, 0.0)

        // ARGB -> RGB -> XYZ -> Lab -> XYZ -> RGB -> ARGB
        val lab: Lab = Lab.fromXYZ(xyz)
        cT = ARGB32.fromRGB(RGB.fromXYZ(lab.toXYZ))
        err = 1.0 - c.similarity(cT)
        if (err != 0.0) println(s"$ctx${c.render} -> ${lab.render} -> ${cT.render}: $err")
        assertEquals(err, 0.0)

        // ARGB -> RGB -> XYZ -> Luv -> XYZ -> RGB -> ARGB
        val luv: Luv = Luv.fromXYZ(xyz)
        cT = ARGB32.fromRGB(RGB.fromXYZ(luv.toXYZ))
        err = 1.0 - c.similarity(cT)
        if (err != 0.0) println(s"$ctx${c.render} -> ${luv.render} -> ${cT.render}: $err")
        assertEquals(err, 0.0)
        i = i + 1
      }
    }
  }
}
