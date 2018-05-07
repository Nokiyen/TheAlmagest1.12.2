package noki.almagest.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.gui.sequence.GuiContainerSequence;

public class EventRender {
	
	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent event) {
		
		if(event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
			return;
		}
		
		GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		if(screen == null || !(screen instanceof GuiContainerSequence)) {
			return;
		}
		
		event.setCanceled(true);
		
	}

}
