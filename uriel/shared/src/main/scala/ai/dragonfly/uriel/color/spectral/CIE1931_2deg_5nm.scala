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

/**
 * Original CIE 1931 CMF data at 5 nano meter wavelength steps
 * Source: http://www.cvrl.org/cmfs.htm
 */

object CIE1931_2deg_5nm extends SampleSet {
  override val samples:NArray[Sample] = NArray[Sample](
    Sample( 360f, VecF[3]( 0.0001299f, 3.917E-06f, 0.0006061f )),
    Sample( 365f, VecF[3]( 0.0002321f, 6.965E-06f, 0.001086f )),
    Sample( 370f, VecF[3]( 0.0004149f, 1.239E-05f, 0.001946f )),
    Sample( 375f, VecF[3]( 0.0007416f, 2.202E-05f, 0.003486f )),
    Sample( 380f, VecF[3]( 0.001368f, 3.9E-05f, 0.006450001f )),
    Sample( 385f, VecF[3]( 0.002236f, 6.4E-05f, 0.01054999f )),
    Sample( 390f, VecF[3]( 0.004243f, 0.00012f, 0.02005001f )),
    Sample( 395f, VecF[3]( 0.00765f, 0.000217f, 0.03621f )),
    Sample( 400f, VecF[3]( 0.01431f, 0.000396f, 0.06785001f )),
    Sample( 405f, VecF[3]( 0.02319f, 0.00064f, 0.1102f )),
    Sample( 410f, VecF[3]( 0.04351f, 0.00121f, 0.2074f )),
    Sample( 415f, VecF[3]( 0.07763f, 0.00218f, 0.3713f )),
    Sample( 420f, VecF[3]( 0.13438f, 0.004f, 0.6456f )),
    Sample( 425f, VecF[3]( 0.21477f, 0.0073f, 1.0390501f )),
    Sample( 430f, VecF[3]( 0.2839f, 0.0116f, 1.3856f )),
    Sample( 435f, VecF[3]( 0.3285f, 0.01684f, 1.62296f )),
    Sample( 440f, VecF[3]( 0.34828f, 0.023f, 1.74706f )),
    Sample( 445f, VecF[3]( 0.34806f, 0.0298f, 1.7826f )),
    Sample( 450f, VecF[3]( 0.3362f, 0.038f, 1.77211f )),
    Sample( 455f, VecF[3]( 0.3187f, 0.048f, 1.7441f )),
    Sample( 460f, VecF[3]( 0.2908f, 0.06f, 1.6692f )),
    Sample( 465f, VecF[3]( 0.2511f, 0.0739f, 1.5281f )),
    Sample( 470f, VecF[3]( 0.19536f, 0.09098f, 1.28764f )),
    Sample( 475f, VecF[3]( 0.1421f, 0.1126f, 1.0419f )),
    Sample( 480f, VecF[3]( 0.09564f, 0.13902f, 0.8129501f )),
    Sample( 485f, VecF[3]( 0.05795001f, 0.1693f, 0.6162f )),
    Sample( 490f, VecF[3]( 0.03201f, 0.20802f, 0.46518f )),
    Sample( 495f, VecF[3]( 0.0147f, 0.2586f, 0.3533f )),
    Sample( 500f, VecF[3]( 0.0049f, 0.323f, 0.272f )),
    Sample( 505f, VecF[3]( 0.0024f, 0.4073f, 0.2123f )),
    Sample( 510f, VecF[3]( 0.0093f, 0.503f, 0.1582f )),
    Sample( 515f, VecF[3]( 0.0291f, 0.6082f, 0.1117f )),
    Sample( 520f, VecF[3]( 0.06327f, 0.71f, 0.07824999f )),
    Sample( 525f, VecF[3]( 0.1096f, 0.7932f, 0.05725001f )),
    Sample( 530f, VecF[3]( 0.1655f, 0.862f, 0.04216f )),
    Sample( 535f, VecF[3]( 0.2257499f, 0.9148501f, 0.02984f )),
    Sample( 540f, VecF[3]( 0.2904f, 0.954f, 0.0203f )),
    Sample( 545f, VecF[3]( 0.3597f, 0.9803f, 0.0134f )),
    Sample( 550f, VecF[3]( 0.4334499f, 0.9949501f, 0.008749999f )),
    Sample( 555f, VecF[3]( 0.5120501f, 1f, 0.005749999f )),
    Sample( 560f, VecF[3]( 0.5945f, 0.995f, 0.0039f )),
    Sample( 565f, VecF[3]( 0.6784f, 0.9786f, 0.002749999f )),
    Sample( 570f, VecF[3]( 0.7621f, 0.952f, 0.0021f )),
    Sample( 575f, VecF[3]( 0.8425f, 0.9154f, 0.0018f )),
    Sample( 580f, VecF[3]( 0.9163f, 0.87f, 0.001650001f )),
    Sample( 585f, VecF[3]( 0.9786f, 0.8163f, 0.0014f )),
    Sample( 590f, VecF[3]( 1.0263f, 0.757f, 0.0011f )),
    Sample( 595f, VecF[3]( 1.0567f, 0.6949f, 0.001f )),
    Sample( 600f, VecF[3]( 1.0622f, 0.631f, 0.0008f )),
    Sample( 605f, VecF[3]( 1.0456f, 0.5668f, 0.0006f )),
    Sample( 610f, VecF[3]( 1.0026f, 0.503f, 0.00034f )),
    Sample( 615f, VecF[3]( 0.9384f, 0.4412f, 0.00024f )),
    Sample( 620f, VecF[3]( 0.8544499f, 0.381f, 0.00019f )),
    Sample( 625f, VecF[3]( 0.7514f, 0.321f, 1E-04f )),
    Sample( 630f, VecF[3]( 0.6424f, 0.265f, 4.999999E-05f )),
    Sample( 635f, VecF[3]( 0.5419f, 0.217f, 3E-05f )),
    Sample( 640f, VecF[3]( 0.4479f, 0.175f, 2E-05f )),
    Sample( 645f, VecF[3]( 0.3608f, 0.1382f, 1E-05f )),
    Sample( 650f, VecF[3]( 0.2835f, 0.107f, 0f )),
    Sample( 655f, VecF[3]( 0.2187f, 0.0816f, 0f )),
    Sample( 660f, VecF[3]( 0.1649f, 0.061f, 0f )),
    Sample( 665f, VecF[3]( 0.1212f, 0.04458f, 0f )),
    Sample( 670f, VecF[3]( 0.0874f, 0.032f, 0f )),
    Sample( 675f, VecF[3]( 0.0636f, 0.0232f, 0f )),
    Sample( 680f, VecF[3]( 0.04677f, 0.017f, 0f )),
    Sample( 685f, VecF[3]( 0.0329f, 0.01192f, 0f )),
    Sample( 690f, VecF[3]( 0.0227f, 0.00821f, 0f )),
    Sample( 695f, VecF[3]( 0.01584f, 0.005723f, 0f )),
    Sample( 700f, VecF[3]( 0.01135916f, 0.004102f, 0f )),
    Sample( 705f, VecF[3]( 0.008110916f, 0.002929f, 0f )),
    Sample( 710f, VecF[3]( 0.005790346f, 0.002091f, 0f )),
    Sample( 715f, VecF[3]( 0.004109457f, 0.001484f, 0f )),
    Sample( 720f, VecF[3]( 0.002899327f, 0.001047f, 0f )),
    Sample( 725f, VecF[3]( 0.00204919f, 0.00074f, 0f )),
    Sample( 730f, VecF[3]( 0.001439971f, 0.00052f, 0f )),
    Sample( 735f, VecF[3]( 0.0009999493f, 0.0003611f, 0f )),
    Sample( 740f, VecF[3]( 0.0006900786f, 0.0002492f, 0f )),
    Sample( 745f, VecF[3]( 0.0004760213f, 0.0001719f, 0f )),
    Sample( 750f, VecF[3]( 0.0003323011f, 0.00012f, 0f )),
    Sample( 755f, VecF[3]( 0.0002348261f, 8.48E-05f, 0f )),
    Sample( 760f, VecF[3]( 0.0001661505f, 6E-05f, 0f )),
    Sample( 765f, VecF[3]( 0.000117413f, 4.24E-05f, 0f )),
    Sample( 770f, VecF[3]( 8.307527E-05f, 3E-05f, 0f )),
    Sample( 775f, VecF[3]( 5.870652E-05f, 2.12E-05f, 0f )),
    Sample( 780f, VecF[3]( 4.150994E-05f, 1.499E-05f, 0f )),
    Sample( 785f, VecF[3]( 2.935326E-05f, 1.06E-05f, 0f )),
    Sample( 790f, VecF[3]( 2.067383E-05f, 7.4657E-06f, 0f )),
    Sample( 795f, VecF[3]( 1.455977E-05f, 5.2578E-06f, 0f )),
    Sample( 800f, VecF[3]( 1.025398E-05f, 3.7029E-06f, 0f )),
    Sample( 805f, VecF[3]( 7.221456E-06f, 2.6078E-06f, 0f )),
    Sample( 810f, VecF[3]( 5.085868E-06f, 1.8366E-06f, 0f )),
    Sample( 815f, VecF[3]( 3.581652E-06f, 1.2934E-06f, 0f )),
    Sample( 820f, VecF[3]( 2.522525E-06f, 9.1093E-07f, 0f )),
    Sample( 825f, VecF[3]( 1.776509E-06f, 6.4153E-07f, 0f )),
    Sample( 830f, VecF[3]( 1.251141E-06f, 4.5181E-07f, 0f ))
  )

}
