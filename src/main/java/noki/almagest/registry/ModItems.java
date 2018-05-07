package noki.almagest.registry;

import net.minecraft.item.Item;
import noki.almagest.item.ItemAlmagest;
import noki.almagest.item.ItemFlyingFish;
import noki.almagest.item.ItemHoney;
import noki.almagest.item.ItemMissingStar;
import noki.almagest.item.ItemOrigamiCrane;
import noki.almagest.item.ItemPhoenixFeather;
import noki.almagest.item.ItemPlanisphere;
import noki.almagest.item.ItemRainbowFeather;
import noki.almagest.item.ItemStardust;
import noki.almagest.item.ItemTsuchinokoSkin;


/**********
 * @class ModItems
 *
 * @description このmodのアイテムを登録し、インスタンスを保持しておくクラスです。
 * 
 */
public class ModItems {
	
	
	//******************************//
	// define member variables.
	//******************************//
	public static Item MISSING_STAR;
	public static final String MISSING_STAR_name = "missing_star";
	
	public static Item PLANISPHERE;
	public static final String PLANISPHERE_name ="planisphere";
	
	public static Item ALMAGEST;
	public static final String ALMAGEST_name = "almagest";
	
	public static Item STARDUST;
	public static final String STARDUST_name = "stardust";
	
	public static Item HONEY;
	public static final String HONEY_name = "honey";
	
	public static Item TSUCHINOKO_SKIN;
	public static final String TSUCHINOKO_SKIN_name = "tsuchinoko_skin";
	
	public static Item FLYING_FISH;
	private static final String FLYING_FISH_name = "flying_fish";
	
	public static Item PHOENIX_FEATHER;
	private static final String PHOENIX_FEATHER_name = "phoenix_feather";
	
	public static Item RAINBOW_FEATHER;
	private static final String RAINBOW_FEATHER_name = "rainbow_feather";
	
	public static Item WHITE_FEATHER;
	private static final String WHITE_FEATHER_name = "white_feather";
	
	public static Item BLACK_FEATHER;
	private static final String BLACK_FEATHER_name = "black_feather";
	
	public static Item ORIGAMI_CRANE;
	private static final String ORIGAMI_CRANE_name = "origami_crane";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public static void register() {
		
		MISSING_STAR = RegistryHelper.registerItem(new ItemMissingStar(), MISSING_STAR_name);
		PLANISPHERE = RegistryHelper.registerItem(new ItemPlanisphere(), PLANISPHERE_name);
		ALMAGEST = RegistryHelper.registerItem(new ItemAlmagest(), ALMAGEST_name);
		STARDUST = RegistryHelper.registerItem(new ItemStardust(), STARDUST_name);
		HONEY = RegistryHelper.registerItem(new ItemHoney(), HONEY_name);
		TSUCHINOKO_SKIN = RegistryHelper.registerItem(new ItemTsuchinokoSkin(), TSUCHINOKO_SKIN_name);
		FLYING_FISH = RegistryHelper.registerItem(new ItemFlyingFish(), FLYING_FISH_name);
		PHOENIX_FEATHER = RegistryHelper.registerItem(new ItemPhoenixFeather(), PHOENIX_FEATHER_name);
		ORIGAMI_CRANE = RegistryHelper.registerItem(new ItemOrigamiCrane(), ORIGAMI_CRANE_name);
		RAINBOW_FEATHER = RegistryHelper.registerItem(new ItemRainbowFeather(), RAINBOW_FEATHER_name);
		
	}

}
