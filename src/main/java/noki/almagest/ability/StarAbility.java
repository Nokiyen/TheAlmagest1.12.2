package noki.almagest.ability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import noki.almagest.helper.HelperNBTStack;


/**********
 * @class StarAbility
 *
 * @description 「星のちから」の継承用クラスです。
 */
public class StarAbility {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String NBT_abilities = "abilities";
	protected String name = "";
	protected int id = 0;
	protected int maxLevel = 1;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public String getName(int level) {
		
		return "almagest.ability."+this.id+"."+MathHelper.clamp(level, 1, this.getMaxLevel())+".name";
		
	}
	
	public StarAbility setId(int id) {
		
		this.id = id;
		return this;
		
	}
	
	public int getId() {
		
		return this.id;
		
	}
	
	public void setMaxLevel(int level) {
		
		this.maxLevel = level;
	}
	
	public int getMaxLevel() {
		
		return this.maxLevel;
		
	}
	
	public int[] getAbilityLevels(ItemStack stack) {
		
		NBTTagCompound abilities = new HelperNBTStack(stack).getTag(NBT_abilities);
		return abilities.getIntArray(String.valueOf(this.id));
		
	}

}
