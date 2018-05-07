package noki.almagest.gui;

import java.io.IOException;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import noki.almagest.ModInfo;
import noki.almagest.packet.PacketHandler;
import noki.almagest.packet.PacketUpdateBookrest;


/**********
 * @class GuiContainerBookrest
 *
 * @description
 */
public class GuiContainerBookrest extends GuiContainer {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/gui/bookrest.png");
	
	private int currentTopAbility;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public GuiContainerBookrest(EntityPlayer player, World world, int x, int y, int z) {
		
		super(new ContainerBookrest(player.inventory, world, new BlockPos(x, y, z)));
		this.xSize = 256;
		this.ySize = 210;
		
	}
	
	/*GUIの文字等の描画処理*/
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
		
		ContainerBookrest container = (ContainerBookrest)this.inventorySlots;
		
		this.mc.renderEngine.bindTexture(texture);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		for(int i=0; i<12; i++) {
			if(container.getLineState()[i] == 2) {
				switch(i) {
					case 0:
					case 1:
						this.drawTexturedModalRect(27+i*28, 32, 0, 241, 11, 4);
						break;
					case 5:
					case 6:
						this.drawTexturedModalRect(27+(i-5)*28, 60, 0, 241, 11, 4);
						break;
					case 10:
					case 11:
						this.drawTexturedModalRect(27+(i-10)*28, 89, 0, 241, 11, 4);
						break;
					case 2:
					case 3:
					case 4:
						this.drawTexturedModalRect(16+(i-2)*28, 43, 0, 245, 4, 11);
						break;
					case 7:
					case 8:
					case 9:
						this.drawTexturedModalRect(16+(i-7)*28, 71, 0, 245, 4, 11);
						break;
				}
			}
			if(container.getLineState()[i] == 1) {
				switch(i) {
					case 0:
					case 1:
						this.drawTexturedModalRect(27+i*28, 32, 0, 237, 11, 4);
						break;
					case 5:
					case 6:
						this.drawTexturedModalRect(27+(i-5)*28, 60, 0, 237, 11, 4);
						break;
					case 10:
					case 11:
						this.drawTexturedModalRect(27+(i-10)*28, 89, 0, 237, 11, 4);
						break;
					case 2:
					case 3:
					case 4:
						this.drawTexturedModalRect(16+(i-2)*28, 43, 4, 245, 4, 11);
						break;
					case 7:
					case 8:
					case 9:
						this.drawTexturedModalRect(16+(i-7)*28, 71, 4, 245, 4, 11);
						break;
				}
			}
		}
		
		if(container.craftResult != null && container.craftResult.isEmpty() == false) {
			if(container.getMemory() < 100) {
				this.drawTexturedModalRect(205, 70, 88+45, 236, 9, 20);
			}
			else if(container.getMemory() < 200) {
				this.drawTexturedModalRect(205, 70, 88+36, 236, 9, 20);
			}
			else if(container.getMemory() < 300) {
				this.drawTexturedModalRect(205, 70, 88+27, 236, 9, 20);
			}
			else if(container.getMemory() < 400) {
				this.drawTexturedModalRect(205, 70, 88+18, 236, 9, 20);
			}
			else if(container.getMemory() < 500) {
				this.drawTexturedModalRect(205, 70, 88+9, 236, 9, 20);
			}
			else {
				this.drawTexturedModalRect(205, 70, 88, 236, 9, 20);
			}
		}
		
		for(int i=0; i<6; i++) {
			if(i+this.currentTopAbility>=container.getAbilities().size()) {
				break;
			}
			int abilityId = container.getAbilities().get(i+this.currentTopAbility).getAbilityId();
			int level = container.getAbilities().get(i+this.currentTopAbility).getLevel();
			this.drawString(this.fontRenderer,
					new TextComponentTranslation("almagest.ability."+abilityId+"."+level+".name").getFormattedText(),
					95, 23+(i)*14, 0xffffff);
		}
		
		GlStateManager.disableBlend();
		
		for(int i=0; i < 46; i++) {
			Slot slot = this.inventorySlots.getSlot(i);
			if(!slot.getStack().isEmpty() && this.isPointInRegion(slot.xPos, slot.yPos, 16, 16, mouseX, mouseZ)) {
				this.renderToolTip(slot.getStack(), mouseX-this.guiLeft, mouseZ-this.guiTop);
			}
		}
		
	}
	
	/*GUIの背景の描画処理*/
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
		
		this.mc.renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		ContainerBookrest container = (ContainerBookrest)this.inventorySlots;
		for(int i=0; i<6; i++) {
			if(i+this.currentTopAbility>=container.getAbilities().size()) {
				break;
			}
			if(container.getAbilities().get(i+this.currentTopAbility).selected()
					|| (this.guiLeft+93<=mouseX && mouseX<=this.guiLeft+93+70 && this.guiTop+21+i*14<=mouseZ && mouseZ<=this.guiTop+21+i*14+12)) {
				this.drawTexturedModalRect(this.guiLeft+93, this.guiTop+21+i*14, 18, 244, 70, 12);
			}
		}
		
	}
	
	/*GUIが開いている時にゲームの処理を止めるかどうか。*/
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		ContainerBookrest container = (ContainerBookrest)this.inventorySlots;
		for(int i=0; i<6; i++) {
			if(i+this.currentTopAbility>=container.getAbilities().size()) {
				break;
			}
			if(this.guiLeft+93<=mouseX && mouseX<=this.guiLeft+93+70 && this.guiTop+21+i*14<=mouseY && mouseY<=this.guiTop+21+i*14+12) {
				container.switchAbilitySelected(i+this.currentTopAbility);
				PacketHandler.instance.sendToServer(new PacketUpdateBookrest(i+this.currentTopAbility));
			}
		}
		
	}
	
}
