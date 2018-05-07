package noki.almagest.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noki.almagest.AlmagestCore;
import noki.almagest.AlmagestData;
import noki.almagest.attribute.AttributeHelper;
import noki.almagest.attribute.BlockWithAttribute;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithRecipe;


/**********
 * @class BlockBookrest
 *
 * @description
 */
public class BlockBookrest extends BlockWithAttribute implements IWithRecipe {

	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public BlockBookrest() {
		
		super(Material.WOOD);
		this.setHardness(2.5F);
		this.setSoundType(SoundType.WOOD);
		
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumHand hand, EnumFacing facing, float f1, float f2, float f3) {
		
		player.openGui(AlmagestCore.instance, AlmagestData.guiID_bookrest, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
		
	}

	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(
				new StarRecipe(new ItemStack(this)) {
					@Override
					public boolean matches(InventoryCrafting inv, World worldIn) {
						int plankCount = 0;
						int attributeSum = 0;
						for(int i=0; i<inv.getSizeInventory(); i++) {
							ItemStack selectedStack = inv.getStackInSlot(i);
							if(selectedStack.isEmpty()) {
								continue;
							}

							boolean plankFlag = false;
							boolean attributeFlag = false;
							
							Block block = Block.getBlockFromItem(selectedStack.getItem());
							if(this.isPlank(block)) {
								plankCount++;
								plankFlag = true;
							}
							int level = AttributeHelper.getAttrributeLevel(selectedStack, EStarAttribute.STAR);
							attributeSum += level;
							if(level != 0) {
								attributeFlag = true;
							}
							
							if(plankFlag == false && attributeFlag == false) {
								return false;
							}
						}
						
						if(plankCount == 2 && attributeSum >= 10) {
							return true;
						}
						return false;
					}
					
					private boolean isPlank(Block block) {
						if(block == Blocks.PLANKS) {
							return true;
						}
						return false;
					}
				}
		);
		
	}

}
