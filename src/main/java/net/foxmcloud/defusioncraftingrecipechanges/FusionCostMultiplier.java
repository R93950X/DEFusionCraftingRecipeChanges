package net.foxmcloud.defusioncraftingrecipechanges;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.brandon3055.brandonscore.registry.ModConfigContainer;
import com.brandon3055.brandonscore.registry.ModConfigProperty;
import com.brandon3055.draconicevolution.DEFeatures;
import com.brandon3055.draconicevolution.api.fusioncrafting.FusionRecipeAPI;
import com.brandon3055.draconicevolution.api.fusioncrafting.IFusionRecipe;
import com.brandon3055.draconicevolution.api.fusioncrafting.SimpleFusionRecipe;
import com.brandon3055.draconicevolution.api.itemupgrade.FusionUpgradeRecipe;
import com.brandon3055.draconicevolution.utils.LogHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@ModConfigContainer(modid = DEFusionCraftingRecipeChanges.MODID)
public class FusionCostMultiplier {
	@ModConfigProperty(category = "Main Settings", name = "Upgrade Energy Costs", comment = "Energy cost for each tier of upgrade. This is the energy cost per ingredient in the recipe.", requiresMCRestart = true, requiresSync = true)
	public static double[] energyCost = new double[] {
			32e3,
			512e3,
			32e6,
			256e6
	};

	@ModConfigProperty(category = "Main Settings", name = "Basic Upgrade Ingredients", comment = "Ingredients for the first upgrade tier.", requiresMCRestart = true, requiresSync = true)
	public static String[] basicRecipe = new String[] {
			"minecraft:golden_apple",
			"minecraft:golden_apple",
			"minecraft:diamond",
			"minecraft:diamond",
			"minecraft:ender_eye",
			"minecraft:ender_eye",
			"draconicevolution:draconic_core"
	};

	@ModConfigProperty(category = "Main Settings", name = "Wyvern Upgrade Ingredients", comment = "Ingredients for the second upgrade tier.", requiresMCRestart = true, requiresSync = true)
	public static String[] wyvernRecipe = new String[] {
			"minecraft:nether_star",
			"minecraft:nether_star",
			"draconicevolution:draconic_core",
			"draconicevolution:draconic_core",
			"minecraft:emerald",
			"minecraft:emerald",
			"draconicevolution:wyvern_core"
	};

	@ModConfigProperty(category = "Main Settings", name = "Draconic Upgrade Ingredients", comment = "Ingredients for the third upgrade tier.", requiresMCRestart = true, requiresSync = true)
	public static String[] draconicRecipe = new String[] {
			"minecraft:nether_star",
			"minecraft:nether_star",
			"draconicevolution:wyvern_core",
			"draconicevolution:wyvern_core",
			"minecraft:emerald_block",
			"minecraft:emerald_block",
			"draconicevolution:awakened_core"
	};

	@ModConfigProperty(category = "Main Settings", name = "Chaotic Upgrade Ingredients", comment = "Ingredients for the fourth upgrade tier.", requiresMCRestart = true, requiresSync = true)
	public static String[] chaoticRecipe = new String[] {
			"draconicevolution:wyvern_core",
			"draconicevolution:wyvern_core",
			"draconicevolution:awakened_core",
			"draconicevolution:awakened_core",
			"minecraft:dragon_egg",
			"minecraft:dragon_egg",
			"draconicevolution:chaotic_core"
	};
	/**
	 * None of the default recipes define ingredients using an ItemStack.<br>
	 * Most of the ingredients will be found as Items.<br>
	 * A few of the ingredients are Blocks, and only one ingredient is an Ore Dictionary tag; "gemDiamond".<br>
	 * <br>
	 * You can see where the recipes are defined by looking at {@link com.brandon3055.draconicevolution.items.ToolUpgrade#registerRecipe(String, int)}.<br>
	 * To visit the above link easily in Eclipse, hover over the {@link #postInit()} function either here or below, then click on the link.
	 * From there, simply go down to the 4th option with the arrow pointing inwards towards a document, then click the icon.
	 */
	public static void postInit() {
		List<IFusionRecipe> recipes = FusionRecipeAPI.getRecipes();
		DEFusionCraftingRecipeChanges.logger.log(Level.INFO, "Starting takeover of Draconic Evolution. Prepare thyself :)");
		LogHelper.info("no no no no NOT AGAI-!!!");
		LogHelper.info("Ha! Too easy. Beginning recipe rebalancing and editing some additional recipes, just for fun.  You like fun, right?");
		for (IFusionRecipe oldRecipe : recipes) {
			if (oldRecipe instanceof FusionUpgradeRecipe) {
				LogHelper.dev(oldRecipe.getRecipeOutput(oldRecipe.getRecipeCatalyst()).getItem().getUnlocalizedName() + " was " + oldRecipe.getIngredientEnergyCost());
				FusionRecipeAPI.removeRecipe(oldRecipe);

				IFusionRecipe newRecipe;
				ItemStack output = oldRecipe.getRecipeOutput(oldRecipe.getRecipeCatalyst());
				ItemStack catalyst = oldRecipe.getRecipeCatalyst();
				int tier = oldRecipe.getRecipeTier();
				long rfCost = (long)(energyCost[tier]);
				List ingredients = oldRecipe.getRecipeIngredients();

				FusionUpgradeRecipe oldUpgradeRecipe = (FusionUpgradeRecipe)oldRecipe;
				List newIngredients = new ArrayList();
				if (tier == 0) {
					for (int i = 0; i < basicRecipe.length; i++) {
						String itemName = basicRecipe[i];
						newIngredients.add(Item.getByNameOrId(itemName));
					}
				}
				else if (tier == 1) {
					for (int i = 0; i < wyvernRecipe.length; i++) {
						String itemName = wyvernRecipe[i];
						newIngredients.add(Item.getByNameOrId(itemName));
					}
				}
				else if (tier == 2) {
					for (int i = 0; i < draconicRecipe.length; i++) {
						String itemName = draconicRecipe[i];
						newIngredients.add(Item.getByNameOrId(itemName));
					}
				}
				else if (tier == 3) {
					for (int i = 0; i < chaoticRecipe.length; i++) {
						String itemName = chaoticRecipe[i];
						newIngredients.add(Item.getByNameOrId(itemName));
					}
				}
					newRecipe = new FusionUpgradeRecipe(oldUpgradeRecipe.upgrade, oldUpgradeRecipe.upgradeKey, rfCost, tier, oldUpgradeRecipe.upgradeLevel, newIngredients.toArray());
					
				FusionRecipeAPI.addRecipe(newRecipe);
				if (!FusionRecipeAPI.getRecipes().contains(newRecipe)) {
					LogHelper.warn("One of the recipes in the fusion crafting database cannot be changed.  Attempting to put old recipe back...");
					FusionRecipeAPI.addRecipe(oldRecipe);
					if (!FusionRecipeAPI.getRecipes().contains(oldRecipe)) {
						LogHelper.error("Something went terribly wrong and the fusion crafting recipe database is damaged beyond repair.  Cannot continue.");
						throw new Error("The fusion crafting recipe database was damaged beyond repair when attempting to modify the recipes contained within.");
					}
					LogHelper.warn("The old recipe was successfully placed back into the registry.");
				}
				else LogHelper.dev(newRecipe.getRecipeOutput(newRecipe.getRecipeCatalyst()).getItem().getUnlocalizedName() + " now " + newRecipe.getIngredientEnergyCost());
			}
			else {
				if (!oldRecipe.getRecipeOutput(oldRecipe.getRecipeCatalyst()).getItem().getUnlocalizedName().contains("air"))
					LogHelper.dev("Recipe with catalyst " + oldRecipe.getRecipeOutput(oldRecipe.getRecipeCatalyst()).getItem().getUnlocalizedName() + " is not compatible.  Skipping... ");
			}
		}
		LogHelper.info("Recipe rebalancing complete. Returning control of Draconic Evolution.");
		LogHelper.info("... im done with this. can you addons just leave me alone for once?");
		DEFusionCraftingRecipeChanges.logger.log(Level.INFO, "Nope :)");
		LogHelper.info("figures...");
	}
}
