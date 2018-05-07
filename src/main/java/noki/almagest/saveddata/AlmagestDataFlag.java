package noki.almagest.saveddata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
import noki.almagest.AlmagestCore;
import noki.almagest.ability.StarAbilityCreator.StarAbility;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.saveddata.gamedata.EMemoData;
import noki.almagest.saveddata.gamedata.GameData;
import noki.almagest.saveddata.gamedata.GameDataAbility;
import noki.almagest.saveddata.gamedata.GameDataAbility2;
import noki.almagest.saveddata.gamedata.GameDataBlock;
import noki.almagest.saveddata.gamedata.GameDataConstellation;
import noki.almagest.saveddata.gamedata.GameDataItem;
import noki.almagest.saveddata.gamedata.GameDataMemo;
import noki.almagest.saveddata.gamedata.GameDataMob;


/**********
 * @class AlmagestSavedData
 *
 * @description 様々なフラグについて管理するクラスです。ストーリー進行、アイテムや星座の取得状況など。
 * @description_en
 */
public class AlmagestDataFlag implements IAlmagestData {
	
	//******************************//
	// define member variables.
	//******************************//
	private HashMap<DataCategory, HashMap<String, GameData>> gameDataMap = new HashMap<DataCategory, HashMap<String,GameData>>();
	private WorldSavedData almagestData;
		
	
	//******************************//
	// define member methods.
	//******************************//
	public AlmagestDataFlag() {
		
//		super(name);
		
		for(DataCategory each: DataCategory.values()) {
			this.gameDataMap.put(each, new HashMap<String, GameData>());
		}
		
	}
	
	public HashMap<DataCategory, HashMap<String, GameData>> getMap() {
		
		return this.gameDataMap;
		
	}
	
	public void registerData(DataCategory category, String key, GameData data) {
		
		this.gameDataMap.get(category).put(key, data);
		
	}
	
	public void registerBlock(ItemStack stack, boolean hasRecipe) {
		
		this.gameDataMap.get(DataCategory.BLOCK).put(stack.getUnlocalizedName(), new GameDataBlock(stack, hasRecipe));
		
	}
	
	public void registerItem(ItemStack stack, boolean hasRecipe) {
		
		this.gameDataMap.get(DataCategory.ITEM).put(stack.getUnlocalizedName(), new GameDataItem(stack, hasRecipe));
		
	}
	
	public void registerMob(EntityLiving entity) {
		
		this.gameDataMap.get(DataCategory.MOB).put(entity.getName(), new GameDataMob(entity));
		
	}
	
	public void registerAbility(StarAbility ability) {
		
		this.gameDataMap.get(DataCategory.ABILITY).put(ability.getName(), new GameDataAbility(ability));
		
	}
	
	public void registerAbility2(noki.almagest.ability.StarAbility ability, int level) {
		
		this.gameDataMap.get(DataCategory.ABILITY).put(ability.getName(level), new GameDataAbility2(ability, level));
		
	}
	
	public void registerConstellation(Constellation constellation) {
		
		this.gameDataMap.get(DataCategory.CONSTELLATION).put(constellation.getName(), new GameDataConstellation(constellation));
		
	}
	
	public void registerMemo(EMemoData memo) {
		
		this.gameDataMap.get(DataCategory.MEMO).put(memo.getDisplay(), new GameDataMemo(memo));
	}
	
	public GameDataConstellation getConstellation(Constellation constellation) {
		
		return (GameDataConstellation)this.gameDataMap.get(DataCategory.CONSTELLATION).get(constellation.getName());
		
	}
	
	public GameData getNextData(GameData data, DataCategory category, boolean isCreative) {
		
		HashMap<String, GameData> categorySet = this.gameDataMap.get(category);
		java.util.List<String> mapKeys = Arrays.asList(categorySet.keySet().toArray(new String[categorySet.size()]));
		Collections.sort(mapKeys, String.CASE_INSENSITIVE_ORDER);
		
		boolean found = false;
		for(String eachKey: mapKeys) {
			GameData eachData = categorySet.get(eachKey);
			if(found && (isCreative || eachData.isObtained())) {
				return eachData;
			}
			if(eachData == data) {
				found = true;
			}
		}
		
		return null;
		
	}
	
	public GameData getPrevData(GameData data, DataCategory category, boolean isCreative) {
		
		HashMap<String, GameData> categorySet = this.gameDataMap.get(category);
		java.util.List<String> mapKeys = Arrays.asList(categorySet.keySet().toArray(new String[categorySet.size()]));
		Collections.sort(mapKeys, String.CASE_INSENSITIVE_ORDER);
		
		GameData prevData = null;
		for(String eachKey: mapKeys) {
			GameData eachData = categorySet.get(eachKey);
			if(eachData == data) {
				return prevData;
			}
			if(isCreative || eachData.isObtained()) {
				prevData = eachData;
			}
		}
		
		return null;
		
	}
	
	public int getMaxPage(DataCategory category, int itemNumber) {
		
		HashMap<String, GameData> map = this.gameDataMap.get(category);
		
		return (int)Math.ceil((double)map.size()/(double)itemNumber);
		
	}
	
	public void setConstellationObtained(Constellation constellation) {
		
		this.setObtained(DataCategory.CONSTELLATION, constellation.getName());
		
	}
	
	public void setConstellationPresented(Constellation constellation) {
		
		this.getConstellation(constellation).setPresented(true);
		this.almagestData.markDirty();
		
	}
	
	public boolean isCostellationPresented(Constellation constellation) {
		
		return this.getConstellation(constellation).isPresented();
		
	}
	
	public void setObtained(DataCategory category, String label) {
		
		this.gameDataMap.get(category).get(label).setObtained(true);
		this.almagestData.markDirty();
		
	}
	
	public ArrayList<GameData> getDataSet(DataCategory category, int start, int length) {
		
		ArrayList<GameData> dataSet = new ArrayList<GameData>();
		
		HashMap<String, GameData> categorySet = this.gameDataMap.get(category);
		int size = categorySet.size();
//		AlmagestCore.log2("at saved data, category set size is %s.", size);
		
		java.util.List<String> mapkey = Arrays.asList(categorySet.keySet().toArray(new String[categorySet.size()]));
		Collections.sort(mapkey, String.CASE_INSENSITIVE_ORDER);
		
		if(size < start) {
			AlmagestCore.log2("size < start. hoo!");
			return dataSet;
		}
		int count = 1;
		for(String each: mapkey) {
			if(start+length <= count) {
				break;
			}
			if(start <= count) {
				dataSet.add(categorySet.get(each));
			}
			count++;
		}
		
		return dataSet;
		
	}
	
	public int countObtainedConstellation() {
		
		int count = 0;
		HashMap<String, GameData> constelationData = this.gameDataMap.get(DataCategory.CONSTELLATION);
		for(GameData each: constelationData.values()) {
			if(each.isObtained()) {
				count++;
			}
		}
		
		return count;
		
	}
	
	public int countPresentedConstellation() {
		
		int count = 0;
		HashMap<String, GameData> constelationData = this.gameDataMap.get(DataCategory.CONSTELLATION);
		for(GameData each: constelationData.values()) {
			if(((GameDataConstellation)each).isPresented()) {
				count++;
			}
		}
		
		return count;
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		for(DataCategory each: DataCategory.values()) {
			NBTTagCompound categoryNbt = nbt.getCompoundTag(each.toString());
			
			HashMap<String, GameData> eachData = this.gameDataMap.get(each);
			for(GameData eachGameData: eachData.values()) {
				eachGameData.readFromNbt(categoryNbt.getCompoundTag(eachGameData.getName().toString()));
			}
		}

	}
	
	@Override
	public NBTTagCompound createNBT() {
		
		NBTTagCompound gameDataNbt = new NBTTagCompound();
		
		for(DataCategory each: DataCategory.values()) {
			HashMap<String, GameData> eachData = this.gameDataMap.get(each);
			NBTTagCompound categoryNbt = new NBTTagCompound();
			for(GameData eachGameData: eachData.values()) {
				NBTTagCompound eachNbt = new NBTTagCompound();
				eachGameData.writeToNbt(eachNbt);
				categoryNbt.setTag(eachGameData.getName().toString(), eachNbt);
			}
			gameDataNbt.setTag(each.toString(), categoryNbt);
		}
		
		return gameDataNbt;
		
	}
	
	@Override
	public void setSavedData(WorldSavedData data) {
		
		this.almagestData = data;
		
	}
	
}
