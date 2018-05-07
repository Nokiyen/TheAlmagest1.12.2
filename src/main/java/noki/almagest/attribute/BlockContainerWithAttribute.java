package noki.almagest.attribute;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


/**********
 * @class BlockContainerWithAttribute
 *
 * @description attribute付きBlockContainerクラスです。
 */
public class BlockContainerWithAttribute extends BlockContainer implements IWithAttribute {
	
	
	//******************************//
	// define member variables.
	//******************************//
	protected Map<EStarAttribute, Integer> attributes = new HashMap<EStarAttribute, Integer>();
	
	
	//******************************//
	// define member methods.
	//******************************//
	public BlockContainerWithAttribute(Material material) {
		
		super(material);
		
	}
	
	public BlockContainerWithAttribute(Material material, MapColor color) {
		
		super(material, color);
		
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		
		return null;
		
	}
	
	@Override
	public void setAttributeLevel(EStarAttribute attribute, int level) {
		
		this.attributes.put(attribute, level);
		
	}
	
	@Override
	public int getAttributeLevel(EStarAttribute attribute, ItemStack stack) {
		
		return this.modifyAttribute(attribute, stack);
		
	}
	
	@Override
	public Map<EStarAttribute, Integer> getAttributes(ItemStack stack) {
		
		return this.attributes;
		
	}
	
	@Override
	public int modifyAttribute(EStarAttribute attribute, ItemStack stack) {
		
		Integer level = this.attributes.get(attribute);
		if(level != null) {
			return level;
		}
		return 0;
		
	}
	
}
