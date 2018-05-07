package noki.almagest.attribute;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;


/**********
 * @class ItemFoodWithAttribute
 *
 * @description 多重継承できないのでしょうがない。ItemFoodにはこれを継承します。
 */
public class ItemFoodWithAttribute extends ItemFood implements IWithAttribute {
	
	
	//******************************//
	// define member variables.
	//******************************//
	protected Map<EStarAttribute, Integer> attributes = new HashMap<EStarAttribute, Integer>();
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemFoodWithAttribute(int amount, float saturation, boolean isWolfFood) {
		
		super(amount, saturation, isWolfFood);
		
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
