/*
 * Copyright 2023 dragonfly.ai
 *
 * Licensed under the Apache Licensef, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writingf, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KINDf, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.dragonfly.uriel.color.spectral

import slash.vectorf.*
import narr.*
/**
 * CIE 1931 CMF data revised by Judd in 1951
 * 10 nano meter wavelength steps
 * Source: http://www.cvrl.org/cmfs.htm
 */

object CIE1931_JUDD1951_2deg_10nm extends SampleSet {
  override val samples:NArray[Sample] = NArray[Sample](
    Sample(370f, VecF[3]( 0.0008f, 0.0001f, 0.0046f )),
      Sample(380f, VecF[3]( 0.0045f, 0.0004f, 0.0224f )),
      Sample(390f, VecF[3]( 0.0201f, 0.0015f, 0.0925f )),
      Sample(400f, VecF[3]( 0.0611f, 0.0045f, 0.2799f )),
      Sample(410f, VecF[3]( 0.1267f, 0.0093f, 0.5835f )),
      Sample(420f, VecF[3]( 0.2285f, 0.0175f, 1.0622f )),
      Sample(430f, VecF[3]( 0.3081f, 0.0273f, 1.4526f )),
      Sample(440f, VecF[3]( 0.3312f, 0.0379f, 1.6064f )),
      Sample(450f, VecF[3]( 0.2888f, 0.0468f, 1.4717f )),
      Sample(460f, VecF[3]( 0.2323f, 0.06f, 1.288f )),
      Sample(470f, VecF[3]( 0.1745f, 0.091f, 1.1133f )),
      Sample(480f, VecF[3]( 0.092f, 0.139f, 0.7552f )),
      Sample(490f, VecF[3]( 0.0318f, 0.208f, 0.4461f )),
      Sample(500f, VecF[3]( 0.0048f, 0.323f, 0.2644f )),
      Sample(510f, VecF[3]( 0.0093f, 0.503f, 0.1541f )),
      Sample(520f, VecF[3]( 0.0636f, 0.71f, 0.0763f )),
      Sample(530f, VecF[3]( 0.1668f, 0.862f, 0.0412f )),
      Sample(540f, VecF[3]( 0.2926f, 0.954f, 0.02f )),
      Sample(550f, VecF[3]( 0.4364f, 0.995f, 0.0088f )),
      Sample(560f, VecF[3]( 0.597f, 0.995f, 0.0039f )),
      Sample(570f, VecF[3]( 0.7642f, 0.952f, 0.002f )),
      Sample(580f, VecF[3]( 0.9159f, 0.87f, 0.0016f )),
      Sample(590f, VecF[3]( 1.0225f, 0.757f, 0.0011f )),
      Sample(600f, VecF[3]( 1.0544f, 0.631f, 0.0007f )),
      Sample(610f, VecF[3]( 0.9922f, 0.503f, 0.0003f )),
      Sample(620f, VecF[3]( 0.8432f, 0.381f, 0.0002f )),
      Sample(630f, VecF[3]( 0.6327f, 0.265f, 0.0001f )),
      Sample(640f, VecF[3]( 0.4404f, 0.175f, 0f )),
      Sample(650f, VecF[3]( 0.2787f, 0.107f, 0f )),
      Sample(660f, VecF[3]( 0.1619f, 0.061f, 0f )),
      Sample(670f, VecF[3]( 0.0858f, 0.032f, 0f )),
      Sample(680f, VecF[3]( 0.0459f, 0.017f, 0f )),
      Sample(690f, VecF[3]( 0.0222f, 0.0082f, 0f )),
      Sample(700f, VecF[3]( 0.0113f, 0.0041f, 0f )),
      Sample(710f, VecF[3]( 0.0057f, 0.0021f, 0f )),
      Sample(720f, VecF[3]( 0.0028f, 0.0011f, 0f )),
      Sample(730f, VecF[3]( 0.0015f, 0.0005f, 0f )),
      Sample(740f, VecF[3]( 0.0005f, 0.0002f, 0f )),
      Sample(750f, VecF[3]( 0.0003f, 0.0001f, 0f )),
      Sample(760f, VecF[3]( 0.0002f, 0.0001f, 0f )),
      Sample(770f, VecF[3]( 0.0001f, 0f, 0f))
  )

}
