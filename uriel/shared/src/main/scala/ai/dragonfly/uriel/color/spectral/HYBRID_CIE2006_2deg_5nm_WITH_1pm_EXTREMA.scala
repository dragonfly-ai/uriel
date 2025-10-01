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
 * CIE 2006 CMF data revised for uriel.
 * These data consist primarily of the 5 nano meter wavelength step data.
 * It also includes extrema from 0.1 nano meter wavelength step data substituted for its nearest 5 nano meter wavelength.
 * 5 nano meter wavelength steps
 * Source: http://www.cvrl.org/cmfs.htm
 */

object HYBRID_CIE2006_2deg_5nm_WITH_1pm_EXTREMA extends SampleSet {
  override val samples:NArray[Sample] = NArray[Sample](
    Sample(390f, VecF[3]( 0.003769647f,  0.0004146161f,  0.0184726f )),
    Sample(395f, VecF[3]( 0.009382967f,  0.001059646f,  0.04609784f )),
    Sample(400f, VecF[3]( 0.02214302f,  0.002452194f,  0.109609f )),
    Sample(405f, VecF[3]( 0.04742986f,  0.004971717f,  0.2369246f )),
    Sample(410f, VecF[3]( 0.08953803f,  0.00907986f,  0.4508369f )),
    Sample(415f, VecF[3]( 0.1446214f,  0.01429377f,  0.7378822f )),
    Sample(420f, VecF[3]( 0.2035729f,  0.02027369f,  1.051821f )),
    Sample(425f, VecF[3]( 0.2488523f,  0.02612106f,  1.305008f )),
    Sample(430f, VecF[3]( 0.2918246f,  0.03319038f,  1.552826f )),
    Sample(435f, VecF[3]( 0.3227087f,  0.0415794f,  1.74828f )),
    //`λ->XYZ`( 440f, VecF[3]( 0.3482554f,  0.05033657f,  1.917479 ) )f, 
    Sample(442.1f, VecF[3]( 0.3487819f,  0.05346281f,  1.934853f )),
    //`λ->XYZ`( 445f, VecF[3]( 0.3418483f,  0.05743393f,  1.918437 ) )f, 
    Sample(450f, VecF[3]( 0.3224637f,  0.06472352f,  1.848545f )),
    Sample(455f, VecF[3]( 0.2826646f,  0.07238339f,  1.664439f )),
    Sample(460f, VecF[3]( 0.2485254f,  0.08514816f,  1.522157f )),
    Sample(465f, VecF[3]( 0.2219781f,  0.1060145f,  1.42844f )),
    Sample(470f, VecF[3]( 0.1806905f,  0.1298957f,  1.25061f )),
    Sample(475f, VecF[3]( 0.129192f,  0.1535066f,  0.9991789f )),
    Sample(480f, VecF[3]( 0.08182895f,  0.1788048f,  0.7552379f )),
    Sample(485f, VecF[3]( 0.04600865f,  0.2064828f,  0.5617313f )),
    Sample(490f, VecF[3]( 0.02083981f,  0.237916f,  0.4099313f )),
    Sample(495f, VecF[3]( 0.007097731f,  0.285068f,  0.3105939f )),
    Sample(500f, VecF[3]( 0.002461588f,  0.3483536f,  0.2376753f )),
    Sample(505f, VecF[3]( 0.003649178f,  0.4277595f,  0.1720018f )),
    Sample(510f, VecF[3]( 0.01556989f,  0.5204972f,  0.1176796f )),
    Sample(515f, VecF[3]( 0.04315171f,  0.6206256f,  0.08283548f )),
    Sample(520f, VecF[3]( 0.07962917f,  0.718089f,  0.05650407f )),
    Sample(525f, VecF[3]( 0.1268468f,  0.7946448f,  0.03751912f )),
    Sample(530f, VecF[3]( 0.1818026f,  0.8575799f,  0.02438164f )),
    Sample(535f, VecF[3]( 0.2405015f,  0.9071347f,  0.01566174f )),
    Sample(540f, VecF[3]( 0.3098117f,  0.9544675f,  0.00984647f )),
    Sample(545f, VecF[3]( 0.3804244f,  0.9814106f,  0.006131421f )),
    Sample(550f, VecF[3]( 0.4494206f,  0.9890228f,  0.003790291f )),
    //Sample( 555f, VecF[3]( 0.5280233f,  0.9994608f,  0.002327186 ) )f, 
    Sample(556.1f, VecF[3]( 0.5465651f,  1f,  0.002090846f )),
    //Sample(560f, VecF[3]( 0.6133784f,  0.9967737f,  0.001432128f )),
    Sample(565f, VecF[3]( 0.7016774f,  0.9902549f,  0.0008822531f )),
    Sample(570f, VecF[3]( 0.796775f,  0.9732611f,  0.0005452416f )),
    Sample(575f, VecF[3]( 0.8853376f,  0.9424569f,  0.0003386739f )),
    Sample(580f, VecF[3]( 0.9638388f,  0.8963613f,  0.0002117772f )),
    Sample(585f, VecF[3]( 1.051011f,  0.8587203f,  0.0001335031f )),
    //`λ->XYZ`( 590f, VecF[3]( 1.109767f,  0.8115868f,  8.494468E-05 ) )f, 
    Sample(591.4f, VecF[3]( 1.121677f,  0.7963719f,  7.497919E-05f )),
    Sample(595f, VecF[3]( 1.14362f,  0.7544785f,  5.460706E-05f )),
    Sample(599.1f, VecF[3]( 1.15133f,  0.7033109f,  3.832554E-05f )),
    //`λ->XYZ`( 600f, VecF[3]( 1.151033f,  0.6918553f,  3.549661E-05 ) )f, 
    Sample(605f, VecF[3]( 1.134757f,  0.6270066f,  2.334738E-05f )),
    Sample(610f, VecF[3]( 1.083928f,  0.5583746f,  1.554631E-05f )),
    Sample(615f, VecF[3]( 1.007344f,  0.489595f,  1.048387E-05f )),
    Sample(620f, VecF[3]( 0.9142877f,  0.4229897f,  0f )),
    Sample(625f, VecF[3]( 0.8135565f,  0.3609245f,  0f )),
    Sample(630f, VecF[3]( 0.6924717f,  0.2980865f,  0f )),
    Sample(635f, VecF[3]( 0.575541f,  0.2416902f,  0f )),
    Sample(640f, VecF[3]( 0.4731224f,  0.1943124f,  0f )),
    Sample(645f, VecF[3]( 0.3844986f,  0.1547397f,  0f )),
    Sample(650f, VecF[3]( 0.2997374f,  0.119312f,  0f )),
    Sample(655f, VecF[3]( 0.2277792f,  0.08979594f,  0f )),
    Sample(660f, VecF[3]( 0.1707914f,  0.06671045f,  0f )),
    Sample(665f, VecF[3]( 0.1263808f,  0.04899699f,  0f )),
    Sample(670f, VecF[3]( 0.09224597f,  0.03559982f,  0f )),
    Sample(675f, VecF[3]( 0.0663996f,  0.02554223f,  0f )),
    Sample(680f, VecF[3]( 0.04710606f,  0.01807939f,  0f )),
    Sample(685f, VecF[3]( 0.03292138f,  0.01261573f,  0f )),
    Sample(690f, VecF[3]( 0.02262306f,  0.008661284f,  0f )),
    Sample(695f, VecF[3]( 0.01575417f,  0.006027677f,  0f )),
    Sample(700f, VecF[3]( 0.01096778f,  0.004195941f,  0f )),
    Sample(705f, VecF[3]( 0.00760875f,  0.002910864f,  0f )),
    Sample(710f, VecF[3]( 0.005214608f,  0.001995557f,  0f )),
    Sample(715f, VecF[3]( 0.003569452f,  0.001367022f,  0f )),
    Sample(720f, VecF[3]( 0.002464821f,  0.0009447269f,  0f )),
    Sample(725f, VecF[3]( 0.001703876f,  0.000653705f,  0f )),
    Sample(730f, VecF[3]( 0.001186238f,  0.000455597f,  0f )),
    Sample(735f, VecF[3]( 0.0008269535f,  0.0003179738f,  0f )),
    Sample(740f, VecF[3]( 0.0005758303f,  0.0002217445f,  0f )),
    Sample(745f, VecF[3]( 0.0004058303f,  0.0001565566f,  0f )),
    Sample(750f, VecF[3]( 0.0002856577f,  0.0001103928f,  0f )),
    Sample(755f, VecF[3]( 0.0002021853f,  7.827442E-05f,  0f )),
    Sample(760f, VecF[3]( 0.000143827f,  5.578862E-05f,  0f )),
    Sample(765f, VecF[3]( 0.0001024685f,  3.981884E-05f,  0f )),
    Sample(770f, VecF[3]( 7.347551E-05f,  2.860175E-05f,  0f )),
    Sample(775f, VecF[3]( 5.25987E-05f,  2.051259E-05f,  0f )),
    Sample(780f, VecF[3]( 3.806114E-05f,  1.487243E-05f,  0f )),
    Sample(785f, VecF[3]( 2.758222E-05f,  1.080001E-05f,  0f )),
    Sample(790f, VecF[3]( 2.004122E-05f,  7.86392E-06f,  0f )),
    Sample(795f, VecF[3]( 1.458792E-05f,  5.736935E-06f,  0f )),
    Sample(800f, VecF[3]( 1.068141E-05f,  4.211597E-06f,  0f )),
    Sample(805f, VecF[3]( 7.857521E-06f,  3.106561E-06f,  0f )),
    Sample(810f, VecF[3]( 5.768284E-06f,  2.286786E-06f,  0f )),
    Sample(815f, VecF[3]( 4.259166E-06f,  1.693147E-06f,  0f )),
    Sample(820f, VecF[3]( 3.167765E-06f,  1.262556E-06f,  0f )),
    Sample(825f, VecF[3]( 2.358723E-06f,  9.422514E-07f,  0f )),
    Sample(830f, VecF[3]( 1.762465E-06f,  7.05386E-07f,  0f )),
    Sample(835f, VecF[3]( 0f,  0f,  0f ))
  )

  
}
