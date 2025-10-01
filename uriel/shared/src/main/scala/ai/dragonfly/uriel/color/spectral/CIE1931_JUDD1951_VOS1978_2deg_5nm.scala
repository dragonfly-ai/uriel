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

import slash.vectorf.*
import narr.*
/**
 * CIE 1931 CMF data revised by Judd in 1951 and Vos in 1978
 * 5 nano meter wavelength steps
 * Source: http://www.cvrl.org/cmfs.htm
 */

object CIE1931_JUDD1951_VOS1978_2deg_5nm extends SampleSet {
  override val samples:NArray[Sample] = NArray[Sample]( 
    Sample(360f, VecF[3]( 1.222E-07f, 1.3398E-08f, 5.35027E-07f )),
      Sample(365f, VecF[3]( 9.1927E-07f, 1.0065E-07f, 4.0283E-06f )),
      Sample(370f, VecF[3]( 5.9586E-06f, 6.511E-07f, 2.61437E-05f )),
      Sample(375f, VecF[3]( 3.3266E-05f, 3.625E-06f, 0.00014622f )),
      Sample(380f, VecF[3]( 0.000159952f, 1.7364E-05f, 0.000704776f )),
      Sample(385f, VecF[3]( 0.00066244f, 7.156E-05f, 0.0029278f )),
      Sample(390f, VecF[3]( 0.0023616f, 0.0002534f, 0.0104822f )),
      Sample(395f, VecF[3]( 0.0072423f, 0.0007685f, 0.032344f )),
      Sample(400f, VecF[3]( 0.0191097f, 0.0020044f, 0.0860109f )),
      Sample(405f, VecF[3]( 0.0434f, 0.004509f, 0.19712f )),
      Sample(410f, VecF[3]( 0.084736f, 0.008756f, 0.389366f )),
      Sample(415f, VecF[3]( 0.140638f, 0.014456f, 0.65676f )),
      Sample(420f, VecF[3]( 0.204492f, 0.021391f, 0.972542f )),
      Sample(425f, VecF[3]( 0.264737f, 0.029497f, 1.2825f )),
      Sample(430f, VecF[3]( 0.314679f, 0.038676f, 1.55348f )),
      Sample(435f, VecF[3]( 0.357719f, 0.049602f, 1.7985f )),
      Sample(440f, VecF[3]( 0.383734f, 0.062077f, 1.96728f )),
      Sample(445f, VecF[3]( 0.386726f, 0.074704f, 2.0273f )),
      Sample(450f, VecF[3]( 0.370702f, 0.089456f, 1.9948f )),
      Sample(455f, VecF[3]( 0.342957f, 0.106256f, 1.9007f )),
      Sample(460f, VecF[3]( 0.302273f, 0.128201f, 1.74537f )),
      Sample(465f, VecF[3]( 0.254085f, 0.152761f, 1.5549f )),
      Sample(470f, VecF[3]( 0.195618f, 0.18519f, 1.31756f )),
      Sample(475f, VecF[3]( 0.132349f, 0.21994f, 1.0302f )),
      Sample(480f, VecF[3]( 0.080507f, 0.253589f, 0.772125f )),
      Sample(485f, VecF[3]( 0.041072f, 0.297665f, 0.57006f )),
      Sample(490f, VecF[3]( 0.016172f, 0.339133f, 0.415254f )),
      Sample(495f, VecF[3]( 0.005132f, 0.395379f, 0.302356f )),
      Sample(500f, VecF[3]( 0.003816f, 0.460777f, 0.218502f )),
      Sample(505f, VecF[3]( 0.015444f, 0.53136f, 0.159249f )),
      Sample(510f, VecF[3]( 0.037465f, 0.606741f, 0.112044f )),
      Sample(515f, VecF[3]( 0.071358f, 0.68566f, 0.082248f )),
      Sample(520f, VecF[3]( 0.117749f, 0.761757f, 0.060709f )),
      Sample(525f, VecF[3]( 0.172953f, 0.82333f, 0.04305f )),
      Sample(530f, VecF[3]( 0.236491f, 0.875211f, 0.030451f )),
      Sample(535f, VecF[3]( 0.304213f, 0.92381f, 0.020584f )),
      Sample(540f, VecF[3]( 0.376772f, 0.961988f, 0.013676f )),
      Sample(545f, VecF[3]( 0.451584f, 0.9822f, 0.007918f )),
      Sample(550f, VecF[3]( 0.529826f, 0.991761f, 0.003988f )),
      Sample(555f, VecF[3]( 0.616053f, 0.99911f, 0.001091f )),
      Sample(560f, VecF[3]( 0.705224f, 0.99734f, 0f )),
      Sample(565f, VecF[3]( 0.793832f, 0.98238f, 0f )),
      Sample(570f, VecF[3]( 0.878655f, 0.955552f, 0f )),
      Sample(575f, VecF[3]( 0.951162f, 0.915175f, 0f )),
      Sample(580f, VecF[3]( 1.01416f, 0.868934f, 0f )),
      Sample(585f, VecF[3]( 1.0743f, 0.825623f, 0f )),
      Sample(590f, VecF[3]( 1.11852f, 0.777405f, 0f )),
      Sample(595f, VecF[3]( 1.1343f, 0.720353f, 0f )),
      Sample(600f, VecF[3]( 1.12399f, 0.658341f, 0f )),
      Sample(605f, VecF[3]( 1.0891f, 0.593878f, 0f )),
      Sample(610f, VecF[3]( 1.03048f, 0.527963f, 0f )),
      Sample(615f, VecF[3]( 0.95074f, 0.461834f, 0f )),
      Sample(620f, VecF[3]( 0.856297f, 0.398057f, 0f )),
      Sample(625f, VecF[3]( 0.75493f, 0.339554f, 0f )),
      Sample(630f, VecF[3]( 0.647467f, 0.283493f, 0f )),
      Sample(635f, VecF[3]( 0.53511f, 0.228254f, 0f )),
      Sample(640f, VecF[3]( 0.431567f, 0.179828f, 0f )),
      Sample(645f, VecF[3]( 0.34369f, 0.140211f, 0f )),
      Sample(650f, VecF[3]( 0.268329f, 0.107633f, 0f )),
      Sample(655f, VecF[3]( 0.2043f, 0.081187f, 0f )),
      Sample(660f, VecF[3]( 0.152568f, 0.060281f, 0f )),
      Sample(665f, VecF[3]( 0.11221f, 0.044096f, 0f )),
      Sample(670f, VecF[3]( 0.0812606f, 0.0318004f, 0f )),
      Sample(675f, VecF[3]( 0.05793f, 0.0226017f, 0f )),
      Sample(680f, VecF[3]( 0.0408508f, 0.0159051f, 0f )),
      Sample(685f, VecF[3]( 0.028623f, 0.0111303f, 0f )),
      Sample(690f, VecF[3]( 0.0199413f, 0.0077488f, 0f )),
      Sample(695f, VecF[3]( 0.013842f, 0.0053751f, 0f )),
      Sample(700f, VecF[3]( 0.00957688f, 0.00371774f, 0f )),
      Sample(705f, VecF[3]( 0.0066052f, 0.00256456f, 0f )),
      Sample(710f, VecF[3]( 0.00455263f, 0.00176847f, 0f )),
      Sample(715f, VecF[3]( 0.0031447f, 0.00122239f, 0f )),
      Sample(720f, VecF[3]( 0.00217496f, 0.00084619f, 0f )),
      Sample(725f, VecF[3]( 0.0015057f, 0.00058644f, 0f )),
      Sample(730f, VecF[3]( 0.00104476f, 0.00040741f, 0f )),
      Sample(735f, VecF[3]( 0.00072745f, 0.000284041f, 0f )),
      Sample(740f, VecF[3]( 0.000508258f, 0.00019873f, 0f )),
      Sample(745f, VecF[3]( 0.00035638f, 0.00013955f, 0f )),
      Sample(750f, VecF[3]( 0.000250969f, 9.8428E-05f, 0f )),
      Sample(755f, VecF[3]( 0.00017773f, 6.9819E-05f, 0f )),
      Sample(760f, VecF[3]( 0.00012639f, 4.9737E-05f, 0f )),
      Sample(765f, VecF[3]( 9.0151E-05f, 3.55405E-05f, 0f )),
      Sample(770f, VecF[3]( 6.45258E-05f, 2.5486E-05f, 0f )),
      Sample(775f, VecF[3]( 4.6339E-05f, 1.83384E-05f, 0f )),
      Sample(780f, VecF[3]( 3.34117E-05f, 1.3249E-05f, 0f )),
      Sample(785f, VecF[3]( 2.4209E-05f, 9.6196E-06f, 0f )),
      Sample(790f, VecF[3]( 1.76115E-05f, 7.0128E-06f, 0f )),
      Sample(795f, VecF[3]( 1.2855E-05f, 5.1298E-06f, 0f )),
      Sample(800f, VecF[3]( 9.41363E-06f, 3.76473E-06f, 0f )),
      Sample(805f, VecF[3]( 6.913E-06f, 2.77081E-06f, 0f )),
      Sample(810f, VecF[3]( 5.09347E-06f, 2.04613E-06f, 0f )),
      Sample(815f, VecF[3]( 3.7671E-06f, 1.51677E-06f, 0f )),
      Sample(820f, VecF[3]( 2.79531E-06f, 1.12809E-06f, 0f )),
      Sample(825f, VecF[3]( 2.082E-06f, 8.4216E-07f, 0f )),
      Sample(830f, VecF[3]( 1.55314E-06f, 6.297E-07f, 0f ))
  )

}
