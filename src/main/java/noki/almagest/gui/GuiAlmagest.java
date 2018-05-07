package noki.almagest.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import noki.almagest.AlmagestCore;
import noki.almagest.ModInfo;
import noki.almagest.attribute.AttributeHelper;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.helper.HelperConstellation.LineData;
import noki.almagest.helper.HelperConstellation.OtherStarData;
import noki.almagest.helper.HelperConstellation.StarData;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithRecipe;
import noki.almagest.saveddata.DataCategory;
import noki.almagest.saveddata.gamedata.GameData;
import noki.almagest.saveddata.gamedata.GameDataBlock;
import noki.almagest.saveddata.gamedata.GameDataConstellation;
import noki.almagest.saveddata.gamedata.GameDataItem;
import scala.collection.generic.BitOperations.Int;


/**********
 * @class GuiAlmagest
 *
 * @description ゲーム内図鑑、アルマゲストのGUIです。
 */
public class GuiAlmagest extends GuiScreen {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/gui/almagest.png");
	private static final ResourceLocation textureLeft = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/gui/almagest_left.png");
	private static final ResourceLocation textureRight = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/gui/almagest_right.png");
	
	private static int pageWidth = 200;
	private static int pageHeight = 236;
	
	private int left;
	private int top;
	private int centerX;
	private int centerY;
	
	@SuppressWarnings("unused")
	private ItemStack stack;
	private HashMap<Integer, PageContent> pages = new HashMap<Integer, PageContent>();
	
	private PageLink currentLink;
	private PageLink prevLink;
	private PageLink topLink;
	private PageLink secondLink;
	private PageLink thirdLink;
	
	private int mouseClicked;
	
	private String[] expEffect;
	private String[] expMagnitude;
	private String[] expMira;
	private String[] expConstellationLeft;
	private String[] expConstellationRight;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public GuiAlmagest(ItemStack stack) {
		
		this.stack = stack;
		for(int i = 1; i <= 88; i++) {
			this.pages.put(i, new PageContent(i, 0));
		}
		
	}
	
	@Override
	public void initGui() {
		
		//initialize position.
		this.left = (int)Math.floor((double)(this.width-pageWidth*2)/2.0D);
		this.top = (int)Math.floor((double)(this.height-pageHeight)/2.0D);
		this.centerX = (int)Math.floor((double)this.width/2.0D);
		this.centerY = (int)Math.floor((double)this.height/2.0D);		
		
		//initialize link.
		this.currentLink = new PageLink();
		this.currentLink.setToFirst();
		this.prevLink = new PageLink();
		this.prevLink.setToFirst();
		
		this.topLink = new PageLink();
		this.topLink.setToFirst();
		this.secondLink = new PageLink();
		this.secondLink.setToSecond(null);
		this.thirdLink = new PageLink();
		this.thirdLink.setToThird(null);
		
		//initialize buttons.
		this.buttonList.clear();

		int adjust = 0;
		if(this.width%2 == 1) {
			adjust = 1;
		}
		
		//buttons in the first page (category).
		int start = 1;
//		int startHeight = this.height/2 - DataCategory.values().length*18/2;
		//0-8
		for(DataCategory each: DataCategory.values()) {
			this.buttonList.add(new CategoryButton(start, (int)Math.floor(this.width/2)+3+adjust, this.height/2-18*5+18*(start-1)+10, each));
			start++;
		}
		
		//buttons in the second page (list).
		//9-18
		for(int i=1; i<=10; i++) {
			this.buttonList.add(new EachButton(100+i, (int)Math.floor((this.width-pageWidth*2)/2)+12+adjust, this.height/2-18*5+18*(i-1)+10));
		}
		//19-28
		for(int i=1; i<=10; i++) {
			this.buttonList.add(new EachButton(200+i, (int)Math.floor(this.width/2)+3+adjust, this.height/2-18*5+18*(i-1)+10));
		}
		
		//paging buttons (list).
		//29,30
		this.buttonList.add(new PagingButton(301, this.width/2+pageWidth-12-30, (this.height-pageHeight)/2+14, false));//right
		this.buttonList.add(new PagingButton(302, this.width/2+pageWidth-12-45, (this.height-pageHeight)/2+14, true));//left
		
		
		//map buttons.
		//31,32,33
/*		this.buttonList.add(new StringButton(401, this.width/2+pageWidth-32, (this.height-pageHeight)/2+15, "almagest.gui.almagest.button.top"));
		this.buttonList.add(new StringButton(402, this.width/2+pageWidth-32, (this.height-pageHeight)/2+15, "almagest.gui.almagest.button.top"));
		this.buttonList.add(new StringButton(403, this.width/2+pageWidth-32, (this.height-pageHeight)/2+15, "almagest.gui.almagest.button.top"));*/
		this.buttonList.add(new StringButton(401, (this.width-pageWidth*2)/2+17, (this.height-pageHeight)/2+15, "almagest.gui.almagest.button.top"));
		this.buttonList.add(new StringButton(402, (this.width-pageWidth*2)/2+17, (this.height-pageHeight)/2+15, "almagest.gui.almagest.button.top"));
		this.buttonList.add(new StringButton(403, (this.width-pageWidth*2)/2+17, (this.height-pageHeight)/2+15, "almagest.gui.almagest.button.top"));
		
		//page up.
		//34
		this.buttonList.add(new UpButton(303, this.width/2+pageWidth-12-15, (this.height-pageHeight)/2+13));
		
		this.updateButtons();
		
	}
	
	public void updateButtons() {
		
		switch(this.currentLink.getFloor()) {
			case 1:
//				AlmagestCore.log2("enter case 1.");
				for(int i=0; i<=DataCategory.values().length-1; i++) {
					((AlmagestButton)this.buttonList.get(i)).enable().visible();
				}
				for(int i=9; i<=18; i++) {
					((AlmagestButton)this.buttonList.get(i)).disable().invisible();
				}
				for(int i=19; i<=28; i++) {
					((AlmagestButton)this.buttonList.get(i)).disable().invisible();
				}
				((AlmagestButton)this.buttonList.get(29)).disable().invisible();
				((AlmagestButton)this.buttonList.get(30)).disable().invisible();
				((AlmagestButton)this.buttonList.get(34)).enable().visible();
				
				this.updateMap();
				break;
			case 2:
				for(int i=0; i<=DataCategory.values().length-1; i++) {
					((AlmagestButton)this.buttonList.get(i)).disable().invisible();
				}
				
				ArrayList<GameData> dataSet;
				switch(this.currentLink.getCategory()) {
					case BLOCK:
					case ITEM:
					case MOB:
					case CONSTELLATION:
						dataSet =
							AlmagestCore.savedDataManager.getFlagData().getDataSet(this.currentLink.getCategory(), (this.currentLink.getPageNum()-1)*20+1, 10);
						for(int i=1; i<=10; i++) {
							if(i <= dataSet.size()) {
								((EachButton)this.buttonList.get(i+8)).setData(dataSet.get(i-1)).enable().visible();
							}
							else {
								((AlmagestButton)this.buttonList.get(i+8)).disable().invisible();
							}
						}
						dataSet =
								AlmagestCore.savedDataManager.getFlagData().getDataSet(this.currentLink.getCategory(), (this.currentLink.getPageNum()-1)*20+11, 10);
						for(int i=1; i<=10; i++) {
							if(i <= dataSet.size()) {
								((EachButton)this.buttonList.get(i+18)).setData(dataSet.get(i-1)).enable().visible();
							}
							else {
								((AlmagestButton)this.buttonList.get(i+18)).disable().invisible();
							}
						}
						
						if(AlmagestCore.savedDataManager.getFlagData().getMaxPage(this.currentLink.getCategory(), 20) != this.currentLink.getPageNum()) {
							((AlmagestButton)this.buttonList.get(29)).enable().visible();
						}
						else {
							((AlmagestButton)this.buttonList.get(29)).disable().invisible();
						}
						break;
					case LIST:
					case ABILITY:
					case MEMO:
					case SEARCH:
					case HELP:
						for(int i=1; i<=10; i++) {
							((AlmagestButton)this.buttonList.get(i+8)).disable().invisible();
						}
						dataSet =
								AlmagestCore.savedDataManager.getFlagData().getDataSet(this.currentLink.getCategory(), (this.currentLink.getPageNum()-1)*10+1, 10);
						for(int i=1; i<=10; i++) {
							if(i <= dataSet.size()) {
								if(dataSet.get(i-1).isObtained()) {
									((EachButton)this.buttonList.get(i+18)).setData(dataSet.get(i-1)).enable().visible();
								}
								else {
									((EachButton)this.buttonList.get(i+18)).setData(dataSet.get(i-1)).disable().visible();
								}
							}
							else {
								((AlmagestButton)this.buttonList.get(i+18)).disable().invisible();
							}
						}
						
						if(AlmagestCore.savedDataManager.getFlagData().getMaxPage(this.currentLink.getCategory(), 10) != this.currentLink.getPageNum()) {
							((AlmagestButton)this.buttonList.get(29)).enable().visible();
						}
						else {
							((AlmagestButton)this.buttonList.get(29)).disable().invisible();
						}
						break;
				}
				
				if(this.currentLink.getPageNum() != 1) {
					((AlmagestButton)this.buttonList.get(30)).enable().visible();
				}
				else {
					((AlmagestButton)this.buttonList.get(30)).disable().invisible();
				}
				
				((AlmagestButton)this.buttonList.get(34)).enable().visible();
				
				this.updateMap();
				break;
			case 3:
				switch(this.currentLink.getCategory()) {
					case BLOCK:
					case ITEM:
					case MOB:
					case CONSTELLATION:
						for(int i=0; i<=DataCategory.values().length-1; i++) {
							((AlmagestButton)this.buttonList.get(i)).disable().invisible();
						}
						for(int i=9; i<=18; i++) {
							((AlmagestButton)this.buttonList.get(i)).disable().invisible();
						}
						for(int i=19; i<=28; i++) {
							((AlmagestButton)this.buttonList.get(i)).disable().invisible();
						}
//						((AlmagestButton)this.buttonList.get(29)).disable().invisible();
//						((AlmagestButton)this.buttonList.get(30)).disable().invisible();
						
						GameData prevData = AlmagestCore.savedDataManager.getFlagData().getNextData(this.currentLink.getData(),
								this.currentLink.getCategory(), AlmagestCore.proxy.getPlayer().isCreative());
						if(prevData != null) {
							((AlmagestButton)this.buttonList.get(29)).enable().visible();
						}
						
						GameData nextData = AlmagestCore.savedDataManager.getFlagData().getNextData(this.currentLink.getData(),
								this.currentLink.getCategory(), AlmagestCore.proxy.getPlayer().isCreative());
						if(nextData != null) {
							((AlmagestButton)this.buttonList.get(30)).enable().visible();
						}
						
						((AlmagestButton)this.buttonList.get(34)).enable().visible();
						break;
					case LIST:
					case ABILITY:
					case MEMO:
					case SEARCH:
					case HELP:
						for(int i=1; i<=10; i++) {
							((AlmagestButton)this.buttonList.get(i+8)).disable().invisible();
						}
						dataSet =
								AlmagestCore.savedDataManager.getFlagData().getDataSet(this.currentLink.getCategory(), (this.currentLink.getPageNum()-1)*10+1, 10);
						for(int i=1; i<=10; i++) {
							if(i <= dataSet.size()) {
								if(dataSet.get(i-1).isObtained()) {
									((EachButton)this.buttonList.get(i+18)).setData(dataSet.get(i-1)).enable().visible();
								}
								else {
									((EachButton)this.buttonList.get(i+18)).setData(dataSet.get(i-1)).disable().visible();
								}
							}
							else {
								((AlmagestButton)this.buttonList.get(i+18)).disable().invisible();
							}
						}
						((AlmagestButton)this.buttonList.get(29)).enable().visible();
						if(this.currentLink.getPageNum() != 1) {
							((AlmagestButton)this.buttonList.get(30)).enable().visible();
						}
						else {
							((AlmagestButton)this.buttonList.get(30)).disable().invisible();
						}
						((AlmagestButton)this.buttonList.get(34)).enable().visible();
						break;
				}
				this.updateMap();
				break;
		}
		
	}
	
	public void updateMap() {
		
		StringButton first = (StringButton)this.buttonList.get(31);
		StringButton second = (StringButton)this.buttonList.get(32);
		StringButton third = (StringButton)this.buttonList.get(33);
		switch(this.currentLink.getFloor()) {
			case 1:
				third.disable().invisible();
				second.disable().invisible();
//				((StringButton)first.disable().visible()).setx(this.width/2+pageWidth-20);
//				((StringButton)first.disable().visible()).setx((this.width-pageWidth*2)/2+40);
				((StringButton)first.disable().visible())
					.setx((this.width-pageWidth*2)/2+17);
				break;
			case 2:
				third.disable().invisible();
/*				((StringButton)second.disable().visible()).setDisplay(this.secondLink.getCategory().getDisplay())
					.setx(this.width/2+pageWidth-20);
				((StringButton)first.enable().visible()).setx(this.width/2+pageWidth-20-second.width-10);*/
				((StringButton)first.enable().visible())
					.setx((this.width-pageWidth*2)/2+17);
				((StringButton)second.disable().visible()).setDisplay(this.secondLink.getCategory().getDisplay())
					.setx((this.width-pageWidth*2)/2+17+first.width+10);
				break;
			case 3:
/*				((StringButton)third.disable().visible()).setDisplay(this.thirdLink.getData().getDisplay())
					.setx(this.width/2+pageWidth-20);
				((StringButton)second.enable().visible()).setDisplay(this.secondLink.getCategory().getDisplay())
					.setx(this.width/2+pageWidth-20-third.width-10);
				((StringButton)first.enable().visible()).setx(this.width/2+pageWidth-20-third.width-10-second.width-10);*/
				((StringButton)first.enable().visible())
					.setx((this.width-pageWidth*2)/2+17);
				((StringButton)second.enable().visible()).setDisplay(this.secondLink.getCategory().getDisplay())
					.setx((this.width-pageWidth*2)/2+17+first.width+10);
				((StringButton)third.disable().visible()).setDisplay(this.thirdLink.getData().getDisplay())
					.setx((this.width-pageWidth*2)/2+17+first.width+10+second.width+10);
				break;
		}
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		
		this.mouseClicked++;
		if(this.mouseClicked > 1) {
			return;
		}
		PageLink prev = this.prevLink;
		this.prevLink = this.currentLink.clone();
		
		switch(button.id) {
//			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				this.currentLink.setToSecond(((CategoryButton)button).getCategory());
				this.secondLink.setToSecond(((CategoryButton)button).getCategory());
				break;
			case 101:
			case 102:
			case 103:
			case 104:
			case 105:
			case 106:
			case 107:
			case 108:
			case 109:
			case 110:
			case 201:
			case 202:
			case 203:
			case 204:
			case 205:
			case 206:
			case 207:
			case 208:
			case 209:
			case 210:
				this.currentLink.setToThird(((EachButton)button).getData());
				this.thirdLink.setToThird(((EachButton)button).getData());
				this.setThirdExplanation();
				break;
			case 301:
				if(this.currentLink.getFloor() == 3) {
					GameData nextData = AlmagestCore.savedDataManager.getFlagData().getNextData(this.currentLink.getData(),
							this.currentLink.getCategory(), AlmagestCore.proxy.getPlayer().isCreative());
					if(nextData != null) {
						this.currentLink.setToThird(nextData);
						this.setThirdExplanation();
					}
				}
				else {
					this.currentLink.nextPage();
				}
				break;
			case 302:
				if(this.currentLink.getFloor() == 3) {
					GameData prevData = AlmagestCore.savedDataManager.getFlagData().getPrevData(this.currentLink.getData(),
							this.currentLink.getCategory(), AlmagestCore.proxy.getPlayer().isCreative());
					if(prevData != null) {
						this.currentLink.setToThird(prevData);
						this.setThirdExplanation();
					}
				}
				else {
					this.currentLink.prevPage();
				}
				break;
			case 303:
				this.currentLink = prev;
				break;
			case 401:
				this.currentLink.setToFirst();
				break;
			case 402:
				this.currentLink.setToSecond(this.secondLink.getCategory());
				this.currentLink.setPageNum(this.secondLink.getPageNum());
				break;
			case 403:
				break;
		}
		
		this.updateButtons();
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		this.mouseClicked = 0;
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		//reset color.
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		//draw background.
		this.mc.getTextureManager().bindTexture(textureLeft);
		this.drawTexturedModalRect(this.centerX-200, this.centerY-118, 0, 0, 200, 236);
		this.mc.getTextureManager().bindTexture(textureRight);
		this.drawTexturedModalRect(this.centerX, this.centerY-118, 0, 0, 200, 236);
		
		this.mc.getTextureManager().bindTexture(texture);
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		//switch by category.
		if(this.currentLink.getFloor() != 3) {
			return;
		}
		
		switch(this.currentLink.getCategory()) {
			case ABILITY:
				break;
			case BLOCK:
			case ITEM:
				//left page.
				
				//display name.
				this.drawScaledTranslated(this.currentLink.getData().getDisplay(), this.left+20, this.top+103, 1.5D, 0XFFc3a25c, true);
				
				//display bars and recipe, component.
				drawRect(this.left+16, this.top+118, this.centerX-5, this.top+120, 0xffd3c3a2);
				drawRect(this.left+104, this.top+120, this.left+106, this.top+223, 0xffd3c3a2);
				this.fontRenderer.drawString(this.getTranslated("almagest.gui.almagest.component", true), this.left+19, this.top+125, 0XFFc3a25c);
				this.fontRenderer.drawString(this.getTranslated("almagest.gui.almagest.recipe", true), this.left+110, this.top+125, 0XFFc3a25c);
				
				//display component.
				int component = 0;
				for(EStarAttribute each: EStarAttribute.values()) {
					int level = AttributeHelper.getAttrributeLevel(((GameDataBlock)this.currentLink.getData()).getStack(), each);
					if(level != 0) {
						this.fontRenderer.drawString(this.getTranslated(each.getName())+" : "+level, this.left+21, this.top+143+component*16, 0XFFc3a25c);
						component++;
					}
				}
				for(int i=0; i<=4; i++) {
					drawRect(this.left+18, this.top+138+i*16, this.left+100, this.top+139+i*16, 0xffd3c3a2);
				}
				
				//display recipe;
//				drawRect((this.width-pageWidth*2)/2+18, (this.height-pageHeight)/2+218, (this.width-pageWidth*2)/2+100, (this.height-pageHeight)/2+219, 0xffd3c3a2);
				boolean hasRecipe;
				if(this.currentLink.getCategory() == DataCategory.BLOCK) {
					hasRecipe = ((GameDataBlock)this.currentLink.getData()).getBlock() instanceof IWithRecipe;
				}
				else {
					hasRecipe = ((GameDataItem)this.currentLink.getData()).getItem() instanceof IWithRecipe;
				}
				if(hasRecipe) {
					IWithRecipe withRecipe;
					if(this.currentLink.getCategory() == DataCategory.BLOCK) {
						withRecipe = (IWithRecipe)((GameDataBlock)this.currentLink.getData()).getBlock();
					}
					else {
						withRecipe = (IWithRecipe)((GameDataItem)this.currentLink.getData()).getItem();
					}
					IRecipe recipe = withRecipe.getRecipe(((GameDataBlock)this.currentLink.getData()).getStack());
					if(recipe instanceof StarRecipe && ((StarRecipe)recipe).isSpecial() == false) {
						StarRecipe starRecipe = (StarRecipe)recipe;
						int recipeCount = 0;
						for(Map.Entry<EStarAttribute, Integer> each: starRecipe.getAttribute().entrySet()) {
							this.fontRenderer.drawString(this.getTranslated(each.getKey().getName())+" : "+each.getValue(),
									this.left+21+93, this.top+143+recipeCount*16, 0XFFc3a25c);
							recipeCount++;
						}
						for(ItemStack each: starRecipe.getStack()) {
							this.fontRenderer.drawString(this.getTranslated(each.getDisplayName()),
									this.left+21+93, this.top+143+recipeCount*16, 0XFFc3a25c);
							recipeCount++;
						}
					}
					else {
						for(int i=0; i<=4; i++) {
							this.fontRenderer.drawString(
								this.getTranslated("almagest.gui.almagest."+this.currentLink.getData().getName().toString()+".recipe."+i),
								this.left+21+93, this.top+143+i*16, 0XFFc3a25c);
						}
					}
				}
				for(int i=0; i<=4; i++) {
					drawRect(this.left+18+93, this.top+138+i*16, this.left+100+93, this.top+139+i*16, 0xffd3c3a2);
				}
//				drawRect((this.width-pageWidth*2)/2+18+93, (this.height-pageHeight)/2+218, (this.width-pageWidth*2)/2+100+93, (this.height-pageHeight)/2+219, 0xffd3c3a2);
				
				//right page.
				int thirdStart = (this.height-pageHeight)/2+17;
				this.fontRenderer.drawString(new TextComponentTranslation("almagest.gui.almagest.effect").setStyle(new Style().setBold(true)).getFormattedText(),
						this.width/2+7, thirdStart, 0XFFc3a25c);
				drawRect(this.width/2+6, thirdStart+9, this.width/2+pageWidth-13, thirdStart+11, 0xffd3c3a2);
	/*			for(int i=0; i<=6; i++) {
					this.fontRenderer.drawString("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", this.width/2+7, thirdStart+14+i*10, 0XFFc3a25c);
				}*/
				for(int i=0; i<this.expEffect.length; i++) {
					this.fontRenderer.drawString(this.expEffect[i], this.width/2+7, thirdStart+14+i*10, 0XFFc3a25c);
				}
				thirdStart = (this.height-pageHeight)/2+104;
				this.fontRenderer.drawString(new TextComponentTranslation("almagest.gui.almagest.magnitude").setStyle(new Style().setBold(true)).getFormattedText(),
						this.width/2+7, thirdStart, 0XFFc3a25c);
				drawRect(this.width/2+6, thirdStart+9, this.width/2+pageWidth-13, thirdStart+11, 0xffd3c3a2);
				for(int i=0; i<this.expMagnitude.length; i++) {
					this.fontRenderer.drawString(this.expMagnitude[i], this.width/2+7, thirdStart+14+i*10, 0XFFc3a25c);
				}
				thirdStart = (this.height-pageHeight)/2+190;
				this.fontRenderer.drawString(new TextComponentTranslation("almagest.gui.almagest.mira").setStyle(new Style().setBold(true)).getFormattedText(),
						this.width/2+7, thirdStart, 0XFFc3a25c);
				drawRect(this.width/2+6, thirdStart+9, this.width/2+pageWidth-13, thirdStart+11, 0xffd3c3a2);
				for(int i=0; i<this.expMira.length; i++) {
					this.fontRenderer.drawString(this.expMira[i], this.width/2+7, thirdStart+14+i*10, 0XFFc3a25c);
				}
				break;
			case CONSTELLATION:
				GlStateManager.pushMatrix();
				GlStateManager.scale(2.0D, 2.0D, 2.0D);
				this.fontRenderer.drawString(new TextComponentTranslation(this.currentLink.getData().getDisplay()).setStyle(new Style().setBold(false)).getFormattedText(),
						(int)(((this.width-pageWidth*2)/2+17)/1.8D), (int)(((this.height-pageHeight)/2+152)/1.8D), 0XFFc3a25c);
				GlStateManager.popMatrix();
				drawRect((this.width-pageWidth*2)/2+17, (this.height-pageHeight)/2+176+9, this.width/2-5, (this.height-pageHeight)/2+176+11, 0xffd3c3a2);
	/*					for(int i=0; i<this.expConstellationLeft.length; i++) {
					this.fontRenderer.drawString(this.expConstellationLeft[i], (this.width-pageWidth*2)/2+17, (this.height-pageHeight)/2+191+14+i*10, 0XFFc3a25c);
				}*/
				this.mc.getTextureManager().bindTexture(texture);
	            GlStateManager.enableBlend();
	            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				
	//			this.drawTexturedModalRect((this.width-pageWidth*2)/2+55, (this.height-pageHeight)/2+35, 140, 0, 95, 70);
				this.drawTexturedModalRect((this.width-pageWidth*2)/2+40+0, (this.height-pageHeight)/2+31, 206, 106, 50, 130);
				this.drawTexturedModalRect((this.width-pageWidth*2)/2+40+50, (this.height-pageHeight)/2+31, 206, 106, 50, 130);
				this.drawTexturedModalRect((this.width-pageWidth*2)/2+40+100, (this.height-pageHeight)/2+31, 206, 106, 30, 130);
				
				GlStateManager.disableBlend();
				this.pages.get(((GameDataConstellation)this.currentLink.getData()).getConstellation().getId()).renderConstellation(
						(this.width-pageWidth*2)/2+105, (this.height-pageHeight)/2+96);
				
				for(int i=0; i<this.expConstellationLeft.length; i++) {
					this.fontRenderer.drawString(this.expConstellationLeft[i], (this.width-pageWidth*2)/2+17, (this.height-pageHeight)/2+190+i*10, 0XFFc3a25c);
				}
				int rightStart = (this.height-pageHeight)/2+27;
				for(int i=0; i<this.expConstellationRight.length; i++) {
					this.fontRenderer.drawString(this.expConstellationRight[i], this.width/2+7, rightStart+i*10, 0XFFc3a25c);
				}
				break;
			case HELP:
				break;
			case LIST:
				break;
			case MEMO:
				break;
			case MOB:
				break;
			case SEARCH:
				break;
		}
				
		switch(this.currentLink.getCategory()) {
			case ABILITY:
				break;
			case BLOCK:
			case ITEM:
				this.zLevel = 100.0F;
				this.itemRender.zLevel = 100.0F;
				RenderHelper.disableStandardItemLighting();
				RenderHelper.enableGUIStandardItemLighting();
				GlStateManager.disableDepth();
				GlStateManager.disableLighting();
				GlStateManager.pushMatrix();
				GlStateManager.scale(3.0D, 3.0D, 3.0D);
				this.itemRender.renderItemAndEffectIntoGUI(((GameDataBlock)this.currentLink.getData()).getStack(), (int)(((this.width-pageWidth*2)/2+83)/3.0D), (int)(((this.height-pageHeight)/2+42)/3.0D));
				GlStateManager.popMatrix();
				GlStateManager.enableDepth();
				GlStateManager.enableLighting();
				RenderHelper.enableStandardItemLighting();
				this.itemRender.zLevel = 0.0F;
				this.zLevel = 0.0F;
				break;
			case CONSTELLATION:
				break;
			case HELP:
				break;
			case LIST:
				break;
			case MEMO:
				break;
			case MOB:
				break;
			case SEARCH:
				break;
		}
		
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
		
	}
	
	public String getTranslated(String key) {
		
		return this.getTranslated(key, false);
		
	}
	
	public String getTranslated(String key, boolean bold) {
		
		return new TextComponentTranslation(key).setStyle(new Style().setBold(bold)).getFormattedText();
		
	}
	
	public void drawScaledTranslated(String key, int x, int y, double scale, int color, boolean bold) {
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(scale, scale, scale);
		this.fontRenderer.drawString(this.getTranslated(key, bold), (int)(x/scale), (int)(y/scale), color);
		GlStateManager.popMatrix();
		
	}
	
	private void setThirdExplanation() {
		
		switch(this.currentLink.getCategory()) {
			case BLOCK:
			case ITEM:
				this.expEffect = this.explode(
						this.getTranslated("almagest.gui.almagest."+this.currentLink.getData().getName().toString()+".effect"), 180, 7);
				this.expMagnitude = this.explode(
						this.getTranslated("almagest.gui.almagest."+this.currentLink.getData().getName().toString()+".magnitude"), 180, 7);
				this.expMira = this.explode(
						this.getTranslated("almagest.gui.almagest."+this.currentLink.getData().getName().toString()+".mira"), 180, 2);
				break;
			case ABILITY:
				break;
			case CONSTELLATION:
				this.expConstellationLeft
					= this.explode(new TextComponentTranslation("almagest.gui.constellation."+((GameDataConstellation)this.currentLink.getData()).getConstellation().getId()+".explanation.left").getFormattedText(), 180, 2);
				this.expConstellationRight
				= this.explode(new TextComponentTranslation("almagest.gui.constellation."+((GameDataConstellation)this.currentLink.getData()).getConstellation().getId()+".explanation.right").getFormattedText(), 180, 19);
				break;
			case LIST:
				break;
			case MEMO:
				break;
			case MOB:
				break;
			case HELP:
				break;
			case SEARCH:
				break;
		}
		
	}
	
	private String[] explode(String string, int width, int maxRow) {
		
		String[] split = string.split("<br>", 0);
		
		ArrayList<String> stringSet = new ArrayList<String>();
		String remain;
		for(String each: split) {
			remain = each;
			while(this.fontRenderer.getStringWidth(remain) > width && stringSet.size() != maxRow) {
				for(int i=1; i<=100; i++) {
					if(this.fontRenderer.getStringWidth(remain.substring(0, i)) > width) {
						stringSet.add(remain.substring(0, i-1));
						remain = remain.substring(i-1);
						break;
					}
				}
			}
			if(stringSet.size() < maxRow) {
				stringSet.add(remain);
			}
		}
		
		return stringSet.toArray(new String[stringSet.size()]);
		
	}
	
	private class PageContent {
		
		public int constNumber;
		@SuppressWarnings("unused")
		public int flag;
		public ArrayList<StarData> stars;
		public ArrayList<OtherStarData> otherStars;
		public List<LineData> lines;
		
		private static final double size = 90;
		private static final double maxWidth = 100;
//		private static final double maxStarWidth = 150;
//		private static final float scale = 0.00390625F;
		private double weight;
/*		private double adjustLong;
		private double adjustLat;
		private double relativeAddLong;
		private double relativeAddLat;*/
		private double centerLong;
		private double centerLat;
		private Map<Integer, Vector3d> calcStar = new HashMap<Integer, Vector3d>();
		
		public PageContent(int constNumber, int flag) {
			
			this.constNumber = constNumber;
			this.flag = flag;
			this.stars = HelperConstellation.getStars(this.constNumber);
			this.lines = HelperConstellation.getLines(this.constNumber);
			this.otherStars = HelperConstellation.getOtherStars(this.constNumber);
			
			//calculate the center of the constellation.
			double minLong = Integer.MAX_VALUE;	//赤経
			double minLat = Integer.MAX_VALUE;	//赤緯
			double maxLong = Integer.MIN_VALUE;
			double maxLat = Integer.MIN_VALUE;
			for(StarData each: stars) {
				minLong = Math.min(minLong, each.getCalculatedLong());
				minLat = Math.min(minLat, each.getCalculatedLat());
				maxLong = Math.max(maxLong, each.getCalculatedLong());
				maxLat = Math.max(maxLat, each.getCalculatedLat());
			}
			this.centerLong = (minLong+maxLong) / 2.0D;
			if(Math.abs(minLong-maxLong) > 180) {
				double otherDist = (360.0D - Math.abs(minLong-maxLong)) / 2.0D;
				if((minLong-otherDist) > 0) {
					this.centerLong = minLong - otherDist;
				}
				else {
					this.centerLong = maxLong + otherDist;
				}
			}
			this.centerLat = (minLat+maxLat) / 2.0D;
			
//			this.centerLong = Constellation.getConstFromNumber(this.constNumber).getRa();
//			this.centerLat = Constellation.getConstFromNumber(this.constNumber).getDec();
			
			double minY = Integer.MAX_VALUE;
			double minX = Integer.MAX_VALUE;
			double maxY = Integer.MIN_VALUE;
			double maxX = Integer.MIN_VALUE;
			//transform sphere coordination into plane coordination.
			for(StarData each: this.stars) {
				Vector3d relative = this.calcRotate(this.createVector(size, 0, 0), each.getCalculatedLong(), -1.0D*each.getCalculatedLat());
				relative = this.moveToCenter(relative, -1.0D*this.centerLong, this.centerLat);
//				relative = this.calcRotate(relative, -1.0D*this.centerLong, this.centerLat);
				this.calcStar.put(each.hip, relative);
				minY = Math.min(minY, relative.z);
				minX = Math.min(minX, relative.y);
				maxY = Math.max(maxY, relative.z);
				maxX = Math.max(maxX, relative.y);
			}
			for(OtherStarData each: this.otherStars) {
				Vector3d relative = this.calcRotate(this.createVector(size, 0, 0), each.ra, -1.0D*each.dec);
				relative = this.moveToCenter(relative, -1.0D*this.centerLong, this.centerLat);
				this.calcStar.put(each.hip, relative);
			}
			this.weight = maxWidth / Math.max(Math.abs(minX-maxX), Math.abs(minY-maxY));
			
/*			double defLong = Math.abs(maxLong - minLong);
			double defLat = Math.abs(maxLat - minLat);
			this.weight = size / Math.max(defLong, defLat);
			this.adjustLong = minLong * -1;
			this.adjustLat = minLat * -1;
			this.relativeAddLong = (size-defLong*this.weight)/2;
			this.relativeAddLat = (size-defLat*this.weight)/2;*/
			
			
		}
		
		public void renderConstellation(double startX, double startY) {
			
/*			for(OtherStarData each: this.otherStars) {
				Vector3d relative = this.calcStar.get(each.hip);
				double scale = 0.4 - Math.min(each.magnitude*0.05, 0.25);
				double adjust = 16*scale/2;
				if(Math.abs(relative.z*this.weight) > maxStarWidth/2 || Math.abs(relative.y*this.weight) > maxStarWidth/2) {
					continue;
				}
				this.drawRect(startX-relative.y*this.weight-adjust, startY-relative.z*this.weight-adjust,
						140, 81+(each.spectrum.getMetadata()-1)*16, 16, 16, scale);
			}*/
			
			for(LineData each: this.lines) {
/*				double relativeX1 = startX - ((each.star1.getCalculatedLong() + this.adjustLong) * this.weight + this.relativeAddLong);
				double relativeY1 = startY - ((each.star1.getCalculatedLat() + this.adjustLat) * this.weight + this.relativeAddLat);
				double relativeX2 = startX - ((each.star2.getCalculatedLong() + this.adjustLong) * this.weight + this.relativeAddLong);
				double relativeY2 = startY - ((each.star2.getCalculatedLat() + this.adjustLat) * this.weight + this.relativeAddLat);*/
				Vector3d relative1 = this.calcStar.get(each.star1.hip);
				Vector3d relative2 = this.calcStar.get(each.star2.hip);
				
				this.drawLine(startX-relative1.y*this.weight, startY-relative1.z*this.weight,
						startX-relative2.y*this.weight, startY-relative2.z*this.weight);
			}
			
			for(StarData each: this.stars) {
/*				double relativeX = startX - ((each.getCalculatedLong() + this.adjustLong) * this.weight + this.relativeAddLong);
				double relativeY = startY - ((each.getCalculatedLat() + this.adjustLat) * this.weight + this.relativeAddLat);*/
				Vector3d relative = this.calcStar.get(each.hip);

				double scale = 0.4 - Math.min(each.magnitude*0.05, 0.25);
				double adjust = 16*scale/2;
				this.drawRect(startX-relative.y*this.weight-adjust, startY-relative.z*this.weight-adjust,
						140, 81+(each.spectrum.getMetadata()-1)*16, 16, 16, scale);
			}
			
		}
		
		private void drawRect(double x, double y, int textureX, int textureY, int width, int height, double scale) {
			
			float f = 0.00390625F;
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder renderer = tessellator.getBuffer();
			
			renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			renderer.pos((double)(x+0*scale), (double)(y+height*scale), (double)(GuiAlmagest.this.zLevel))
				.tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f)).endVertex();
			renderer.pos((double)(x+width*scale), (double)(y+height*scale), (double)(GuiAlmagest.this.zLevel))
				.tex((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f)).endVertex();
			renderer.pos((double)(x+width*scale), (double)(y+0*scale), (double)(GuiAlmagest.this.zLevel))
				.tex((double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f)).endVertex();
			renderer.pos((double)(x+0*scale), (double)(y+0*scale), (double)(GuiAlmagest.this.zLevel))
				.tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f)).endVertex();
			tessellator.draw();
			
		}
		
		private void drawLine(double relativeX1, double relativeY1, double relativeX2, double relativeY2) {
		
			BufferBuilder renderer = Tessellator.getInstance().getBuffer();
	        GlStateManager.enableBlend();
	        GlStateManager.disableTexture2D();
	        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.color(1F, 1F, 1F, 1F);
			
			renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
			GL11.glLineWidth(2);
			renderer.pos(relativeX1, relativeY1, GuiAlmagest.this.zLevel).endVertex();
			renderer.pos(relativeX2, relativeY2, GuiAlmagest.this.zLevel).endVertex();
			Tessellator.getInstance().draw();
			
	        GlStateManager.enableTexture2D();
	        GlStateManager.disableBlend();
			
		}
		
		private Vector3d calcRotate(Vector3d vector, double g, double b) {
			
			double sinB = Math.sin(Math.toRadians(b));
			double sinG = Math.sin(Math.toRadians(g));
			double cosB = Math.cos(Math.toRadians(b));
			double cosG = Math.cos(Math.toRadians(g));

			double newX = vector.x*cosB*cosG + vector.z*sinB*cosG - vector.y*sinG;
			double newY = vector.x*sinG*cosB + vector.z*sinG*sinB + vector.y*cosG;
			double newZ = -1*vector.x*sinB + vector.z*cosB;
			
			return this.createVector(newX, newY, newZ);
			
		}
		
		private Vector3d createVector(double x, double y, double z) {
			
			Vector3d newVector = new Vector3d();
			newVector.x = x;
			newVector.y = y;
			newVector.z = z;
			
			return newVector;
			
		}
		
		private Vector3d moveToCenter(Vector3d vector, double g, double b) {
			
			double sinB = Math.sin(Math.toRadians(b));
			double sinG = Math.sin(Math.toRadians(g));
			double cosB = Math.cos(Math.toRadians(b));
			double cosG = Math.cos(Math.toRadians(g));

			double newX = vector.x*cosB*cosG - vector.y*cosB*sinG + vector.z*sinB;
			double newY = vector.x*sinG + vector.y*cosG;
			double newZ = -1*vector.x*sinB*cosG + vector.y*sinB*sinG + vector.z*cosB;
			
			return this.createVector(newX, newY, newZ);
			
		}
		
	}
	
	private class AlmagestButton extends GuiButton {

		public AlmagestButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
			super(buttonId, x, y, widthIn, heightIn, buttonText);
		}
		
		public AlmagestButton enable() {
			this.enabled = true;
			return this;
		}
		
		public AlmagestButton disable() {
			this.enabled = false;
			return this;
		}
		
		public AlmagestButton visible() {
			this.visible = true;
			return this;
		}
		
		public AlmagestButton invisible() {
			this.visible = false;
			return this;
		}
		
	}
	
	private class PagingButton extends AlmagestButton {
		
		private final boolean isLeft;
		
		public PagingButton(int buttonID, int x, int y, boolean isLeft) {
			super(buttonID, x, y, 12, 9, "");
			this.isLeft = isLeft;
		}
		
		//drawButton.
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTick) {
			if(this.visible) {
				boolean mouseOver = false;
				if(mouseX >= this.x && mouseY >= this.y
						&& mouseX < this.x + this.width && mouseY < this.y + this.height) {
					mouseOver = true;
				}
				
	            GlStateManager.enableBlend();
	            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(GuiAlmagest.textureLeft);
				int x = 214;
				int y = 237;
				
				if(mouseOver) {
					y += 10;
				}
				if(!isLeft) {
					x += 16;
				}
				
				this.drawTexturedModalRect(this.x, this.y, x, y, this.width, this.height);
				GlStateManager.disableBlend();
			}
		}
		
	}
	
	private class UpButton extends AlmagestButton {
		
		public UpButton(int buttonID, int x, int y) {
			super(buttonID, x, y, 9, 10, "");
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTick) {
			if(this.visible) {
				boolean mouseOver = false;
				if(mouseX >= this.x && mouseY >= this.y
						&& mouseX < this.x + this.width && mouseY < this.y + this.height) {
					mouseOver = true;
				}
				
	            GlStateManager.enableBlend();
	            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(GuiAlmagest.textureLeft);
				int x = 247;
				int y = 236;
				if(mouseOver) {
					y += 10;
				}
				this.drawTexturedModalRect(this.x, this.y, x, y, this.width, this.height);
				GlStateManager.disableBlend();
			}
		}
		
	}
	
	private class CategoryButton extends AlmagestButton {
		
		private DataCategory category;
		
		public CategoryButton(int buttonId, int x, int y, DataCategory category) {
			super(buttonId, x, y, 185, 18, "");
			this.category = category;
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTick) {
			if(this.visible) {
				boolean mouseOver = false;
				if(mouseX >= this.x && mouseY >= this.y
						&& mouseX < this.x + this.width && mouseY < this.y + this.height) {
					mouseOver = true;
				}
				
				if(mouseOver) {
		            GlStateManager.enableBlend();
		            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					drawRect(this.x, this.y, this.x+this.width, this.y+this.height, 0x30ffa500);
					GlStateManager.disableBlend();
				}
				mc.fontRenderer.drawString(I18n.format(this.category.getDisplay()), this.x+10, this.y+6, 0XFFc3a25c, false);
			}
		}
		
		public DataCategory getCategory() {
			return this.category;
		}
		
	}
	
	private class EachButton extends AlmagestButton {
		
		private GameData data;
		
		public EachButton(int buttonId, int x, int y) {
			super(buttonId, x, y, 185, 18, "");
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTick) {
			if(this.visible) {
				boolean mouseOver = false;
				if(mouseX >= this.x && mouseY >= this.y
						&& mouseX < this.x + this.width && mouseY < this.y + this.height) {
					mouseOver = true;
				}
				
				if(mouseOver) {
		            GlStateManager.enableBlend();
		            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					drawRect(this.x, this.y, this.x+this.width, this.y+this.height, 0x30ffa500);
					GlStateManager.disableBlend();
				}
				if((this.data == null || this.data.isObtained() == false) && AlmagestCore.proxy.getPlayer().isCreative() == false) {
					mc.fontRenderer.drawString("?????", this.x+10, this.y+6, 0XFFc3a25c);
				}
				else {
					mc.fontRenderer.drawString(I18n.format(this.data.getDisplay()), this.x+10, this.y+6, 0XFFc3a25c);
				}
			}
		}
		
		public AlmagestButton setData(GameData data) {
			this.data = data;
			return this;
		}
		
		public GameData getData() {
			return this.data;
		}
		
	}
	
	private class StringButton extends AlmagestButton {
		
		public StringButton(int buttonId, int x, int y, String display) {
			super(buttonId, x, y, 180, 20, display);
			this.setDisplay(display);
			this.setx(x);
		}
		
		public StringButton setDisplay(String display) {
			this.displayString = display;
			int stringWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(I18n.format(this.displayString));
			this.width = stringWidth;
			return this;
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTick) {
			if(this.visible) {
				boolean mouseOver = false;
				if(mouseX >= this.x && mouseY >= this.y
						&& mouseX < this.x + this.width && mouseY < this.y + this.height) {
					mouseOver = true;
				}
/*				if(mouseOver) {
					drawRect(this.x, this.y, this.x+this.width, this.y+this.height, 0xffa50050);
				}*/
				mc.fontRenderer.drawString(
						new TextComponentTranslation(this.displayString).setStyle(new Style().setUnderlined(this.enabled && !mouseOver)).getFormattedText(),
						this.x, this.y, 0XFFc3a25c);
				if(this.enabled) {
					mc.fontRenderer.drawString(">", this.x+this.width+3, this.y, 0XFFc3a25c);
					
				}
			}
		}
		
		public StringButton setx(int x) {
//			this.x = x-this.width;
			this.x = x;
			return this;
		}
		
	}
	
	private class PageLink {
		
		private int floor;
		private DataCategory category;
		private GameData data;
		private int pageNum;
		
		public void setToFirst() {
			this.floor = 1;
			this.category = null;
			this.data = null;
			this.pageNum = 1;
		}
		
		public void setToSecond(DataCategory category) {
			this.floor = 2;
			this.category = category;
			this.data = null;
			this.pageNum = 1;
		}
		
		public void setToThird(GameData data) {
			this.floor = 3;
			this.data = data;
//			this.pageNum = 1;
		}
		
		public int getFloor() {
			return this.floor;
		}
		
		public int getPageNum() {
			return this.pageNum;
		}
		
		public void nextPage() {
			this.pageNum++;
		}
		
		public void prevPage() {
			if(this.pageNum != 1) {
				this.pageNum--;
			}
		}
		
		public void setPageNum(int pageNum) {
			this.pageNum = pageNum;
		}
		
		public DataCategory getCategory() {
			return this.category;
		}
		
		public GameData getData() {
			return this.data;
		}
		
		public PageLink clone() {
			
			PageLink newLink = new PageLink();
			switch(this.floor) {
			case 1:
				newLink.setToFirst();
				break;
			case 2:
				newLink.setToSecond(this.getCategory());
				newLink.setPageNum(this.pageNum);
				break;
			case 3:
				newLink.setToSecond(this.getCategory());
				newLink.setPageNum(this.pageNum);
				newLink.setToThird(this.getData());
				break;
			}
			
			return newLink;
			
		}
		
	}
	
}
