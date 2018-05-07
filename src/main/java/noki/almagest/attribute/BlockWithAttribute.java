package noki.almagest.attribute;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;


/**********
 * @class BlockWithAttribute
 *
 * @description attribute付きBlockクラスです。
 */
public class BlockWithAttribute extends Block implements IWithAttribute {
	
	
	//******************************//
	// define member variables.
	//******************************//
	protected Map<EStarAttribute, Integer> attributes = new HashMap<EStarAttribute, Integer>();
	
	
	//******************************//
	// define member methods.
	//******************************//
	public BlockWithAttribute(Material material) {
		
		super(material);
		
	}
	
	public BlockWithAttribute(Material material, MapColor color) {
		
		super(material, color);
		
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
