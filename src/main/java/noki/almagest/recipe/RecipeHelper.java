package noki.almagest.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import noki.almagest.ModInfo;
import noki.almagest.attribute.EStarAttribute;


public class RecipeHelper {
	
	public static void register() {
		
		register(new StarRecipe(new ItemStack(Blocks.PLANKS, 5)).setStack(new ItemStack(Blocks.LOG, 1)).setAttribute(EStarAttribute.TOOL, 20));
		register(new StarRecipe(new ItemStack(Blocks.PLANKS, 5)).setStack(new ItemStack(Blocks.LOG2, 1)).setAttribute(EStarAttribute.TOOL, 20));
		register(new StarRecipe(new ItemStack(Items.STICK, 5)).setStack(new ItemStack(Blocks.PLANKS, 2)).setAttribute(EStarAttribute.TOOL, 20));
		register(new StarRecipe(new ItemStack(Items.IRON_INGOT, 2)).setStack(new ItemStack(Blocks.IRON_ORE, 1)).setAttribute(EStarAttribute.FUEL, 20).setAttribute(EStarAttribute.TOOL, 20));
		register(new StarRecipe(new ItemStack(Items.GOLD_INGOT, 2)).setStack(new ItemStack(Blocks.GOLD_ORE, 1)).setAttribute(EStarAttribute.FUEL, 20).setAttribute(EStarAttribute.TOOL, 20));
		register(new StarRecipe(new ItemStack(Items.DIAMOND, 2)).setStack(new ItemStack(Blocks.DIAMOND_ORE, 1)).setAttribute(EStarAttribute.FUEL, 20).setAttribute(EStarAttribute.TOOL, 20));
		register(new StarRecipe(new ItemStack(Blocks.COBBLESTONE, 2)).setStack(new ItemStack(Blocks.STONE, 1)).setAttribute(EStarAttribute.TOOL, 20));
		register(new StarRecipe(new ItemStack(Items.STRING, 5)).setStack(new ItemStack(Blocks.WOOL, 1)).setStack(new ItemStack(Items.SHEARS)));
		
	}
	
	public static void register(StarRecipe recipe) {
		
		ForgeRegistries.RECIPES.register(
				recipe.setRegistryName(new ResourceLocation(ModInfo.ID.toLowerCase(), recipe.getRecipeOutput().getUnlocalizedName()+"_almagest")));
		
	}

}
