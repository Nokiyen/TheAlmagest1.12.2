package noki.almagest.event;

import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;
import noki.almagest.AlmagestData;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.registry.ModBlocks;

public class EventConstellation {
		
/*	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent event) {
		
		// プレイヤーダメージによる死亡のみ。
		if(event.source.getDamageType().equals("player") == false) {
			return;
		}
		
		EntityLivingBase entity = event.entityLiving;
		
		// 指定のモブのみ。
		MobType type = MobType.getType(entity);
		if(type == null) {
			return;
		}
		
		Random rand = entity.getRNG();
		int prov = 0;
		
		// モブごとに指定の星座をドロップ。
		Constellation dropConst = null;
		switch(type) {
			case Sheep:
				boolean sheared = ((EntitySheep)entity).getSheared();
				prov = rand.nextInt(20);
				if(sheared == true && prov < 10) {
					dropConst = Constellation.Cap;	// やぎ座
				}
				else if(prov == 0){
					dropConst = Constellation.Cap;
				}
				else if(prov < 5) {
					dropConst = Constellation.Ari;	// おひつじ座
				}
				break;
			case Cow:
				prov = rand.nextInt(20);
				if(prov == 0) {
					dropConst = Constellation.Boo;	// うしかい座
				}
				else if(prov < 5) {
					dropConst = Constellation.Tau;	// おうし座
				}
				break;
			case Mooshroom:
				dropConst = filterConst(Constellation.Boo, rand, 4);	// うしかい座
				break;
			case Pig:
				dropConst = filterConst(Constellation.Cyg, rand, 4);	// はくちょう座
				break;
			case Chicken:
				prov = rand.nextInt(16);
				if(prov == 0) {
					dropConst = Constellation.Aps;	// ふうちょう座
				}
				else if(prov == 1) {
					dropConst = Constellation.Tuc;	// きょしちょう座
				}
				else if(prov == 2) {
					dropConst = Constellation.Pav;	// くじゃく座
				}
				else if(prov == 3) {
					dropConst = Constellation.Gru;	//つる座
				}
				break;
			case Horse:
				int variant = ((EntityHorse)entity).getHorseVariant() & 255;
				prov = rand.nextInt(20);
				if(variant == 0 && prov < 5){
					dropConst = Constellation.Mon;	// いっかくじゅう座
				}
				else if(prov == 0) {
					dropConst = Constellation.Mon;
				}
				else if(prov < 5) {
					dropConst = Constellation.Equ;	// こうま座
				}
				break;
			case Rabbit:
				dropConst = filterConst(Constellation.Lep, rand, 4);
				break;
			case Wolf:
				boolean tamed = ((EntityWolf)entity).isTamed();
				prov = rand.nextInt(8);
				if(tamed == true) {
					if(prov == 0) {
						dropConst = Constellation.CMa;
					}
					else if(prov == 1) {
						dropConst = Constellation.CMi;
					}
				}
				else {
					if(prov == 0) {
						dropConst = Constellation.Lup;
					}
					else if (prov == 1) {
						dropConst = Constellation.CVn;
					}
				}
				break;
			case Ocelot:
				dropConst = filterConst(Constellation.Lyn, rand, 4);
				break;
			case Bat:
				dropConst = filterConst(Constellation.Crv, rand, 4);
				break;
			case Squid:
				dropConst = filterConst(Constellation.Del, rand, 4);
				break;
			case Zombie:
				dropConst = filterConst(Constellation.Ara, rand, 4);
				break;
			case Skeleton:
				if(((EntitySkeleton)entity).getSkeletonType() == 1) {
					dropConst = filterConst(Constellation.Sct, rand, 4);
				}
				else {
					dropConst = filterConst(Constellation.Sgr, rand, 4);
				}
				break;
			case Spider:
				dropConst = filterConst(Constellation.Sco, rand, 4);
				break;
			case Creeper:
				dropConst = filterConst(Constellation.Ori, rand, 4);
				break;
			case Slime:
				dropConst = filterConst(Constellation.Cha, rand, 4);
				break;
			case Silverfish:
				dropConst = filterConst(Constellation.Mus, rand, 4);
				break;
			case CaveSpider:
				dropConst = filterConst(Constellation.Ser, rand, 4);
				break;
			case Enderman:
				dropConst = filterConst(Constellation.UMa, rand, 4);
				break;
			case Endermite:
				dropConst = filterConst(Constellation.UMi, rand, 4);
				break;
			case Witch:
				dropConst = filterConst(Constellation.Oph, rand, 4);
				break;
			case PigMan:
				dropConst = filterConst(Constellation.Cen, rand, 4);
				break;
			case Ghast:
				dropConst = filterConst(Constellation.Col, rand, 4);
				break;
			case MagmaCube:
				dropConst = filterConst(Constellation.Lac, rand, 4);
				break;
			case Blaze:
				dropConst = filterConst(Constellation.Phe, rand, 4);
				break;
			case Guardian:
				if(((EntityGuardian)entity).isElder() == false) {
					dropConst = filterConst(Constellation.Hya, rand, 4);
				}
				else {
					dropConst = Constellation.Hyi;					
				}
				break;
			case EnderDragon:
				dropConst = Constellation.Dra;
				break;
			case Wither:
				dropConst = Constellation.Aql;
				break;
			case IronGolem:
				dropConst = filterConst(Constellation.Her, rand, 4);
				break;
			case SnowGolem:
				dropConst = filterConst(Constellation.Com, rand, 4);
				break;
			default:
				break;
		}
		
		if(dropConst == null) {
			return;
		}
		
		event.drops.add(new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ,
				HelperConstellation.getIncompleteConstStack(dropConst, 1)));
		
	}
	
	@SubscribeEvent
	public void onEntityInteract(EntityInteractEvent event) {
		
		if(event.entityPlayer.worldObj.isRemote == true) {
			return;
		}
		
		if(!(event.target instanceof EntityAnimal) && !(event.target instanceof EntityIronGolem)) {
			return;
		}
		EntityLivingBase entity = (EntityLivingBase)event.target;
		
		MobType type = MobType.getType(entity);
		if(type == null) {
			return;
		}
		
		ItemStack heldItem = event.entityPlayer.getHeldItem();
		if(entity instanceof EntityAnimal) {
			if(heldItem == null) {
				return;
			}
			EntityAnimal animal = (EntityAnimal)entity;
			if(animal.isBreedingItem(heldItem) == false || animal.isInLove() == true || animal.isChild() == true) {
				return;
			}
		}
		else {
			if(heldItem.getItem() != Item.getItemFromBlock(Blocks.red_flower)
					|| heldItem.getItemDamage() != BlockFlower.EnumFlowerType.POPPY.getMeta()) {
				return;
			}
		}
		
		Random rand = entity.getRNG();
		int prov = 0;
		
		Constellation dropConst = null;
		switch(type) {
			case Sheep:
				boolean sheared = ((EntitySheep)entity).getSheared();
				prov = rand.nextInt(20);
				if(sheared == true && prov < 10) {
					dropConst = Constellation.Cap;	// やぎ座
				}
				else if(prov == 0){
					dropConst = Constellation.Cap;
				}
				else if(prov < 10) {
					dropConst = Constellation.Ari;	// おひつじ座
				}
				break;
			case Cow:
				prov = rand.nextInt(20);
				if(prov == 0) {
					dropConst = Constellation.Boo;	// うしかい座
				}
				else if(prov < 10) {
					dropConst = Constellation.Tau;	// おうし座
				}
				break;
			case Mooshroom:
				dropConst = filterConst(Constellation.Boo, rand, 2);	// うしかい座
				break;
			case Pig:
				dropConst = filterConst(Constellation.Cyg, rand, 2);	// はくちょう座
				break;
			case Chicken:
				prov = rand.nextInt(8);
				if(prov == 0) {
					dropConst = Constellation.Aps;	// ふうちょう座
				}
				else if(prov == 1) {
					dropConst = Constellation.Tuc;	// きょしちょう座
				}
				else if(prov == 2) {
					dropConst = Constellation.Pav;	// くじゃく座
				}
				else if(prov == 3) {
					dropConst = Constellation.Gru;	//つる座
				}
				break;
			case Horse:
				int variant = ((EntityHorse)entity).getHorseVariant() & 255;
				prov = rand.nextInt(20);
				if(variant == 0 && prov < 10){
					dropConst = Constellation.Mon;	// いっかくじゅう座
				}
				else if(prov == 0) {
					dropConst = Constellation.Mon;
				}
				else if(prov < 10) {
					dropConst = Constellation.Equ;	// こうま座
				}
				break;
			case Rabbit:
				dropConst = filterConst(Constellation.Lep, rand, 2);
				break;
			case Wolf:
				boolean tamed = ((EntityWolf)entity).isTamed();
				prov = rand.nextInt(4);
				if(tamed == true) {
					if(prov == 0) {
						dropConst = Constellation.CMa;
					}
					else if(prov == 1) {
						dropConst = Constellation.CMi;
					}
				}
				else {
					if(prov == 0) {
						dropConst = Constellation.Lup;
					}
					else if (prov == 1) {
						dropConst = Constellation.CVn;
					}
				}
				break;
			case Ocelot:
				dropConst = filterConst(Constellation.Lyn, rand, 2);
				break;
			case IronGolem:
				EntityPlayer player = event.entityPlayer;
				if(!player.capabilities.isCreativeMode) {
					--heldItem.stackSize;
					if(heldItem.stackSize <= 0) {
						player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
					}
				}
				if(rand.nextInt(4) == 0) {
					dropConst = Constellation.Her;
				}
				break;
			default:
				break;
		}
		
		if(dropConst == null) {
			return;
		}
		
		EntityItem dropItem = new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ,
				HelperConstellation.getIncompleteConstStack(dropConst, 1));
		entity.worldObj.spawnEntityInWorld(dropItem);
				
	}*/
	
	@SubscribeEvent
	public void onPickupItem(EntityItemPickupEvent event) {
		
		if(event.getItem() == null || event.getEntityPlayer() == null) {
			return;
		}
		if(event.getItem().getItem().getItem() != Item.getItemFromBlock(ModBlocks.CONSTELLATION_BLOCK)) {
			return;
		}
		
		Constellation constellation = Constellation.getConstFromNumber(HelperConstellation.getConstNumber(event.getItem().getItem()));
		if(AlmagestCore.savedDataManager.getFlagData().getConstellation(constellation).isObtained() == false) {
			AlmagestCore.savedDataManager.getFlagData().setConstellationObtained(constellation);
		}
		
	}
	
	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent event) {
		
		EntityLivingBase entity = event.getEntityLiving();
		if(entity == null) {
			return;
		}
		
		if(entity instanceof EntityParrot) {
			if(entity.world.rand.nextDouble() < 0.3D) {
				event.getDrops().add(new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, HelperConstellation.getConstStack(82, 1)));
			}
		}
		
	}
	
	@SubscribeEvent
	public void onAnimalTame(AnimalTameEvent event) {
		
		EntityAnimal entity = event.getAnimal();
		if(entity == null) {
			return;
		}
		
		if(entity instanceof EntityParrot) {
			entity.world.spawnEntity(new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, HelperConstellation.getConstStack(82, 1)));
		}
		
	}

}
