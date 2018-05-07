package noki.almagest.ability;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import noki.almagest.event.post.MemoryEvent;
import noki.almagest.helper.HelperNBTStack;


/**********
 * @class StarPropertyCreator
 *
 * @description star propertyである、memory, lineについて処理するクラスです。
 * @description_en
 */
public class StarPropertyCreator {
	
	//******************************//
	// define member variables.
	//******************************//
	public static final String memoryName = "memory";

	
	//******************************//
	// define member methods.
	//******************************//
	public static ItemStack addLines(ItemStack stack, ItemStarLine... line) {
		
		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		
		for(ItemStarLine each : line) {
			nbtStack.setBoolean(each.getName(), true);
		}
		
		return nbtStack.getStack();
		
	}
	
	public static ArrayList<ItemStarLine> getLines(ItemStack stack) {

		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		ArrayList<ItemStarLine> list = new ArrayList<ItemStarLine>();

		for(ItemStarLine each : ItemStarLine.values()) {
			if(nbtStack.getBoolean(each.getName())) {
				list.add(each);
			}
		}
		
		return list;
		
	}
	
	public static boolean hasLine(ItemStack stack) {
		
		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		for(ItemStarLine each : ItemStarLine.values()) {
			if(nbtStack.getBoolean(each.getName())) {
				return true;
			}
		}
		return false;
		
	}
	
	public static ItemStack setMemory(ItemStack stack, int memory) {
		
		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		return nbtStack.setInteger(memoryName, memory).getStack();
		
	}
	
	public static int getMemory(ItemStack stack) {
		
		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		int memory = nbtStack.getInteger(memoryName);
		return MemoryEvent.postEvent(stack, memory);
		
	}
	
	public static int getMagnitude(ItemStack stack) {
		
		double division = Math.floor(getMemory(stack) / 30.0D);
		int magnitude = 6 - (int)Math.min(5, division);
		
		return magnitude;
		
	}
	
	public static boolean isMagnitude(ItemStack stack, int magnitude) {
		
		if(getMagnitude(stack) <= magnitude) {
			return true;
		}
		return false;
		
	}
	
	public static ItemStack setProperty(ItemStack stack, int memory, ItemStarLine... lines) {
		
		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		
		for(ItemStarLine each : lines) {
			nbtStack.setBoolean(each.getName(), true);
		}
		nbtStack.setInteger(memoryName, memory);
		
		return nbtStack.getStack();
//		return addLines(setMemory(stack, memory), lines);
		
	}
	
	public enum ItemStarLine {
		
		TOP("top"),
		BOTTOM("bottom"),
		LEFT("left"),
		RIGHT("right");
		
		private String name;
		
		private ItemStarLine(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
		
	}

}
