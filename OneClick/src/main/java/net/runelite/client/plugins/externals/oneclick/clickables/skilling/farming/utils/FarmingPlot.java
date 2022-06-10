/*
 * Copyright (c) 2019 Abex
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.externals.oneclick.clickables.skilling.farming.utils;

import java.util.Set;
import javax.annotation.Nullable;
import lombok.Getter;
import static net.runelite.api.ItemID.*;
import static net.runelite.client.plugins.externals.oneclick.clickables.skilling.farming.utils.FarmingConstants.ALLOTMENT_SEEDS;
import static net.runelite.client.plugins.externals.oneclick.clickables.skilling.farming.utils.FarmingConstants.BUSH_SEEDS;
import static net.runelite.client.plugins.externals.oneclick.clickables.skilling.farming.utils.FarmingConstants.FLOWER_SEEDS;
import static net.runelite.client.plugins.externals.oneclick.clickables.skilling.farming.utils.FarmingConstants.FRUIT_TREE_SAPLINGS;
import static net.runelite.client.plugins.externals.oneclick.clickables.skilling.farming.utils.FarmingConstants.HERB_SEEDS;
import static net.runelite.client.plugins.externals.oneclick.clickables.skilling.farming.utils.FarmingConstants.HOP_SEEDS;
import static net.runelite.client.plugins.externals.oneclick.clickables.skilling.farming.utils.FarmingConstants.TREE_SAPLINGS;

@Getter
public enum FarmingPlot
{
	BELLADONNA(Set.of(BELLADONNA_SEED), true)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Belladonna patch[Rake,Inspect,Guide] 7560,7559,7558,7557
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 4 && value <= 7)
				{
					// Belladonna[Inspect,Guide] 7561,7562,7563,7564
					return new PatchState(Produce.BELLADONNA, CropState.GROWING);
				}
				if (value == 8)
				{
					// Belladonna[Pick,Inspect,Guide] 7565
					return new PatchState(Produce.BELLADONNA, CropState.HARVESTABLE);
				}
				if (value >= 9 && value <= 11)
				{
					// Diseased Belladonna[Cure,Inspect,Guide] 7566,7567,7568
					return new PatchState(Produce.BELLADONNA, CropState.DISEASED);
				}
				if (value >= 12 && value <= 14)
				{
					// Dead Belladonna[Clear,Inspect,Guide] 7569,7570,7571
					return new PatchState(Produce.BELLADONNA, CropState.DEAD);
				}
				return null;
			}
		},
	MUSHROOM(Set.of(MUSHROOM_SPORE), true)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Mushroom patch[Rake,Inspect,Guide] 8314,8313,8312,8311
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 4 && value <= 9)
				{
					// Bittercap Mushrooms[Inspect,Guide] 8315,8316,8317,8318,8319,8320
					return new PatchState(Produce.MUSHROOM, CropState.GROWING);
				}
				if (value >= 10 && value <= 15)
				{
					// Bittercap Mushrooms[Pick,Inspect,Guide] 8321,8322,8323,8324,8325,8326
					return new PatchState(Produce.MUSHROOM, CropState.HARVESTABLE);
				}
				if (value >= 16 && value <= 20)
				{
					// Diseased Bittercap Mushrooms[Cure,Inspect,Guide] 8327,8328,8329,8330,8331
					return new PatchState(Produce.MUSHROOM, CropState.DISEASED);
				}
				if (value >= 21 && value <= 25)
				{
					// Dead Bittercap Mushrooms[Clear,Inspect,Guide] 8332,8333,8334,8335,8336
					return new PatchState(Produce.MUSHROOM, CropState.DEAD);
				}
				return null;
			}
		},
	HESPORI(Set.of(HESPORI_SEED), false)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Hespori patch[Rake,Inspect,Guide] 33722,33723,33724,33725
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				return null;
			}
		},
	ALLOTMENT(ALLOTMENT_SEEDS, true)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Allotment[Rake,Inspect,Guide] 8576,8575,8574,8573
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 6 && value <= 9)
				{
					// Potato seed,Potato plant[Inspect,Guide] 8558,8559,8560,8561
					return new PatchState(Produce.POTATO, CropState.GROWING);
				}
				if (value >= 10 && value <= 12)
				{
					// Potato[Harvest,Inspect,Guide] 8562,8562,8562
					return new PatchState(Produce.POTATO, CropState.HARVESTABLE);
				}
				if (value >= 13 && value <= 16)
				{
					// Onion seeds,Onion plant[Inspect,Guide] 8580,8581,8582,8583
					return new PatchState(Produce.ONION, CropState.GROWING);
				}
				if (value >= 17 && value <= 19)
				{
					// Onion[Harvest,Inspect,Guide] 8584,8584,8584
					return new PatchState(Produce.ONION, CropState.HARVESTABLE);
				}
				if (value >= 20 && value <= 23)
				{
					// Cabbages[Inspect,Guide] 8535,8536,8537,8538
					return new PatchState(Produce.CABBAGE, CropState.GROWING);
				}
				if (value >= 24 && value <= 26)
				{
					// Cabbages[Harvest,Inspect,Guide] 8539,8539,8539
					return new PatchState(Produce.CABBAGE, CropState.HARVESTABLE);
				}
				if (value >= 27 && value <= 30)
				{
					// Tomato plant[Inspect,Guide] 8641,8642,8643,8644
					return new PatchState(Produce.TOMATO, CropState.GROWING);
				}
				if (value >= 31 && value <= 33)
				{
					// Tomato[Harvest,Inspect,Guide] 8645,8645,8645
					return new PatchState(Produce.TOMATO, CropState.HARVESTABLE);
				}
				if (value >= 34 && value <= 39)
				{
					// Sweetcorn seed,Sweetcorn plant[Inspect,Guide] 8618,8619,8620,8621,8622,8623
					return new PatchState(Produce.SWEETCORN, CropState.GROWING);
				}
				if (value >= 40 && value <= 42)
				{
					// Sweetcorn[Harvest,Inspect,Guide] 8624,8624,8624
					return new PatchState(Produce.SWEETCORN, CropState.HARVESTABLE);
				}
				if (value >= 43 && value <= 48)
				{
					// Strawberry seed,Strawberry plant[Inspect,Guide] 8595,8596,8597,8598,8599,8600
					return new PatchState(Produce.STRAWBERRY, CropState.GROWING);
				}
				if (value >= 49 && value <= 51)
				{
					// Strawberry[Harvest,Inspect,Guide] 8601,8601,8601
					return new PatchState(Produce.STRAWBERRY, CropState.HARVESTABLE);
				}
				if (value >= 52 && value <= 59)
				{
					// Watermelon seed,Watermelons[Inspect,Guide] 8656,8657,8658,8659,8660,8661,8662,8663
					return new PatchState(Produce.WATERMELON, CropState.GROWING);
				}
				if (value >= 60 && value <= 62)
				{
					// Watermelon[Harvest,Inspect,Guide] 8664,8664,8664
					return new PatchState(Produce.WATERMELON, CropState.HARVESTABLE);
				}
				if (value >= 63 && value <= 69)
				{
					// Snape grass seedling,Snape grass plant[Inspect,Guide] 33674,33675,33676,33677,33678,33679,33680
					return new PatchState(Produce.SNAPE_GRASS, CropState.GROWING);
				}
				if (value >= 70 && value <= 73)
				{
					// Potato seed,Potato plant[Inspect,Guide] 8563,8564,8565,8566
					return new PatchState(Produce.POTATO, CropState.GROWING);
				}
				if (value >= 77 && value <= 80)
				{
					// Onion seeds,Onion plant[Inspect,Guide] 8585,8586,8587,8588
					return new PatchState(Produce.ONION, CropState.GROWING);
				}
				if (value >= 84 && value <= 87)
				{
					// Cabbages[Inspect,Guide] 8540,8541,8542,8543
					return new PatchState(Produce.CABBAGE, CropState.GROWING);
				}
				if (value >= 91 && value <= 94)
				{
					// Tomato plant[Inspect,Guide] 8646,8647,8648,8649
					return new PatchState(Produce.TOMATO, CropState.GROWING);
				}
				if (value >= 98 && value <= 103)
				{
					// Sweetcorn seed,Sweetcorn plant[Inspect,Guide] 8625,8626,8627,8628,8629,8630
					return new PatchState(Produce.SWEETCORN, CropState.GROWING);
				}
				if (value >= 107 && value <= 112)
				{
					// Strawberry seed,Strawberry plant[Inspect,Guide] 8602,8603,8604,8605,8606,8607
					return new PatchState(Produce.STRAWBERRY, CropState.GROWING);
				}
				if (value >= 116 && value <= 123)
				{
					// Watermelon seed,Watermelons[Inspect,Guide] 8665,8666,8667,8668,8669,8670,8671,8672
					return new PatchState(Produce.WATERMELON, CropState.GROWING);
				}
				if (value >= 128 && value <= 134)
				{
					// Snape grass seedling,Snape grass plant[Inspect,Guide] 33666,33667,33668,33669,33670,33671,33672
					return new PatchState(Produce.SNAPE_GRASS, CropState.GROWING);
				}
				if (value >= 135 && value <= 137)
				{
					// Diseased potatoes[Cure,Inspect,Guide] 8567,8568,8569
					return new PatchState(Produce.POTATO, CropState.DISEASED);
				}
				if (value >= 138 && value <= 140)
				{
					// Snape grass plant[Harvest,Inspect,Guide] 33673,33673,33673
					return new PatchState(Produce.SNAPE_GRASS, CropState.HARVESTABLE);
				}
				if (value >= 142 && value <= 144)
				{
					// Diseased onions[Cure,Inspect,Guide] 8589,8590,8591
					return new PatchState(Produce.ONION, CropState.DISEASED);
				}
				if (value >= 149 && value <= 151)
				{
					// Diseased cabbages[Cure,Inspect,Guide] 8544,8545,8546
					return new PatchState(Produce.CABBAGE, CropState.DISEASED);
				}
				if (value >= 156 && value <= 158)
				{
					// Diseased tomato plant[Cure,Inspect,Guide] 8650,8651,8652
					return new PatchState(Produce.TOMATO, CropState.DISEASED);
				}
				if (value >= 163 && value <= 167)
				{
					// Diseased sweetcorn plant[Cure,Inspect,Guide] 8631,8632,8633,8634,8635
					return new PatchState(Produce.SWEETCORN, CropState.DISEASED);
				}
				if (value >= 172 && value <= 176)
				{
					// Diseased strawberry plant[Cure,Inspect,Guide] 8608,8609,8610,8611,8612
					return new PatchState(Produce.STRAWBERRY, CropState.DISEASED);
				}
				if (value >= 181 && value <= 187)
				{
					// Diseased watermelons[Cure,Inspect,Guide] 8673,8674,8675,8676,8677,8678,8679
					return new PatchState(Produce.WATERMELON, CropState.DISEASED);
				}
				if (value >= 193 && value <= 195)
				{
					// Dead Snape grass[Clear,Inspect,Guide] 33687,33688,33689
					return new PatchState(Produce.SNAPE_GRASS, CropState.DEAD);
				}
				if (value >= 196 && value <= 198)
				{
					// Diseased Snape grass[Cure,Inspect,Guide] 33681,33682,33683
					return new PatchState(Produce.SNAPE_GRASS, CropState.DISEASED);
				}
				if (value >= 199 && value <= 201)
				{
					// Dead potatoes[Clear,Inspect,Guide] 8570,8571,8572
					return new PatchState(Produce.POTATO, CropState.DEAD);
				}
				if (value >= 202 && value <= 204)
				{
					// Diseased Snape grass[Cure,Inspect,Guide] 33684,33685,33686
					return new PatchState(Produce.SNAPE_GRASS, CropState.DISEASED);
				}
				if (value >= 206 && value <= 208)
				{
					// Dead onions[Clear,Inspect,Guide] 8592,8593,8594
					return new PatchState(Produce.ONION, CropState.DEAD);
				}
				if (value >= 209 && value <= 211)
				{
					// Dead Snape grass[Clear,Inspect,Guide] 33690,33691,33692
					return new PatchState(Produce.SNAPE_GRASS, CropState.DEAD);
				}
				if (value >= 213 && value <= 215)
				{
					// Dead cabbages[Clear,Inspect,Guide] 8547,8548,8549
					return new PatchState(Produce.CABBAGE, CropState.DEAD);
				}
				if (value >= 220 && value <= 222)
				{
					// Dead tomato plant[Clear,Inspect,Guide] 8653,8654,8655
					return new PatchState(Produce.TOMATO, CropState.DEAD);
				}
				if (value >= 227 && value <= 231)
				{
					// Dead sweetcorn plant[Clear,Inspect,Guide] 8636,8637,8638,8639,8640
					return new PatchState(Produce.SWEETCORN, CropState.DEAD);
				}
				if (value >= 236 && value <= 240)
				{
					// Dead strawberry plant[Clear,Inspect,Guide] 8613,8614,8615,8616,8617
					return new PatchState(Produce.STRAWBERRY, CropState.DEAD);
				}
				if (value >= 245 && value <= 251)
				{
					// Dead watermelons[Clear,Inspect,Guide] 8680,8681,8682,8683,8684,8685,8686
					return new PatchState(Produce.WATERMELON, CropState.DEAD);
				}
				return null;
			}
		},
	HERB(HERB_SEEDS, true)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Herb patch[Rake,Inspect,Guide] 8135,8134,8133,8132
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 4 && value <= 7)
				{
					// Herbs[Inspect,Guide] 8139,8140,8141,8142
					return new PatchState(Produce.GUAM, CropState.GROWING);
				}
				if (value >= 8 && value <= 10)
				{
					// Herbs[Pick,Inspect,Guide] 8143,8143,8143
					return new PatchState(Produce.GUAM, CropState.HARVESTABLE);
				}
				if (value >= 11 && value <= 14)
				{
					// Herbs[Inspect,Guide] 8139,8140,8141,8142
					return new PatchState(Produce.MARRENTILL, CropState.GROWING);
				}
				if (value >= 15 && value <= 17)
				{
					// Herbs[Pick,Inspect,Guide] 8143,8143,8143
					return new PatchState(Produce.MARRENTILL, CropState.HARVESTABLE);
				}
				if (value >= 18 && value <= 21)
				{
					// Herbs[Inspect,Guide] 8139,8140,8141,8142
					return new PatchState(Produce.TARROMIN, CropState.GROWING);
				}
				if (value >= 22 && value <= 24)
				{
					// Herbs[Pick,Inspect,Guide] 8143,8143,8143
					return new PatchState(Produce.TARROMIN, CropState.HARVESTABLE);
				}
				if (value >= 25 && value <= 28)
				{
					// Herbs[Inspect,Guide] 8139,8140,8141,8142
					return new PatchState(Produce.HARRALANDER, CropState.GROWING);
				}
				if (value >= 29 && value <= 31)
				{
					// Herbs[Pick,Inspect,Guide] 8143,8143,8143
					return new PatchState(Produce.HARRALANDER, CropState.HARVESTABLE);
				}
				if (value >= 32 && value <= 35)
				{
					// Herbs[Inspect,Guide] 8139,8140,8141,8142
					return new PatchState(Produce.RANARR, CropState.GROWING);
				}
				if (value >= 36 && value <= 38)
				{
					// Herbs[Pick,Inspect,Guide] 8143,8143,8143
					return new PatchState(Produce.RANARR, CropState.HARVESTABLE);
				}
				if (value >= 39 && value <= 42)
				{
					// Herbs[Inspect,Guide] 8139,8140,8141,8142
					return new PatchState(Produce.TOADFLAX, CropState.GROWING);
				}
				if (value >= 43 && value <= 45)
				{
					// Herbs[Pick,Inspect,Guide] 8143,8143,8143
					return new PatchState(Produce.TOADFLAX, CropState.HARVESTABLE);
				}
				if (value >= 46 && value <= 49)
				{
					// Herbs[Inspect,Guide] 8139,8140,8141,8142
					return new PatchState(Produce.IRIT, CropState.GROWING);
				}
				if (value >= 50 && value <= 52)
				{
					// Herbs[Pick,Inspect,Guide] 8143,8143,8143
					return new PatchState(Produce.IRIT, CropState.HARVESTABLE);
				}
				if (value >= 53 && value <= 56)
				{
					// Herbs[Inspect,Guide] 8139,8140,8141,8142
					return new PatchState(Produce.AVANTOE, CropState.GROWING);
				}
				if (value >= 57 && value <= 59)
				{
					// Herbs[Pick,Inspect,Guide] 8143,8143,8143
					return new PatchState(Produce.AVANTOE, CropState.HARVESTABLE);
				}
				if (value >= 68 && value <= 71)
				{
					// Herbs[Inspect,Guide] 8139,8140,8141,8142
					return new PatchState(Produce.KWUARM, CropState.GROWING);
				}
				if (value >= 72 && value <= 74)
				{
					// Herbs[Pick,Inspect,Guide] 8143,8143,8143
					return new PatchState(Produce.KWUARM, CropState.HARVESTABLE);
				}
				if (value >= 75 && value <= 78)
				{
					// Herbs[Inspect,Guide] 8139,8140,8141,8142
					return new PatchState(Produce.SNAPDRAGON, CropState.GROWING);
				}
				if (value >= 79 && value <= 81)
				{
					// Herbs[Pick,Inspect,Guide] 8143,8143,8143
					return new PatchState(Produce.SNAPDRAGON, CropState.HARVESTABLE);
				}
				if (value >= 82 && value <= 85)
				{
					// Herbs[Inspect,Guide] 8139,8140,8141,8142
					return new PatchState(Produce.CADANTINE, CropState.GROWING);
				}
				if (value >= 86 && value <= 88)
				{
					// Herbs[Pick,Inspect,Guide] 8143,8143,8143
					return new PatchState(Produce.CADANTINE, CropState.HARVESTABLE);
				}
				if (value >= 89 && value <= 92)
				{
					// Herbs[Inspect,Guide] 8139,8140,8141,8142
					return new PatchState(Produce.LANTADYME, CropState.GROWING);
				}
				if (value >= 93 && value <= 95)
				{
					// Herbs[Pick,Inspect,Guide] 8143,8143,8143
					return new PatchState(Produce.LANTADYME, CropState.HARVESTABLE);
				}
				if (value >= 96 && value <= 99)
				{
					// Herbs[Inspect,Guide] 8139,8140,8141,8142
					return new PatchState(Produce.DWARF_WEED, CropState.GROWING);
				}
				if (value >= 100 && value <= 102)
				{
					// Herbs[Pick,Inspect,Guide] 8143,8143,8143
					return new PatchState(Produce.DWARF_WEED, CropState.HARVESTABLE);
				}
				if (value >= 103 && value <= 106)
				{
					// Herbs[Inspect,Guide] 8139,8140,8141,8142
					return new PatchState(Produce.TORSTOL, CropState.GROWING);
				}
				if (value >= 107 && value <= 109)
				{
					// Herbs[Pick,Inspect,Guide] 8143,8143,8143
					return new PatchState(Produce.TORSTOL, CropState.HARVESTABLE);
				}
				if (value >= 128 && value <= 130)
				{
					// Diseased herbs[Cure,Inspect,Guide] 8144,8145,8146
					return new PatchState(Produce.GUAM, CropState.DISEASED);
				}
				if (value >= 131 && value <= 133)
				{
					// Diseased herbs[Cure,Inspect,Guide] 8144,8145,8146
					return new PatchState(Produce.MARRENTILL, CropState.DISEASED);
				}
				if (value >= 134 && value <= 136)
				{
					// Diseased herbs[Cure,Inspect,Guide] 8144,8145,8146
					return new PatchState(Produce.TARROMIN, CropState.DISEASED);
				}
				if (value >= 137 && value <= 139)
				{
					// Diseased herbs[Cure,Inspect,Guide] 8144,8145,8146
					return new PatchState(Produce.HARRALANDER, CropState.DISEASED);
				}
				if (value >= 140 && value <= 142)
				{
					// Diseased herbs[Cure,Inspect,Guide] 8144,8145,8146
					return new PatchState(Produce.RANARR, CropState.DISEASED);
				}
				if (value >= 143 && value <= 145)
				{
					// Diseased herbs[Cure,Inspect,Guide] 8144,8145,8146
					return new PatchState(Produce.TOADFLAX, CropState.DISEASED);
				}
				if (value >= 146 && value <= 148)
				{
					// Diseased herbs[Cure,Inspect,Guide] 8144,8145,8146
					return new PatchState(Produce.IRIT, CropState.DISEASED);
				}
				if (value >= 149 && value <= 151)
				{
					// Diseased herbs[Cure,Inspect,Guide] 8144,8145,8146
					return new PatchState(Produce.AVANTOE, CropState.DISEASED);
				}
				if (value >= 152 && value <= 154)
				{
					// Diseased herbs[Cure,Inspect,Guide] 8144,8145,8146
					return new PatchState(Produce.KWUARM, CropState.DISEASED);
				}
				if (value >= 155 && value <= 157)
				{
					// Diseased herbs[Cure,Inspect,Guide] 8144,8145,8146
					return new PatchState(Produce.SNAPDRAGON, CropState.DISEASED);
				}
				if (value >= 158 && value <= 160)
				{
					// Diseased herbs[Cure,Inspect,Guide] 8144,8145,8146
					return new PatchState(Produce.CADANTINE, CropState.DISEASED);
				}
				if (value >= 161 && value <= 163)
				{
					// Diseased herbs[Cure,Inspect,Guide] 8144,8145,8146
					return new PatchState(Produce.LANTADYME, CropState.DISEASED);
				}
				if (value >= 164 && value <= 166)
				{
					// Diseased herbs[Cure,Inspect,Guide] 8144,8145,8146
					return new PatchState(Produce.DWARF_WEED, CropState.DISEASED);
				}
				if (value >= 167 && value <= 169)
				{
					// Diseased herbs[Cure,Inspect,Guide] 8144,8145,8146
					return new PatchState(Produce.TORSTOL, CropState.DISEASED);
				}
				if (value >= 170 && value <= 172)
				{
					// Dead herbs[Clear,Inspect,Guide] 8147,8148,8149
					return new PatchState(Produce.ANYHERB, CropState.DEAD);
				}
				if (value >= 192 && value <= 195)
				{
					// Goutweed[Inspect,Guide] 9044,9045,9046,9047
					return new PatchState(Produce.GOUTWEED, CropState.GROWING);
				}
				if (value >= 196 && value <= 197)
				{
					// Goutweed[Pick,Inspect,Guide] 9048,9048
					return new PatchState(Produce.GOUTWEED, CropState.HARVESTABLE);
				}
				if (value >= 198 && value <= 200)
				{
					// Diseased goutweed[Cure,Inspect,Guide] 9049,9050,9051
					return new PatchState(Produce.GOUTWEED, CropState.DISEASED);
				}
				if (value >= 201 && value <= 203)
				{
					// Dead goutweed[Clear,Inspect,Guide] 9052,9053,9054
					return new PatchState(Produce.GOUTWEED, CropState.DEAD);
				}
				return null;
			}
		},
	FLOWER(FLOWER_SEEDS, true)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Flower Patch[Rake,Inspect,Guide] 7843,7842,7841,7840
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 8 && value <= 11)
				{
					// Marigold[Inspect,Guide] 7867,7868,7869,7870
					return new PatchState(Produce.MARIGOLD, CropState.GROWING);
				}
				if (value == 12)
				{
					// Marigold[Pick,Inspect,Guide] 7871
					return new PatchState(Produce.MARIGOLD, CropState.HARVESTABLE);
				}
				if (value >= 13 && value <= 16)
				{
					// Rosemary[Inspect,Guide] 7899,7900,7901,7902
					return new PatchState(Produce.ROSEMARY, CropState.GROWING);
				}
				if (value == 17)
				{
					// Rosemary[Pick,Inspect,Guide] 7903
					return new PatchState(Produce.ROSEMARY, CropState.HARVESTABLE);
				}
				if (value >= 18 && value <= 21)
				{
					// Nasturtium[Inspect,Guide] 7883,7884,7885,7886
					return new PatchState(Produce.NASTURTIUM, CropState.GROWING);
				}
				if (value == 22)
				{
					// Nasturtium[Pick,Inspect,Guide] 7887
					return new PatchState(Produce.NASTURTIUM, CropState.HARVESTABLE);
				}
				if (value >= 23 && value <= 26)
				{
					// Woad plant[Inspect,Guide] 7919,7920,7921,7922
					return new PatchState(Produce.WOAD, CropState.GROWING);
				}
				if (value == 27)
				{
					// Woad plant[Pick,Inspect,Guide] 7923
					return new PatchState(Produce.WOAD, CropState.HARVESTABLE);
				}
				if (value >= 28 && value <= 31)
				{
					// Limpwurt plant[Inspect,Guide] 7851,7852,7853,7854
					return new PatchState(Produce.LIMPWURT, CropState.GROWING);
				}
				if (value == 32)
				{
					// Limpwurt plant[Pick,Inspect,Guide] 7855
					return new PatchState(Produce.LIMPWURT, CropState.HARVESTABLE);
				}
				if (value == 36)
				{
					// Scarecrow[Remove,Inspect,Guide] 7915
					return new PatchState(Produce.SCARECROW, CropState.GROWING);
				}
				if (value >= 37 && value <= 40)
				{
					// White lily[Inspect,Guide] 33650,33651,33652,33653
					return new PatchState(Produce.WHITE_LILY, CropState.GROWING);
				}
				if (value == 41)
				{
					// White lily[Pick,Inspect,Guide] 33654
					return new PatchState(Produce.WHITE_LILY, CropState.HARVESTABLE);
				}
				if (value >= 72 && value <= 75)
				{
					// Marigold[Inspect,Guide] 7872,7873,7874,7875
					return new PatchState(Produce.MARIGOLD, CropState.GROWING);
				}
				if (value >= 77 && value <= 80)
				{
					// Rosemary[Inspect,Guide] 7904,7905,7906,7907
					return new PatchState(Produce.ROSEMARY, CropState.GROWING);
				}
				if (value >= 82 && value <= 85)
				{
					// Nasturtium[Inspect,Guide] 7888,7889,7890,7891
					return new PatchState(Produce.NASTURTIUM, CropState.GROWING);
				}
				if (value >= 87 && value <= 90)
				{
					// Woad plant[Inspect,Guide] 7924,7925,7926,7927
					return new PatchState(Produce.WOAD, CropState.GROWING);
				}
				if (value >= 92 && value <= 95)
				{
					// Limpwurt plant[Inspect,Guide] 7856,7857,7858,7859
					return new PatchState(Produce.LIMPWURT, CropState.GROWING);
				}
				if (value >= 101 && value <= 104)
				{
					// White lily[Inspect,Guide] 33655,33656,33657,33658
					return new PatchState(Produce.WHITE_LILY, CropState.GROWING);
				}
				if (value >= 137 && value <= 139)
				{
					// Diseased marigold[Cure,Inspect,Guide] 7876,7877,7878
					return new PatchState(Produce.MARIGOLD, CropState.DISEASED);
				}
				if (value >= 142 && value <= 144)
				{
					// Diseased rosemary[Cure,Inspect,Guide] 7908,7909,7910
					return new PatchState(Produce.ROSEMARY, CropState.DISEASED);
				}
				if (value >= 147 && value <= 149)
				{
					// Diseased nasturtium[Cure,Inspect,Guide] 7892,7893,7894
					return new PatchState(Produce.NASTURTIUM, CropState.DISEASED);
				}
				if (value >= 152 && value <= 154)
				{
					// Diseased woad plant[Cure,Inspect,Guide] 7928,7929,7930
					return new PatchState(Produce.WOAD, CropState.DISEASED);
				}
				if (value >= 157 && value <= 159)
				{
					// Diseased limpwurt plant[Cure,Inspect,Guide] 7860,7861,7862
					return new PatchState(Produce.LIMPWURT, CropState.DISEASED);
				}
				if (value >= 166 && value <= 168)
				{
					// Diseased White lily[Cure,Inspect,Guide] 33659,33660,33661
					return new PatchState(Produce.WHITE_LILY, CropState.DISEASED);
				}
				if (value >= 201 && value <= 204)
				{
					// Dead marigold[Clear,Inspect,Guide] 7879,7880,7881,7882
					return new PatchState(Produce.MARIGOLD, CropState.DEAD);
				}
				if (value >= 206 && value <= 209)
				{
					// Dead rosemary[Clear,Inspect,Guide] 7911,7912,7913,7914
					return new PatchState(Produce.ROSEMARY, CropState.DEAD);
				}
				if (value >= 211 && value <= 214)
				{
					// Dead nasturtium[Clear,Inspect,Guide] 7895,7896,7897,7898
					return new PatchState(Produce.NASTURTIUM, CropState.DEAD);
				}
				if (value >= 216 && value <= 219)
				{
					// Dead woad plant[Clear,Inspect,Guide] 7931,7932,7933,7934
					return new PatchState(Produce.WOAD, CropState.DEAD);
				}
				if (value >= 221 && value <= 224)
				{
					// Dead limpwurt plant[Clear,Inspect,Guide] 7863,7864,7865,7866
					return new PatchState(Produce.LIMPWURT, CropState.DEAD);
				}
				if (value >= 230 && value <= 233)
				{
					// Dead White lily[Clear,Inspect,Guide] 33662,33663,33664,33665
					return new PatchState(Produce.WHITE_LILY, CropState.DEAD);
				}
				return null;
			}
		},
	BUSH(BUSH_SEEDS, true)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Bush Patch[Rake,Inspect,Guide] 7576,7575,7574,7573
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 5 && value <= 9)
				{
					// Redberry bush[Inspect,Guide] 7692,7693,7694,7695,7696
					return new PatchState(Produce.REDBERRIES, CropState.GROWING);
				}
				if (value >= 10 && value <= 14)
				{
					// Redberry bush[Clear,Inspect,Guide,Pick-from] 7697,7701,7701,7701,7701
					return new PatchState(Produce.REDBERRIES, CropState.HARVESTABLE);
				}
				if (value >= 15 && value <= 20)
				{
					// Cadavaberry bush[Inspect,Guide] 7581,7582,7583,7584,7585,7586
					return new PatchState(Produce.CADAVABERRIES, CropState.GROWING);
				}
				if (value >= 21 && value <= 25)
				{
					// Cadavaberry bush[Clear,Inspect,Guide,Pick-from] 7587,7591,7591,7591,7591
					return new PatchState(Produce.CADAVABERRIES, CropState.HARVESTABLE);
				}
				if (value >= 26 && value <= 32)
				{
					// Dwellberry bush[Inspect,Guide] 7605,7606,7607,7608,7609,7610,7611
					return new PatchState(Produce.DWELLBERRIES, CropState.GROWING);
				}
				if (value >= 33 && value <= 37)
				{
					// Dwellberry bush[Clear,Inspect,Guide,Pick-from] 7612,7616,7616,7616,7616
					return new PatchState(Produce.DWELLBERRIES, CropState.HARVESTABLE);
				}
				if (value >= 38 && value <= 45)
				{
					// Jangerberry bush[Inspect,Guide] 7632,7633,7634,7635,7636,7637,7638,7639
					return new PatchState(Produce.JANGERBERRIES, CropState.GROWING);
				}
				if (value >= 46 && value <= 50)
				{
					// Jangerberry bush[Clear,Inspect,Guide,Pick-from] 7640,7644,7644,7644,7644
					return new PatchState(Produce.JANGERBERRIES, CropState.HARVESTABLE);
				}
				if (value >= 51 && value <= 58)
				{
					// Whiteberry bush[Inspect,Guide] 7713,7714,7715,7716,7717,7718,7719,7720
					return new PatchState(Produce.WHITEBERRIES, CropState.GROWING);
				}
				if (value >= 59 && value <= 63)
				{
					// Whiteberry bush[Clear,Inspect,Guide,Pick-from] 7721,7725,7725,7725,7725
					return new PatchState(Produce.WHITEBERRIES, CropState.HARVESTABLE);
				}
				if (value >= 70 && value <= 74)
				{
					// Diseased redberry bush[Prune,Inspect,Guide] 7703,7704,7705,7706,7707
					return new PatchState(Produce.REDBERRIES, CropState.DISEASED);
				}
				if (value >= 80 && value <= 85)
				{
					// Diseased cadavaberry bush[Prune,Inspect,Guide] 7593,7594,7595,7596,7597,7598
					return new PatchState(Produce.CADAVABERRIES, CropState.DISEASED);
				}
				if (value >= 91 && value <= 97)
				{
					// Diseased dwellberry bush[Prune,Inspect,Guide] 7618,7619,7620,7621,7622,7623,7624
					return new PatchState(Produce.DWELLBERRIES, CropState.DISEASED);
				}
				if (value >= 103 && value <= 110)
				{
					// Diseased jangerberry bush[Prune,Inspect,Guide] 7646,7647,7648,7649,7650,7651,7652,7653
					return new PatchState(Produce.JANGERBERRIES, CropState.DISEASED);
				}
				if (value >= 116 && value <= 123)
				{
					// Diseased whiteberry bush[Prune,Inspect,Guide] 7727,7728,7729,7730,7731,7732,7733,7734
					return new PatchState(Produce.WHITEBERRIES, CropState.DISEASED);
				}
				if (value >= 134 && value <= 138)
				{
					// Dead redberry bush[Clear,Inspect,Guide] 7708,7709,7710,7711,7712
					return new PatchState(Produce.REDBERRIES, CropState.DEAD);
				}
				if (value >= 144 && value <= 149)
				{
					// Dead cadavaberry bush[Clear,Inspect,Guide] 7599,7600,7601,7602,7603,7604
					return new PatchState(Produce.CADAVABERRIES, CropState.DEAD);
				}
				if (value >= 155 && value <= 161)
				{
					// Dead dwellberry bush[Clear,Inspect,Guide] 7625,7626,7627,7628,7629,7630,7631
					return new PatchState(Produce.DWELLBERRIES, CropState.DEAD);
				}
				if (value >= 167 && value <= 174)
				{
					// Dead jangerberry bush[Clear,Inspect,Guide] 7654,7655,7656,7657,7658,7659,7660,7661
					return new PatchState(Produce.JANGERBERRIES, CropState.DEAD);
				}
				if (value >= 180 && value <= 187)
				{
					// Dead whiteberry bush[Clear,Inspect,Guide] 7735,7736,7737,7738,7739,7740,7741,7742
					return new PatchState(Produce.WHITEBERRIES, CropState.DEAD);
				}
				if (value >= 197 && value <= 204)
				{
					// Poison Ivy bush[Inspect,Guide] 7662,7663,7664,7665,7666,7667,7668,7669
					return new PatchState(Produce.POISON_IVY, CropState.GROWING);
				}
				if (value >= 205 && value <= 209)
				{
					// Poison Ivy bush[Clear,Inspect,Guide,Pick-from] 7670,7674,7674,7674,7674
					return new PatchState(Produce.POISON_IVY, CropState.HARVESTABLE);
				}
				if (value >= 210 && value <= 216)
				{
					// Diseased Poison Ivy bush[Prune,Inspect,Guide] 7676,7677,7678,7679,7680,7681,7682
					return new PatchState(Produce.POISON_IVY, CropState.DISEASED);
				}
				if (value >= 217 && value <= 224)
				{
					// Dead Poison Ivy bush[Clear,Inspect,Guide] 7684,7685,7686,7687,7688,7689,7690,7691
					return new PatchState(Produce.POISON_IVY, CropState.DEAD);
				}
				if (value == 225)
				{
					// Diseased Poison Ivy bush[Prune,Inspect,Guide] 7683
					return new PatchState(Produce.POISON_IVY, CropState.DISEASED);
				}
				if (value == 250)
				{
					// Redberry bush[Check-health,Inspect,Guide] 7702
					return new PatchState(Produce.REDBERRIES, CropState.GROWING);
				}
				if (value == 251)
				{
					// Cadavaberry bush[Check-health,Inspect,Guide] 7592
					return new PatchState(Produce.CADAVABERRIES, CropState.GROWING);
				}
				if (value == 252)
				{
					// Dwellberry bush[Check-health,Inspect,Guide] 7617
					return new PatchState(Produce.DWELLBERRIES, CropState.GROWING);
				}
				if (value == 253)
				{
					// Jangerberry bush[Check-health,Inspect,Guide] 7645
					return new PatchState(Produce.JANGERBERRIES, CropState.GROWING);
				}
				if (value == 254)
				{
					// Whiteberry bush[Check-health,Inspect,Guide] 7726
					return new PatchState(Produce.WHITEBERRIES, CropState.GROWING);
				}
				if (value == 255)
				{
					// Poison Ivy bush[Check-health,Inspect,Guide] 7675
					return new PatchState(Produce.POISON_IVY, CropState.GROWING);
				}
				return null;
			}
		},
	FRUIT_TREE(FRUIT_TREE_SAPLINGS, true)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Fruit Tree Patch[Rake,Inspect,Guide] 8050,8049,8048,8047
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 8 && value <= 13)
				{
					// Apple tree[Inspect,Guide] 7935,7936,7937,7938,7939,7940
					return new PatchState(Produce.APPLE, CropState.GROWING);
				}
				if (value >= 14 && value <= 20)
				{
					// Apple tree[Chop-down,Inspect,Guide,Pick-apple] 7941,7942,7943,7944,7945,7946,7947
					return new PatchState(Produce.APPLE, CropState.HARVESTABLE);
				}
				if (value >= 21 && value <= 26)
				{
					// Diseased apple tree[Prune,Inspect,Guide] 7949,7950,7951,7952,7953,7954
					return new PatchState(Produce.APPLE, CropState.DISEASED);
				}
				if (value >= 27 && value <= 32)
				{
					// Dead apple tree[Clear,Inspect,Guide] 7955,7956,7957,7958,7959,7960
					return new PatchState(Produce.APPLE, CropState.DEAD);
				}
				if (value == 33)
				{
					// Apple tree stump[Clear,Inspect,Guide] 7961
					return new PatchState(Produce.APPLE, CropState.HARVESTABLE);
				}
				if (value == 34)
				{
					// Apple tree[Check-health,Inspect,Guide] 7948
					return new PatchState(Produce.APPLE, CropState.GROWING);
				}
				if (value >= 35 && value <= 40)
				{
					// Banana tree[Inspect,Guide] 7993,7994,7995,7996,7997,7998
					return new PatchState(Produce.BANANA, CropState.GROWING);
				}
				if (value >= 41 && value <= 47)
				{
					// Banana tree[Chop-down,Inspect,Guide,Pick-banana] 8000,8001,8002,8003,8004,8005,8006
					return new PatchState(Produce.BANANA, CropState.HARVESTABLE);
				}
				if (value >= 48 && value <= 53)
				{
					// Diseased banana tree[Prune,Inspect,Guide] 8007,8008,8009,8010,8011,8012
					return new PatchState(Produce.BANANA, CropState.DISEASED);
				}
				if (value >= 54 && value <= 59)
				{
					// Dead banana tree[Clear,Inspect,Guide] 8013,8014,8015,8016,8017,8018
					return new PatchState(Produce.BANANA, CropState.DEAD);
				}
				if (value == 60)
				{
					// Banana tree stump[Clear,Inspect,Guide] 8019
					return new PatchState(Produce.BANANA, CropState.HARVESTABLE);
				}
				if (value == 61)
				{
					// Banana tree[Check-health,Inspect,Guide] 7999
					return new PatchState(Produce.BANANA, CropState.GROWING);
				}
				if (value >= 72 && value <= 77)
				{
					// Orange tree[Inspect,Guide] 8051,8052,8053,8054,8055,8056
					return new PatchState(Produce.ORANGE, CropState.GROWING);
				}
				if (value >= 78 && value <= 84)
				{
					// Orange tree[Chop-down,Inspect,Guide,Pick-orange] 8057,8058,8059,8060,8061,8062,8063
					return new PatchState(Produce.ORANGE, CropState.HARVESTABLE);
				}
				if (value >= 85 && value <= 89)
				{
					// Diseased orange tree[Prune,Inspect,Guide] 8065,8066,8067,8068,8069
					return new PatchState(Produce.ORANGE, CropState.DISEASED);
				}
				if (value == 90)
				{
					// Diseased orange tree[Chop-down,Inspect,Guide] 8070
					return new PatchState(Produce.ORANGE, CropState.DISEASED);
				}
				if (value >= 91 && value <= 96)
				{
					// Dead orange tree[Clear,Inspect,Guide] 8071,8072,8073,8074,8075,8076
					return new PatchState(Produce.ORANGE, CropState.DEAD);
				}
				if (value == 97)
				{
					// Orange tree stump[Clear,Inspect,Guide] 8077
					return new PatchState(Produce.ORANGE, CropState.HARVESTABLE);
				}
				if (value == 98)
				{
					// Orange tree[Check-health,Inspect,Guide] 8064
					return new PatchState(Produce.ORANGE, CropState.GROWING);
				}
				if (value >= 99 && value <= 104)
				{
					// Curry tree[Inspect,Guide] 8020,8021,8022,8023,8024,8025
					return new PatchState(Produce.CURRY, CropState.GROWING);
				}
				if (value >= 105 && value <= 111)
				{
					// Curry tree[Chop-down,Inspect,Guide,Pick-leaf] 8026,8027,8028,8029,8030,8031,8032
					return new PatchState(Produce.CURRY, CropState.HARVESTABLE);
				}
				if (value >= 112 && value <= 117)
				{
					// Diseased curry tree[Prune,Inspect,Guide] 8034,8035,8036,8037,8038,8039
					return new PatchState(Produce.CURRY, CropState.DISEASED);
				}
				if (value >= 118 && value <= 123)
				{
					// Dead curry tree[Clear,Inspect,Guide] 8040,8041,8042,8043,8044,8045
					return new PatchState(Produce.CURRY, CropState.DEAD);
				}
				if (value == 124)
				{
					// Curry tree stump[Clear,Inspect,Guide] 8046
					return new PatchState(Produce.CURRY, CropState.HARVESTABLE);
				}
				if (value == 125)
				{
					// Curry tree[Check-health,Inspect,Guide] 8033
					return new PatchState(Produce.CURRY, CropState.GROWING);
				}
				if (value >= 136 && value <= 141)
				{
					// Pineapple plant[Inspect,Guide] 7966,7967,7968,7969,7970,7971
					return new PatchState(Produce.PINEAPPLE, CropState.GROWING);
				}
				if (value >= 142 && value <= 148)
				{
					// Pineapple plant[Chop down,Inspect,Guide,Pick-pineapple] 7972,7973,7974,7975,7976,7977,7978
					return new PatchState(Produce.PINEAPPLE, CropState.HARVESTABLE);
				}
				if (value >= 149 && value <= 154)
				{
					// Diseased pineapple plant[Prune,Inspect,Guide] 7980,7981,7982,7983,7984,7985
					return new PatchState(Produce.PINEAPPLE, CropState.DISEASED);
				}
				if (value >= 155 && value <= 160)
				{
					// Dead pineapple plant[Clear,Inspect,Guide] 7986,7987,7988,7989,7990,7991
					return new PatchState(Produce.PINEAPPLE, CropState.DEAD);
				}
				if (value == 161)
				{
					// Pineapple plant stump[Clear,Inspect,Guide] 7992
					return new PatchState(Produce.PINEAPPLE, CropState.HARVESTABLE);
				}
				if (value == 162)
				{
					// Pineapple plant[Check-health,Inspect,Guide] 7979
					return new PatchState(Produce.PINEAPPLE, CropState.GROWING);
				}
				if (value >= 163 && value <= 168)
				{
					// Papaya tree[Inspect,Guide] 8105,8106,8107,8108,8109,8110
					return new PatchState(Produce.PAPAYA, CropState.GROWING);
				}
				if (value >= 169 && value <= 175)
				{
					// Papaya tree[Chop-down,Inspect,Guide,Pick-fruit] 8111,8112,8113,8114,8115,8116,8117
					return new PatchState(Produce.PAPAYA, CropState.HARVESTABLE);
				}
				if (value >= 176 && value <= 181)
				{
					// Diseased papaya tree[Prune,Inspect,Guide] 8119,8120,8121,8122,8123,8124
					return new PatchState(Produce.PAPAYA, CropState.DISEASED);
				}
				if (value >= 182 && value <= 187)
				{
					// Dead papaya tree[Clear,Inspect,Guide] 8125,8126,8127,8128,8129,8130
					return new PatchState(Produce.PAPAYA, CropState.DEAD);
				}
				if (value == 188)
				{
					// Papaya tree stump[Clear,Inspect,Guide] 8131
					return new PatchState(Produce.PAPAYA, CropState.HARVESTABLE);
				}
				if (value == 189)
				{
					// Papaya tree[Check-health,Inspect,Guide] 8118
					return new PatchState(Produce.PAPAYA, CropState.GROWING);
				}
				if (value >= 200 && value <= 205)
				{
					// Palm tree[Inspect,Guide] 8078,8079,8080,8081,8082,8083
					return new PatchState(Produce.PALM, CropState.GROWING);
				}
				if (value >= 206 && value <= 212)
				{
					// Palm tree[Chop-down,Inspect,Guide,Pick-coconut] 8084,8085,8086,8087,8088,8089,8090
					return new PatchState(Produce.PALM, CropState.HARVESTABLE);
				}
				if (value >= 213 && value <= 218)
				{
					// Diseased palm tree[Prune,Inspect,Guide] 8092,8093,8094,8095,8096,8097
					return new PatchState(Produce.PALM, CropState.DISEASED);
				}
				if (value >= 219 && value <= 224)
				{
					// Dead palm tree[Clear,Inspect,Guide] 8098,8099,8100,8101,8102,8103
					return new PatchState(Produce.PALM, CropState.DEAD);
				}
				if (value == 225)
				{
					// Palm tree stump[Clear,Inspect,Guide] 8104
					return new PatchState(Produce.PALM, CropState.HARVESTABLE);
				}
				if (value == 226)
				{
					// Palm tree[Check-health,Inspect,Guide] 8091
					return new PatchState(Produce.PALM, CropState.GROWING);
				}
				if (value >= 227 && value <= 232)
				{
					// Dragonfruit tree[Inspect,Guide] 34008,34009,34010,34011,34012,34013
					return new PatchState(Produce.DRAGONFRUIT, CropState.GROWING);
				}
				if (value >= 233 && value <= 239)
				{
					// Dragonfruit tree[Chop down,Inspect,Guide,Pick-dragonfruit] 34014,34015,34016,34017,34018,34019,34020
					return new PatchState(Produce.DRAGONFRUIT, CropState.HARVESTABLE);
				}
				if (value >= 240 && value <= 245)
				{
					// Diseased dragonfruit plant[Prune,Inspect,Guide] 34022,34023,34024,34025,34026,34027
					return new PatchState(Produce.DRAGONFRUIT, CropState.DISEASED);
				}
				if (value >= 246 && value <= 251)
				{
					// Dead dragonfruit plant[Clear,Inspect,Guide] 34028,34029,34030,34031,34032,34033
					return new PatchState(Produce.DRAGONFRUIT, CropState.DEAD);
				}
				if (value == 252)
				{
					// Dragonfruit tree stump[Clear,Inspect,Guide] 34034
					return new PatchState(Produce.DRAGONFRUIT, CropState.HARVESTABLE);
				}
				if (value == 253)
				{
					// Dragonfruit tree[Check-health,Inspect,Guide] 34021
					return new PatchState(Produce.DRAGONFRUIT, CropState.GROWING);
				}
				return null;
			}
		},
	HOPS(HOP_SEEDS, true)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Hops Patch[Rake,Inspect,Guide] 8210,8209,8208,8207
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 4 && value <= 7)
				{
					// Hammerstone Hops[Inspect,Guide] 8177,8178,8179,8180
					return new PatchState(Produce.HAMMERSTONE, CropState.GROWING);
				}
				if (value >= 8 && value <= 10)
				{
					// Hammerstone Hops[Harvest,Inspect,Guide] 8181,8181,8181
					return new PatchState(Produce.HAMMERSTONE, CropState.HARVESTABLE);
				}
				if (value >= 11 && value <= 15)
				{
					// Asgarnian Hops[Inspect,Guide] 8154,8155,8156,8157,8158
					return new PatchState(Produce.ASGARNIAN, CropState.GROWING);
				}
				if (value >= 16 && value <= 18)
				{
					// Asgarnian Hops[Harvest,Inspect,Guide] 8159,8159,8159
					return new PatchState(Produce.ASGARNIAN, CropState.HARVESTABLE);
				}
				if (value >= 19 && value <= 24)
				{
					// Yanillian Hops[Inspect,Guide] 8288,8289,8290,8291,8292,8293
					return new PatchState(Produce.YANILLIAN, CropState.GROWING);
				}
				if (value >= 25 && value <= 27)
				{
					// Yanillian Hops[Harvest,Inspect,Guide] 8294,8294,8294
					return new PatchState(Produce.YANILLIAN, CropState.HARVESTABLE);
				}
				if (value >= 28 && value <= 34)
				{
					// Krandorian Hops[Inspect,Guide] 8211,8212,8213,8214,8215,8216,8217
					return new PatchState(Produce.KRANDORIAN, CropState.GROWING);
				}
				if (value >= 35 && value <= 37)
				{
					// Krandorian Hops[Harvest,Inspect,Guide] 8218,8218,8218
					return new PatchState(Produce.KRANDORIAN, CropState.HARVESTABLE);
				}
				if (value >= 38 && value <= 45)
				{
					// Wildblood Hops[Inspect,Guide] 8257,8258,8259,8260,8261,8262,8263,8264
					return new PatchState(Produce.WILDBLOOD, CropState.GROWING);
				}
				if (value >= 46 && value <= 48)
				{
					// Wildblood Hops[Harvest,Inspect,Guide] 8265,8265,8265
					return new PatchState(Produce.WILDBLOOD, CropState.HARVESTABLE);
				}
				if (value >= 49 && value <= 52)
				{
					// Barley[Inspect,Guide] 8192,8193,8194,8195
					return new PatchState(Produce.BARLEY, CropState.GROWING);
				}
				if (value >= 53 && value <= 55)
				{
					// Barley[Harvest,Inspect,Guide] 8196,8196,8196
					return new PatchState(Produce.BARLEY, CropState.HARVESTABLE);
				}
				if (value >= 56 && value <= 60)
				{
					// Jute[Inspect,Guide] 8238,8239,8240,8241,8242
					return new PatchState(Produce.JUTE, CropState.GROWING);
				}
				if (value >= 61 && value <= 63)
				{
					// Jute[Harvest,Inspect,Guide] 8243,8243,8243
					return new PatchState(Produce.JUTE, CropState.HARVESTABLE);
				}
				if (value >= 68 && value <= 71)
				{
					// Hammerstone Hops[Inspect,Guide] 8182,8183,8184,8185
					return new PatchState(Produce.HAMMERSTONE, CropState.GROWING);
				}
				if (value >= 75 && value <= 79)
				{
					// Asgarnian Hops[Inspect,Guide] 8160,8161,8162,8163,8164
					return new PatchState(Produce.ASGARNIAN, CropState.GROWING);
				}
				if (value >= 83 && value <= 88)
				{
					// Yanillian Hops[Inspect,Guide] 8295,8296,8297,8298,8299,8300
					return new PatchState(Produce.YANILLIAN, CropState.GROWING);
				}
				if (value >= 92 && value <= 98)
				{
					// Krandorian Hops[Inspect,Guide] 8219,8220,8221,8222,8223,8224,8225
					return new PatchState(Produce.KRANDORIAN, CropState.GROWING);
				}
				if (value >= 102 && value <= 109)
				{
					// Wildblood Hops[Inspect,Guide] 8266,8267,8268,8269,8270,8271,8272,8273
					return new PatchState(Produce.WILDBLOOD, CropState.GROWING);
				}
				if (value >= 113 && value <= 116)
				{
					// Barley[Inspect,Guide] 8197,8198,8199,8200
					return new PatchState(Produce.BARLEY, CropState.GROWING);
				}
				if (value >= 120 && value <= 124)
				{
					// Jute[Inspect,Guide] 8244,8245,8246,8247,8248
					return new PatchState(Produce.JUTE, CropState.GROWING);
				}
				if (value >= 133 && value <= 135)
				{
					// Diseased Hammerstone Hops[Cure,Inspect,Guide] 8186,8187,8188
					return new PatchState(Produce.HAMMERSTONE, CropState.DISEASED);
				}
				if (value >= 140 && value <= 143)
				{
					// Diseased Asgarnian Hops[Cure,Inspect,Guide] 8165,8166,8167,8168
					return new PatchState(Produce.ASGARNIAN, CropState.DISEASED);
				}
				if (value >= 148 && value <= 152)
				{
					// Diseased Yanillian Hops[Cure,Inspect,Guide] 8301,8302,8303,8304,8305
					return new PatchState(Produce.YANILLIAN, CropState.DISEASED);
				}
				if (value >= 157 && value <= 162)
				{
					// Diseased Krandorian Hops[Cure,Inspect,Guide] 8226,8227,8228,8229,8230,8231
					return new PatchState(Produce.KRANDORIAN, CropState.DISEASED);
				}
				if (value >= 167 && value <= 173)
				{
					// Diseased Wildblood Hops[Cure,Inspect,Guide] 8274,8275,8276,8277,8278,8279,8280
					return new PatchState(Produce.WILDBLOOD, CropState.DISEASED);
				}
				if (value >= 178 && value <= 180)
				{
					// Diseased Barley[Cure,Inspect,Guide] 8201,8202,8203
					return new PatchState(Produce.BARLEY, CropState.DISEASED);
				}
				if (value >= 185 && value <= 188)
				{
					// Diseased Jute[Cure,Inspect,Guide] 8249,8250,8251,8252
					return new PatchState(Produce.JUTE, CropState.DISEASED);
				}
				if (value >= 197 && value <= 199)
				{
					// Dead Hammerstone Hops[Clear,Inspect,Guide] 8189,8190,8191
					return new PatchState(Produce.HAMMERSTONE, CropState.DEAD);
				}
				if (value >= 204 && value <= 207)
				{
					// Dead Asgarnian Hops[Clear,Inspect,Guide] 8169,8170,8171,8172
					return new PatchState(Produce.ASGARNIAN, CropState.DEAD);
				}
				if (value >= 212 && value <= 216)
				{
					// Dead Yanillian Hops[Clear,Inspect,Guide] 8306,8307,8308,8309,8310
					return new PatchState(Produce.YANILLIAN, CropState.DEAD);
				}
				if (value >= 221 && value <= 226)
				{
					// Dead Krandorian Hops[Clear,Inspect,Guide] 8232,8233,8234,8235,8236,8237
					return new PatchState(Produce.KRANDORIAN, CropState.DEAD);
				}
				if (value >= 231 && value <= 237)
				{
					// Dead Wildblood Hops[Clear,Inspect,Guide] 8281,8282,8283,8284,8285,8286,8287
					return new PatchState(Produce.WILDBLOOD, CropState.DEAD);
				}
				if (value >= 242 && value <= 244)
				{
					// Dead Barley[Clear,Inspect,Guide] 8204,8205,8206
					return new PatchState(Produce.BARLEY, CropState.DEAD);
				}
				if (value >= 249 && value <= 252)
				{
					// Dead Jute[Clear,Inspect,Guide] 8253,8254,8255,8256
					return new PatchState(Produce.JUTE, CropState.DEAD);
				}
				return null;
			}
		},
	TREE(TREE_SAPLINGS, true)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8394,8393,8392
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 8 && value <= 11)
				{
					// Oak[Inspect,Guide] 8462,8463,8464,8465
					return new PatchState(Produce.OAK, CropState.GROWING);
				}
				if (value == 12)
				{
					// Oak[Check-health,Inspect,Guide] 8466
					return new PatchState(Produce.OAK, CropState.GROWING);
				}
				if (value == 13)
				{
					// Oak[Chop down,Inspect,Guide] 8467
					return new PatchState(Produce.OAK, CropState.HARVESTABLE);
				}
				if (value == 14)
				{
					// Oak tree stump[Clear,Inspect,Guide] 8468
					return new PatchState(Produce.OAK, CropState.HARVESTABLE);
				}
				if (value >= 15 && value <= 20)
				{
					// Willow Tree[Inspect,Guide] 8481,8482,8483,8484,8485,8486
					return new PatchState(Produce.WILLOW, CropState.GROWING);
				}
				if (value == 21)
				{
					// Willow Tree[Check-health,Inspect,Guide] 8487
					return new PatchState(Produce.WILLOW, CropState.GROWING);
				}
				if (value == 22)
				{
					// Willow Tree[Chop down,Inspect,Guide] 8488
					return new PatchState(Produce.WILLOW, CropState.HARVESTABLE);
				}
				if (value == 23)
				{
					// Willow tree stump[Clear,Inspect,Guide] 8489
					return new PatchState(Produce.WILLOW, CropState.HARVESTABLE);
				}
				if (value >= 24 && value <= 31)
				{
					// Maple Tree[Inspect,Guide] 8435,8436,8437,8438,8439,8440,8441,8442
					return new PatchState(Produce.MAPLE, CropState.GROWING);
				}
				if (value == 32)
				{
					// Maple Tree[Check-health,Inspect,Guide] 8443
					return new PatchState(Produce.MAPLE, CropState.GROWING);
				}
				if (value == 33)
				{
					// Maple Tree[Chop down,Inspect,Guide] 8444
					return new PatchState(Produce.MAPLE, CropState.HARVESTABLE);
				}
				if (value == 34)
				{
					// Tree stump[Clear,Inspect,Guide] 8445
					return new PatchState(Produce.MAPLE, CropState.HARVESTABLE);
				}
				if (value >= 35 && value <= 44)
				{
					// Yew sapling,Yew tree[Inspect,Guide] 8502,8503,8504,8505,8506,8507,8508,8509,8510,8511
					return new PatchState(Produce.YEW, CropState.GROWING);
				}
				if (value == 45)
				{
					// Yew tree[Check-health,Inspect,Guide] 8512
					return new PatchState(Produce.YEW, CropState.GROWING);
				}
				if (value == 46)
				{
					// Yew tree[Chop down,Inspect,Guide] 8513
					return new PatchState(Produce.YEW, CropState.HARVESTABLE);
				}
				if (value == 47)
				{
					// Yew tree stump[Clear,Inspect,Guide] 8514
					return new PatchState(Produce.YEW, CropState.HARVESTABLE);
				}
				if (value >= 48 && value <= 59)
				{
					// Magic Tree[Inspect,Guide] 8396,8397,8398,8399,8400,8401,8402,8403,8404,8405,8406,8407
					return new PatchState(Produce.MAGIC, CropState.GROWING);
				}
				if (value == 60)
				{
					// Magic Tree[Check-health,Inspect,Guide] 8408
					return new PatchState(Produce.MAGIC, CropState.GROWING);
				}
				if (value == 61)
				{
					// Magic Tree[Chop down,Inspect,Guide] 8409
					return new PatchState(Produce.MAGIC, CropState.HARVESTABLE);
				}
				if (value == 62)
				{
					// Magic Tree Stump[Clear,Inspect,Guide] 8410
					return new PatchState(Produce.MAGIC, CropState.HARVESTABLE);
				}
				if (value >= 73 && value <= 75)
				{
					// Diseased Oak[Prune,Inspect,Guide] 8473,8474,8475
					return new PatchState(Produce.OAK, CropState.DISEASED);
				}
				if (value == 77)
				{
					// Diseased Oak[Prune,Inspect,Guide] 8476
					return new PatchState(Produce.OAK, CropState.DISEASED);
				}
				if (value >= 80 && value <= 84)
				{
					// Diseased Willow[Prune,Inspect,Guide] 8490,8491,8492,8493,8494
					return new PatchState(Produce.WILLOW, CropState.DISEASED);
				}
				if (value == 86)
				{
					// Diseased Willow[Prune,Inspect,Guide] 8495
					return new PatchState(Produce.WILLOW, CropState.DISEASED);
				}
				if (value >= 89 && value <= 95)
				{
					// Diseased Maple[Prune,Inspect,Guide] 8446,8447,8448,8449,8450,8451,8452
					return new PatchState(Produce.MAPLE, CropState.DISEASED);
				}
				if (value == 97)
				{
					// Diseased Maple[Prune,Inspect,Guide] 8453
					return new PatchState(Produce.MAPLE, CropState.DISEASED);
				}
				if (value >= 100 && value <= 108)
				{
					// Diseased Yew[Prune,Inspect,Guide] 8515,8516,8517,8518,8519,8520,8521,8522,8523
					return new PatchState(Produce.YEW, CropState.DISEASED);
				}
				if (value == 110)
				{
					// Diseased Yew[Prune,Inspect,Guide] 8524
					return new PatchState(Produce.YEW, CropState.DISEASED);
				}
				if (value >= 113 && value <= 123)
				{
					// Diseased Magic Tree[Prune,Inspect,Guide] 8411,8412,8413,8414,8415,8416,8417,8418,8419,8420,8421
					return new PatchState(Produce.MAGIC, CropState.DISEASED);
				}
				if (value == 125)
				{
					// Diseased Magic Tree[Prune,Inspect,Guide] 8422
					return new PatchState(Produce.MAGIC, CropState.DISEASED);
				}
				if (value >= 137 && value <= 139)
				{
					// Dead Oak[Clear,Inspect,Guide] 8477,8478,8479
					return new PatchState(Produce.OAK, CropState.DEAD);
				}
				if (value == 141)
				{
					// Dead Oak[Clear,Inspect,Guide] 8480
					return new PatchState(Produce.OAK, CropState.DEAD);
				}
				if (value >= 144 && value <= 148)
				{
					// Dead Willow[Clear,Inspect,Guide] 8496,8497,8498,8499,8500
					return new PatchState(Produce.WILLOW, CropState.DEAD);
				}
				if (value == 150)
				{
					// Dead Willow[Clear,Inspect,Guide] 8501
					return new PatchState(Produce.WILLOW, CropState.DEAD);
				}
				if (value >= 153 && value <= 159)
				{
					// Dead Maple[Clear,Inspect,Guide] 8454,8455,8456,8457,8458,8459,8460
					return new PatchState(Produce.MAPLE, CropState.DEAD);
				}
				if (value == 161)
				{
					// Dead Maple[Clear,Inspect,Guide] 8461
					return new PatchState(Produce.MAPLE, CropState.DEAD);
				}
				if (value >= 164 && value <= 172)
				{
					// Dead Yew[Clear,Inspect,Guide] 8525,8526,8527,8528,8529,8530,8531,8532,8533
					return new PatchState(Produce.YEW, CropState.DEAD);
				}
				if (value == 174)
				{
					// Dead Yew[Clear,Inspect,Guide] 8534
					return new PatchState(Produce.YEW, CropState.DEAD);
				}
				if (value >= 177 && value <= 187)
				{
					// Dead Magic Tree[Clear,Inspect,Guide] 8423,8424,8425,8426,8427,8428,8429,8430,8431,8432,8433
					return new PatchState(Produce.MAGIC, CropState.DEAD);
				}
				if (value == 189)
				{
					// Dead Magic Tree[Clear,Inspect,Guide] 8434
					return new PatchState(Produce.MAGIC, CropState.DEAD);
				}
				if (value >= 192 && value <= 197)
				{
					// Willow Tree[Chop down,Inspect,Guide] 8488,8488,8488,8488,8488,8488
					return new PatchState(Produce.WILLOW, CropState.HARVESTABLE);
				}
				return null;
			}
		},
	HARDWOOD_TREE(Set.of(TEAK_SAPLING, MAHOGANY_SAPLING), true)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Tree patch[Rake,Inspect,Guide] 30479,30478,30477,30476
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 8 && value <= 14)
				{
					// Teak Tree[Inspect,Guide] 30437,30438,30439,30440,30441,30442,30443
					return new PatchState(Produce.TEAK, CropState.GROWING);
				}
				if (value == 15)
				{
					// Teak Tree[Check-health,Inspect,Guide] 30444
					return new PatchState(Produce.TEAK, CropState.GROWING);
				}
				if (value == 16)
				{
					// Teak Tree[Chop down,Inspect,Guide] 30445
					return new PatchState(Produce.TEAK, CropState.HARVESTABLE);
				}
				if (value == 17)
				{
					// Tree stump[Clear,Inspect,Guide] 30446
					return new PatchState(Produce.TEAK, CropState.HARVESTABLE);
				}
				if (value >= 18 && value <= 23)
				{
					// Diseased Teak[Prune,Inspect,Guide] 30447,30448,30449,30450,30451,30452
					return new PatchState(Produce.TEAK, CropState.DISEASED);
				}
				if (value >= 24 && value <= 29)
				{
					// Dead Teak[Clear,Inspect,Guide] 30453,30454,30455,30456,30457,30458
					return new PatchState(Produce.TEAK, CropState.DEAD);
				}
				if (value >= 30 && value <= 37)
				{
					// Mahogany sapling,Mahogany tree[Inspect,Guide] 30406,30407,30408,30409,30410,30411,30412,30413
					return new PatchState(Produce.MAHOGANY, CropState.GROWING);
				}
				if (value == 38)
				{
					// Mahogany tree[Check-health,Inspect,Guide] 30416
					return new PatchState(Produce.MAHOGANY, CropState.GROWING);
				}
				if (value == 39)
				{
					// Mahogany tree[Chop down,Inspect,Guide] 30417
					return new PatchState(Produce.MAHOGANY, CropState.HARVESTABLE);
				}
				if (value == 40)
				{
					// Mahogany tree stump[Clear,Inspect,Guide] 30418
					return new PatchState(Produce.MAHOGANY, CropState.HARVESTABLE);
				}
				if (value >= 41 && value <= 47)
				{
					// Diseased Mahogany[Prune,Inspect,Guide] 30419,30420,30421,30422,30423,30424,30425
					return new PatchState(Produce.MAHOGANY, CropState.DISEASED);
				}
				if (value >= 48 && value <= 54)
				{
					// Dead Mahogany[Clear,Inspect,Guide] 30428,30429,30430,30431,30432,30433,30434
					return new PatchState(Produce.MAHOGANY, CropState.DEAD);
				}
				return null;
			}
		},
	REDWOOD(Set.of(REDWOOD_SAPLING), true)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Redwood tree patch[Rake,Inspect,Guide] 34050,34049,34048,34047
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 8 && value <= 17)
				{
					// Redwood tree[Inspect,Guide] 34205,34206,34207,34208,34209,34210,34215,34224,34242,34260
					return new PatchState(Produce.REDWOOD, CropState.GROWING);
				}
				if (value == 18)
				{
					// Redwood tree[Clear,Inspect,Guide] 34278
					return new PatchState(Produce.REDWOOD, CropState.HARVESTABLE);
				}
				if (value >= 19 && value <= 27)
				{
					// Diseased Redwood tree[Prune,Inspect,Guide] 34130,34131,34132,34133,34134,34139,34148,34166,34184
					return new PatchState(Produce.REDWOOD, CropState.DISEASED);
				}
				if (value >= 28 && value <= 36)
				{
					// Dead Redwood tree[Clear,Inspect,Guide] 34061,34062,34063,34064,34065,34070,34079,34097,34115
					return new PatchState(Produce.REDWOOD, CropState.DEAD);
				}
				if (value == 37)
				{
					// Redwood tree[Check-health,Inspect,Guide] 34297
					return new PatchState(Produce.REDWOOD, CropState.GROWING);
				}
				if (value >= 41 && value <= 55)
				{
					// Redwood tree[Clear,Inspect,Guide] 34278,34278,34278,34278,34278,34278,34278,34278,34278,34278,34278,34278,34278,34278,34278
					return new PatchState(Produce.REDWOOD, CropState.HARVESTABLE);
				}
				return null;
			}
		},
	SPIRIT_TREE(Set.of(SPIRIT_SAPLING), true)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Spirit Tree Patch[Rake,Inspect,Guide] 8342,8341,8340,8339
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 8 && value <= 19)
				{
					// Spirit Tree[Inspect,Guide] 8343,8344,8345,8346,8347,8348,8349,8350,8351,8352,8353,8354
					return new PatchState(Produce.SPIRIT_TREE, CropState.GROWING);
				}
				if (value == 20)
				{
					// Spirit Tree[Travel,Talk-to,Inspect,Guide,Clear] 8355
					return new PatchState(Produce.SPIRIT_TREE, CropState.GROWING);
				}
				if (value >= 21 && value <= 31)
				{
					// Diseased Spirit Tree[Prune,Inspect,Guide] 8358,8359,8360,8361,8362,8363,8364,8365,8366,8367,8368
					return new PatchState(Produce.SPIRIT_TREE, CropState.DISEASED);
				}
				if (value >= 32 && value <= 43)
				{
					// Dead Spirit Tree[Clear,Inspect,Guide] 8370,8371,8372,8373,8374,8375,8376,8377,8378,8379,8380,8381
					return new PatchState(Produce.SPIRIT_TREE, CropState.DEAD);
				}
				if (value == 44)
				{
					// Spirit Tree[Check-health,Inspect,Guide] 8356
					return new PatchState(Produce.SPIRIT_TREE, CropState.GROWING);
				}
				return null;
			}
		},
	ANIMA(Set.of(ATTAS_SEED, IASOR_SEED, KRONOS_SEED), false)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Anima patch[Rake,Inspect,Guide] 33983,33982,33981,33980
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 8 && value <= 16)
				{
					// Attas plant[Inspect,Guide] 33991,33992,33993,33994,33995
					// Attas plant[Inspect,Guide] 33995,33995
					// Withering Attas plant[Inspect,Guide] 33996
					// Dead Attas plant[Clear,Inspect,Guide] 33997
					return new PatchState(Produce.ATTAS, CropState.GROWING);
				}
				if (value >= 17 && value <= 25)
				{
					// Iasor plant[Inspect,Guide] 33984,33985,33986,33987,33988
					// Iasor plant[Inspect,Guide] 33988,33988
					// Withering Iasor plant[Inspect,Guide] 33989
					// Dead Iasor plant[Clear,Inspect,Guide] 33990
					return new PatchState(Produce.IASOR, CropState.GROWING);
				}
				if (value >= 26 && value <= 34)
				{
					// Kronos plant[Inspect,Guide] 33999,34000,34001,34002,34003
					// Kronos plant[Inspect,Guide] 34003,34003
					// Withering Kronos plant[Inspect,Guide] 34004
					// Dead Kronos plant[Clear,Inspect,Guide] 34005
					return new PatchState(Produce.KRONOS, CropState.GROWING);
				}
				return null;
			}
		},
	CACTUS(Set.of(CACTUS_SEED, POTATO_CACTUS_SEED), true)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Cactus patch[Rake,Inspect,Guide] 7746,7745,7744,7743
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 8 && value <= 14)
				{
					// Cactus[Inspect,Guide] 7747,7748,7749,7750,7751,7752,7753
					return new PatchState(Produce.CACTUS, CropState.GROWING);
				}
				if (value >= 15 && value <= 18)
				{
					// Cactus[Clear,Inspect,Guide,Pick-spine] 7754,7757,7757,7757
					return new PatchState(Produce.CACTUS, CropState.HARVESTABLE);
				}
				if (value >= 19 && value <= 24)
				{
					// Diseased cactus[Cure,Inspect,Guide] 7759,7760,7761,7762,7763,7764
					return new PatchState(Produce.CACTUS, CropState.DISEASED);
				}
				if (value >= 25 && value <= 30)
				{
					// Dead cactus[Clear,Inspect,Guide] 7765,7766,7767,7768,7769,7770
					return new PatchState(Produce.CACTUS, CropState.DEAD);
				}
				if (value == 31)
				{
					// Cactus[Check-health,Inspect,Guide] 7758
					return new PatchState(Produce.CACTUS, CropState.GROWING);
				}
				if (value >= 32 && value <= 38)
				{
					// Potato cactus[Inspect,Guide] 33734,33735,33736,33737,33738,33739,33740
					return new PatchState(Produce.POTATO_CACTUS, CropState.GROWING);
				}
				if (value >= 39 && value <= 45)
				{
					// Potato cactus[Clear,Inspect,Guide,Pick] 33741,33742,33743,33744,33745,33746,33747
					return new PatchState(Produce.POTATO_CACTUS, CropState.HARVESTABLE);
				}
				if (value >= 46 && value <= 51)
				{
					// Diseased Potato cactus[Cure,Inspect,Guide] 33749,33750,33751,33752,33753,33754
					return new PatchState(Produce.POTATO_CACTUS, CropState.DISEASED);
				}
				if (value >= 52 && value <= 57)
				{
					// Dead Potato cactus[Clear,Inspect,Guide] 33755,33756,33757,33758,33759,33760
					return new PatchState(Produce.POTATO_CACTUS, CropState.DEAD);
				}
				if (value == 58)
				{
					// Potato cactus[Check-health,Inspect,Guide] 33748
					return new PatchState(Produce.POTATO_CACTUS, CropState.GROWING);
				}
				return null;
			}
		},
	SEAWEED(Set.of(SEAWEED_SPORE), true)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Seaweed patch[Rake,Inspect,Guide] 30486,30485,30484,30483
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 4 && value <= 7)
				{
					// Seaweed[Inspect,Guide] 30487,30488,30489,30490
					return new PatchState(Produce.SEAWEED, CropState.GROWING);
				}
				if (value >= 8 && value <= 10)
				{
					// Seaweed[Pick,Inspect,Guide] 30491,30492,30493
					return new PatchState(Produce.SEAWEED, CropState.HARVESTABLE);
				}
				if (value >= 11 && value <= 13)
				{
					// Diseased seaweed[Cure,Inspect,Guide] 30494,30495,30496
					return new PatchState(Produce.SEAWEED, CropState.DISEASED);
				}
				if (value >= 14 && value <= 16)
				{
					// Dead seaweed[Clear,Inspect,Guide] 30497,30498,30499
					return new PatchState(Produce.SEAWEED, CropState.DEAD);
				}
				return null;
			}
		},
	CALQUAT(Set.of(CALQUAT_SAPLING), true)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Calquat patch[Rake,Inspect,Guide] 7775,7774,7773,7772
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 4 && value <= 11)
				{
					// Calquat Tree[Inspect,Guide] 7776,7777,7778,7779,7780,7781,7782,7783
					return new PatchState(Produce.CALQUAT, CropState.GROWING);
				}
				if (value >= 12 && value <= 18)
				{
					// Calquat Tree[Clear,Inspect,Guide,Pick-fruit] 7784,7785,7786,7787,7788,7789,7790
					return new PatchState(Produce.CALQUAT, CropState.HARVESTABLE);
				}
				if (value >= 19 && value <= 25)
				{
					// Diseased Calquat[Prune,Inspect,Guide] 7792,7793,7794,7795,7796,7797,7798
					return new PatchState(Produce.CALQUAT, CropState.DISEASED);
				}
				if (value >= 26 && value <= 33)
				{
					// Dead Calquat[Clear,Inspect,Guide] 7799,7800,7801,7802,7803,7804,7805,7806
					return new PatchState(Produce.CALQUAT, CropState.DEAD);
				}
				if (value == 34)
				{
					// Calquat Tree[Check-health,Inspect,Guide] 7791
					return new PatchState(Produce.CALQUAT, CropState.GROWING);
				}
				return null;
			}
		},
	CELASTRUS(Set.of(CELASTRUS_SAPLING), true)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Celastrus patch[Rake,Inspect,Guide] 33698,33697,33696,33695
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 8 && value <= 12)
				{
					// Celastrus tree[Inspect,Guide] 33699,33700,33701,33702,33703
					return new PatchState(Produce.CELASTRUS, CropState.GROWING);
				}
				if (value == 13)
				{
					// Celastrus tree[Check-health,Inspect,Guide] 33704
					return new PatchState(Produce.CELASTRUS, CropState.GROWING);
				}
				if (value >= 14 && value <= 16)
				{
					// Celastrus tree[Harvest,Inspect,Guide] 33719,33718,33717
					return new PatchState(Produce.CELASTRUS, CropState.HARVESTABLE);
				}
				if (value == 17)
				{
					// Harvested Celastrus tree[Chop,Inspect,Guide] 33720
					return new PatchState(Produce.CELASTRUS, CropState.HARVESTABLE);
				}
				if (value >= 18 && value <= 22)
				{
					// Diseased celastrus tree[Prune,Inspect,Guide] 33705,33706,33707,33708,33709
					return new PatchState(Produce.CELASTRUS, CropState.DISEASED);
				}
				if (value >= 23 && value <= 27)
				{
					// Dead celastrus tree[Clear,Inspect,Guide] 33711,33712,33713,33714,33715
					return new PatchState(Produce.CELASTRUS, CropState.DEAD);
				}
				if (value == 28)
				{
					// Celastrus tree stump[Clear,Inspect,Guide] 33721
					return new PatchState(Produce.CELASTRUS, CropState.HARVESTABLE);
				}
				return null;
			}
		},
	GRAPES(Set.of(GRAPE_SEED), true)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 1)
				{
					// Empty, empty+fertilizer
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 2 && value <= 9)
				{
					return new PatchState(Produce.GRAPE, CropState.GROWING);
				}
				if (value == 10)
				{
					return new PatchState(Produce.GRAPE, CropState.GROWING);
				}
				if (value >= 11 && value <= 15)
				{
					return new PatchState(Produce.GRAPE, CropState.HARVESTABLE);
				}
				return null;
			}
		},
	CRYSTAL_TREE(Set.of(CRYSTAL_SAPLING), false)
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value <= 3)
				{
					// Crystal tree patch[Rake,Inspect,Guide] 34910,34909,34908,34907
					return new PatchState(Produce.WEEDS, CropState.SEEDABLE);
				}
				if (value >= 8 && value <= 13)
				{
					// Crystal tree[Inspect,Guide] 34911,34912,34913,34914,34915,34916
					return new PatchState(Produce.CRYSTAL_TREE, CropState.GROWING);
				}
				if (value == 14)
				{
					// Crystal tree[Check-health,Inspect,Guide] 34917
					return new PatchState(Produce.CRYSTAL_TREE, CropState.GROWING);
				}
				if (value == 15)
				{
					// Crystal tree[Chop-down,Inspect,Guide] 34918
					return new PatchState(Produce.CRYSTAL_TREE, CropState.HARVESTABLE);
				}
				return null;
			}
		};

	@Nullable
	public abstract PatchState forVarbitValue(int value);

	private final Set<Integer> seeds;
	private final boolean compostable;

	FarmingPlot(Set<Integer> seeds, boolean compostable)
	{
		this.seeds = seeds;
		this.compostable = compostable;
	}
}
