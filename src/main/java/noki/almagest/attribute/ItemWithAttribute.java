package noki.almagest.attribute;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


/**********
 * @class ItemWithAttribute
 *
 * @description attribute付きItemクラスです。基本これを継承します。
 */
public class ItemWithAttribute extends Item implements IWithAttribute {
	
	
	//******************************//
	// define member variables.
	//******************************//
	protected Map<EStarAttribute, Integer> attributes = new HashMap<EStarAttribute, Integer>();
	
	
	//******************************//
	// define member methods.
	//******************************//
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
