package noki.almagest.item;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.attribute.ItemWithAttribute;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithRecipe;
import noki.almagest.registry.ModBlocks;
import noki.almagest.registry.ModItems;


/**********
 * @class ItemStardustItemMissingStar
 *
 * @description 星ブロックを砕くと手に入るアイテムです。何に使うんだ。
 * 
 */
public class ItemStardust extends ItemWithAttribute implements IWithRecipe {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemStardust() {
		
		this.setAttributeLevel(EStarAttribute.STAR, 5);
		
	}
	
	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(
				new StarRecipe(new ItemStack(this, 4)).setStack(new ItemStack(ModBlocks.ATLAS_STAR)));
		
	}

}
