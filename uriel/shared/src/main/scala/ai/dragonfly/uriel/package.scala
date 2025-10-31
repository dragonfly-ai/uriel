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

package ai.dragonfly

import narr.*
import ai.dragonfly.uriel.cie.WorkingSpace
import ai.dragonfly.uriel.color.model.*
import ai.dragonfly.uriel.color.model.huesat.{HSL, HSV}
import ai.dragonfly.uriel.color.model.perceptual.{Lab, Luv, Oklab}
import ai.dragonfly.uriel.color.model.subtractive.{CMY, CMYK}
import ai.dragonfly.uriel.color.spectral.*

import slash.matrix.*

package object uriel {

  inline def valid0to1(i: Float): Boolean = 0f <= i && i <= 1f
  inline def valid0to1(i0: Float, i1: Float):Boolean = valid0to1(i0) && valid0to1(i1)
  inline def valid0to1(i0: Float, i1: Float, i2: Float):Boolean = valid0to1(i0) && valid0to1(i1) && valid0to1(i2)
  inline def valid0to1(i0: Float, i1: Float, i2: Float, i3: Float):Boolean = valid0to1(i0) && valid0to1(i1) && valid0to1(i2) && valid0to1(i3)
  inline def valid0to1(i0: Float, i1: Float, i2: Float, i3: Float, i4: Float):Boolean = valid0to1(i0) && valid0to1(i1) && valid0to1(i2) && valid0to1(i3) && valid0to1(i4)

  inline def clamp0to1(i: Float): Float = Math.min(1f, Math.max(0f, i))
  inline def clamp0to1(i0: Float, i1: Float, i2: Float):NArray[Float] = NArray[Float](
    clamp0to1(i0), clamp0to1(i1), clamp0to1(i2)
  )
  inline def clamp0to1(i0: Float, i1: Float, i2: Float, i3: Float):NArray[Float] = NArray[Float](
    clamp0to1(i0), clamp0to1(i1), clamp0to1(i2), clamp0to1(i3)
  )
  inline def clamp0to1(i0: Float, i1: Float, i2: Float, i3: Float, i4: Float):NArray[Float] = NArray[Float](
    clamp0to1(i0), clamp0to1(i1), clamp0to1(i2), clamp0to1(i3), clamp0to1(i4)
  )

  trait ProvidedColorContexts extends WorkingSpace
    with rgb.RGB
    with rgb.LRGB
    with rgb.discrete.ARGB32
    with rgb.discrete.ARGB64
    with rgb.discrete.RGBA32
    with rgb.discrete.RGBA64
    with CMY
    with CMYK
    with HSL
    with HSV
    with Lab
    with Luv
    with Oklab

  object ColorContext {

    import ai.dragonfly.uriel.cie.*
    import Illuminant.*

    val knownContexts:Array[ProvidedColorContexts] = Array[ProvidedColorContexts](
//      Adobe_RGB_1998,
//      Apple_RGB,
//      Best_RGB,
//      Beta_RGB,
//      Bruce_RGB,  // included as a gesture of gratitude to Bruce Lindenbloom who's work inspired this library and made it possible.
//      CIE_RGB,
//      ColorMatch_RGB,
//      Don_RGB_4,
//      ECI_RGB_v2,
//      Ekta_Space_PS5,
//      NTSC_RGB,
//      PAL_RGB,
//      ProPhoto_RGB,
      SMPTE_C_RGB,
      sRGB,
      Wide_Gamut_RGB,
      P3_D65_Display
    )

    //Adobe RGB (1998)
    // specification: https://www.adobe.com/digitalimag/pdfs/AdobeRGB1998.pdf
    object Adobe_RGB_1998 extends ProvidedColorContexts {
      override val transferFunction:TransferFunction = Gamma(2.19921875)

      override val primaries:ChromaticityPrimaries = ChromaticityPrimaries(
        ChromaticityPrimary( 0.64f, 0.33f, 0.297361f ),
        ChromaticityPrimary( 0.21f, 0.71f, 0.627355f ),
        ChromaticityPrimary( 0.15f, 0.06f, 0.075285f )
      )

      override val illuminant: Illuminant = D65

      val verificationMatrix: Mat[3, 3] = Mat[3, 3](
        1.93939393939394f, 1f, 0.090909090909091f,
        0.295774647887324f, 1f, 0.112676056338028f,
        2.5f, 1f, 13.1666666666667f
      )
    }
    // Apple RGB
    object Apple_RGB extends ProvidedColorContexts {
      override val transferFunction:TransferFunction = Gamma(1.8)

      override val primaries:ChromaticityPrimaries = ChromaticityPrimaries(
        ChromaticityPrimary( 0.625f, 0.34f, 0.244634f ),
        ChromaticityPrimary( 0.28f, 0.595f, 0.672034f ),
        ChromaticityPrimary( 0.155f, 0.07f, 0.083332f )
      )

      override val illuminant: Illuminant = D65

      val verificationMatrix: Mat[3, 3] = Mat[3, 3](
        1.83823529411765f, 1f, 0.102941176470588f,
        0.470588235294118f, 1f, 0.210084033613445f,
        2.21428571428571f, 1f, 11.0714285714286f
      )
    }
    // Best RGB
    object Best_RGB extends ProvidedColorContexts {
      override val transferFunction: TransferFunction = Gamma(2.2)

      override val primaries: ChromaticityPrimaries = ChromaticityPrimaries(
        ChromaticityPrimary( 0.7347f, 0.2653f, 0.228457f),
        ChromaticityPrimary( 0.215f, 0.775f, 0.737352f),
        ChromaticityPrimary( 0.13f, 0.035f, 0.034191f)
      )

      override val illuminant: Illuminant = D50

      val verificationMatrix: Mat[3, 3] = Mat[3, 3](
        2.76931775348662f, 1f, 0f,
        0.27741935483871f, 1f, 0.012903225806452f,
        3.71428571428571f, 1f, 23.8571428571429f
      )
    }

    object Beta_RGB extends ProvidedColorContexts {
      override val transferFunction: TransferFunction = Gamma(2.2)

      override val primaries: ChromaticityPrimaries = ChromaticityPrimaries(
        ChromaticityPrimary( 0.6888f, 0.3112f, 0.303273f),
        ChromaticityPrimary( 0.1986f, 0.7551f, 0.663786f),
        ChromaticityPrimary( 0.1265f, 0.0352f, 0.032941f)
      )

      override val illuminant: Illuminant = D50

      val verificationMatrix: Mat[3, 3] = Mat[3, 3](
        2.2133676092545f, 1f, 0f,
        0.263011521652761f, 1f, 0.061316381936167f,
        3.59375f, 1f, 23.8153409090909f
      )
    }

    /**
     * We honor Bruce here to acknowledge the central role his website played in the design of this library.
     */

    object Bruce_RGB extends ProvidedColorContexts {
      override val transferFunction: TransferFunction = Gamma(2.2)

      override val primaries: ChromaticityPrimaries = ChromaticityPrimaries(
        ChromaticityPrimary( 0.64f, 0.33f, 0.240995f),
        ChromaticityPrimary( 0.28f, 0.65f, 0.683554f),
        ChromaticityPrimary( 0.15f, 0.06f, 0.075452f)
      )

      override val illuminant: Illuminant = D65

      val verificationMatrix: Mat[3, 3] = Mat[3, 3](
        1.93939393939394f, 1f, 0.090909090909091f,
        0.430769230769231f, 1f, 0.107692307692308f,
        2.5f, 1.0f, 13.1666666666667f
      )
    }

    object CIE_RGB extends ProvidedColorContexts {
      override val transferFunction: TransferFunction = Gamma(2.2)

      override val primaries: ChromaticityPrimaries = ChromaticityPrimaries(
        ChromaticityPrimary( 0.735f, 0.265f, 0.176204f),
        ChromaticityPrimary( 0.274f, 0.717f, 0.812985f),
        ChromaticityPrimary( 0.167f, 0.009f, 0.010811f)
      )

      override val illuminant: Illuminant = E

      val verificationMatrix: Mat[3, 3] = Mat[3, 3](
        2.77358490566038f, 1f, 0f,
        0.382147838214784f, 1f, 0.01255230125523f,
        18.5555555555556f, 1f, 91.5555555555556f
      )
    }

    object ColorMatch_RGB extends ProvidedColorContexts {
      override val transferFunction: TransferFunction = Gamma(1.8)

      override val primaries: ChromaticityPrimaries = ChromaticityPrimaries(
        ChromaticityPrimary( 0.63f, 0.34f, 0.274884f),
        ChromaticityPrimary( 0.295f, 0.605f, 0.658132f),
        ChromaticityPrimary( 0.15f, 0.075f, 0.066985f)
      )

      override val illuminant: Illuminant = D50

      val verificationMatrix: Mat[3, 3] = Mat[3, 3](
        1.85294117647059f, 1f, 0.088235294117647f,
        0.487603305785124f, 1f, 0.165289256198347f,
        2f, 1f, 10.3333333333333f
      )
    }

    object Don_RGB_4 extends ProvidedColorContexts {
      override val transferFunction: TransferFunction = Gamma(2.2)

      override val primaries: ChromaticityPrimaries = ChromaticityPrimaries(
        ChromaticityPrimary( 0.696f, 0.3f, 0.27835f),
        ChromaticityPrimary( 0.215f, 0.765f, 0.68797f),
        ChromaticityPrimary( 0.13f, 0.035f, 0.03368f)
      )

      override val illuminant: Illuminant = D50

      val verificationMatrix: Mat[3, 3] = Mat[3, 3](
        2.32f, 1f, 0.013333333333333f,
        0.281045751633987f, 1f, 0.026143790849673f,
        3.71428571428571f, 1f, 23.8571428571429f
      )
    }

    object ECI_RGB_v2 extends ProvidedColorContexts {
      override val transferFunction: TransferFunction = Lstar

      override val primaries: ChromaticityPrimaries = ChromaticityPrimaries(
        ChromaticityPrimary( 0.67f, 0.33f, 0.32025f),
        ChromaticityPrimary( 0.21f, 0.71f, 0.602071f),
        ChromaticityPrimary( 0.14f, 0.08f, 0.077679f)
      )

      override val illuminant: Illuminant = D50

      val verificationMatrix: Mat[3, 3] = Mat[3, 3](
        2.03030303030303f, 1f, 0f,
        0.295774647887324f, 1f, 0.112676056338028f,
        1.75f, 1f, 9.75f
      )
    }

    object Ekta_Space_PS5 extends ProvidedColorContexts {
      override val transferFunction: TransferFunction = Gamma(2.2)

      override val primaries: ChromaticityPrimaries = ChromaticityPrimaries(
        ChromaticityPrimary( 0.695f, 0.305f, 0.260629f),
        ChromaticityPrimary( 0.26f, 0.7f, 0.734946f),
        ChromaticityPrimary( 0.11f, 0.005f, 0.004425f)
      )

      override val illuminant: Illuminant = D50

      val verificationMatrix: Mat[3, 3] = Mat[3, 3](
        2.27868852459016f, 1f, 0f,
        0.371428571428571f, 1f, 0.057142857142857f,
        22f, 1f, 177f
      )
    }

    // 1953 NTSC https://en.wikipedia.org/wiki/NTSC#Colorimetry
    object NTSC_RGB extends ProvidedColorContexts {
      override val transferFunction: TransferFunction = Gamma(2.2)
      override val primaries: ChromaticityPrimaries = ChromaticityPrimaries(
        ChromaticityPrimary( 0.67f, 0.33f, 0.298839f),
        ChromaticityPrimary( 0.21f, 0.71f, 0.586811f),
        ChromaticityPrimary( 0.14f, 0.08f, 0.11435f)
      )

      override val illuminant: Illuminant = C

      val verificationMatrix: Mat[3, 3] = Mat[3, 3](
        2.03030303030303f, 1f, 0f,
        0.295774647887324f, 1f, 0.112676056338028f,
        1.75f, 1f, 9.75f
      )

      override val cmf: SampleSet = CIE1931_2deg_5nm
    }

    // PAL/SECAM RGB
    object PAL_RGB extends ProvidedColorContexts {
      override val transferFunction: TransferFunction = Gamma(2.2)

      override val primaries: ChromaticityPrimaries = ChromaticityPrimaries(
        ChromaticityPrimary( 0.64f, 0.33f, 0.222021f),
        ChromaticityPrimary( 0.29f, 0.6f, 0.706645f),
        ChromaticityPrimary( 0.15f, 0.06f, 0.071334f)
      )

      override val illuminant: Illuminant = D65

      val verificationMatrix: Mat[3, 3] = Mat[3, 3](
        1.93939393939394f, 1f, 0.090909090909091f,
        0.483333333333333f, 1f, 0.183333333333333f,
        2.5f, 1f, 13.1666666666667f
      )
    }

    val SECAM_RGB = PAL_RGB

    object ProPhoto_RGB extends ProvidedColorContexts {
      override val transferFunction: TransferFunction = Gamma(1.8)

      override val primaries: ChromaticityPrimaries = ChromaticityPrimaries(
        ChromaticityPrimary( 0.7347f, 0.2653f, 0.28804f),
        ChromaticityPrimary( 0.1596f, 0.8404f, 0.711874f),
        ChromaticityPrimary( 0.0366f, 0.0001f, 8.6E-05f)
      )

      override val illuminant: Illuminant = D50

      val verificationMatrix: Mat[3, 3] = Mat[3, 3](
        2.76931775348662f, 1f, 0f,
        0.189909566872918f, 1f, 0f,
        366f, 1f, 9633f
      )
    }

    // SMPTE "C" RGB: https://en.wikipedia.org/wiki/NTSC#SMPTE_C
    object SMPTE_C_RGB extends ProvidedColorContexts {
      override val transferFunction: TransferFunction = Gamma(2.2)

      override val primaries: ChromaticityPrimaries = ChromaticityPrimaries(
        ChromaticityPrimary( 0.63f, 0.34f, 0.212395f),
        ChromaticityPrimary( 0.31f, 0.595f, 0.701049f),
        ChromaticityPrimary( 0.155f, 0.07f, 0.086556f)
      )

      override val illuminant: Illuminant = D65

      val verificationMatrix: Mat[3, 3] = Mat[3, 3](
        1.85294117647059f, 1f, 0.088235294117647f,
        0.521008403361345f, 1f, 0.159663865546218f,
        2.21428571428571f, 1f, 11.0714285714286f
      )

      override val cmf: SampleSet = CIE1931_2deg_5nm
    }

    object sRGB extends ProvidedColorContexts {
      override val transferFunction: TransferFunction = sRGB_ICC_V2 // ~2.2 ?

      override val primaries: ChromaticityPrimaries = ChromaticityPrimaries(
        ChromaticityPrimary( 0.64f, 0.33f, 0.212656f),
        ChromaticityPrimary( 0.3f, 0.6f, 0.715158f),
        ChromaticityPrimary( 0.15f, 0.06f, 0.072186f)
      )

      override val illuminant: Illuminant = D65

      val verificationMatrix: Mat[3, 3] = Mat[3, 3](
        1.93939393939394f, 1f, 0.090909090909091f,
        0.5f, 1f, 0.166666666666667f,
        2.5f, 1f, 13.1666666666667f
      )
      override val cmf: SampleSet = CIE1931_2deg_5nm
    }

    object Wide_Gamut_RGB extends ProvidedColorContexts {
      override val transferFunction: TransferFunction = Gamma(2.2)

      override val primaries: ChromaticityPrimaries = ChromaticityPrimaries(
        ChromaticityPrimary( 0.735f, 0.265f, 0.258187f),
        ChromaticityPrimary( 0.115f, 0.826f, 0.724938f),
        ChromaticityPrimary( 0.157f, 0.018f, 0.016875f)
      )

      override val illuminant: Illuminant = D50

      val verificationMatrix: Mat[3, 3] = Mat[3, 3](
        2.77358490566038f, 1f, 0f,
        0.139225181598063f, 1f, 0.071428571428571f,
        8.72222222222222f, 1f, 45.8333333333333f
      )
    }

    object P3_D65_Display extends ProvidedColorContexts {
      //https://www.dcimovies.com/archives/spec_v1/DCI_Digital_Cinema_System_Spec_v1.pdf
      override val primaries: ChromaticityPrimaries = ChromaticityPrimaries(
        ChromaticityPrimary( 0.6800f, 0.3200f, Float.NaN),
        ChromaticityPrimary( 0.2650f, 0.6900f, Float.NaN),
        ChromaticityPrimary( 0.1500f, 0.0600f, Float.NaN)
      )
      //https://www.color.org/chardata/rgb/DisplayP3.xalter
      override val transferFunction: TransferFunction = sRGB_ICC_V2

      override val illuminant: Illuminant = D65
    }

  }

}
