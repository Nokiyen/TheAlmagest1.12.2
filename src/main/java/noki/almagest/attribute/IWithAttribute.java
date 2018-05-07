package noki.almagest.attribute;

import java.util.Map;
import net.minecraft.item.ItemStack;


/**********
 * @class IWithAttribute
 *
 * @description 属性付きのブロック・アイテムに実装するinterfaceです。このmodのアイテムにはすべて実装させます。
 */
public interface IWithAttribute {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	abstract void setAttributeLevel(EStarAttribute attribute, int level);
	abstract int getAttributeLevel(EStarAttribute attribute, ItemStack stack);
	abstract Map<EStarAttribute, Integer> getAttributes(ItemStack stack);
	abstract int modifyAttribute(EStarAttribute attribute, ItemStack stack);

}
