package noki.almagest.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;
import noki.almagest.saveddata.AlmagestDataBlock;
import noki.almagest.saveddata.AlmagestDataChunk;


/**********
 * @class EventProperty
 *
 * @description propertyまわりのイベントです。
 * @description_en
 */
public class EventProperty {
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	//ドロップ時にpropertyをつける。
	@SubscribeEvent
	public void onHarvestDrop(HarvestDropsEvent event) {
		
		AlmagestDataBlock blockData = AlmagestCore.savedDataManager.getBlockData();
		AlmagestDataChunk chunkData = AlmagestCore.savedDataManager.getChunkData();
		
		//a bit loose check.
		for(ItemStack eachStack: event.getDrops()) {
			if(blockData.isBlockPlacedAt(event.getWorld(), event.getPos())) {
				blockData.addProperty(event.getWorld(), event.getPos(), eachStack);
			}
			else {
				chunkData.addProperty(event.getWorld(), event.getPos().getX()>>4, event.getPos().getZ()>>4, eachStack);
			}
		}
		
	}
	
	//プレイヤーが設置したブロックの位置を記憶する。
	@SubscribeEvent
	public void onBlockPlaced(PlaceEvent event) {
		
		AlmagestCore.savedDataManager.getBlockData().blockPlaced(event.getWorld(), event.getPos(), event.getPlayer().getHeldItem(event.getHand()));
//		AlmagestCore.log2("%s/%s/%s.", event.getPos().getX(), event.getPos().getZ(), event.getPos().getZ());
		
	}

}
