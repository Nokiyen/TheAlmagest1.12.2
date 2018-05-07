package noki.almagest.saveddata.gamedata;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;


public class GameDataMob extends GameData {
	
	private EntityLiving entity;
	
	public GameDataMob(EntityLiving entity) {
		
		this.entity = entity;
		this.name = new ResourceLocation(this.entity.getCachedUniqueIdString());
		
	}
	
	public EntityLiving getEntity() {
		
		return this.entity;
		
	}
	
	@Override
	public String getDisplay() {
		
		return this.entity.getName();
		
	}

}
