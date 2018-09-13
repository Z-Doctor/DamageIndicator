package zdoctor.damageindicator.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zdoctor.damageindicator.client.render.HealthRenderer;

@SideOnly(Side.CLIENT)
public class ClientEvents {
	
	@SubscribeEvent
	public void renderEntity(RenderLivingEvent.Pre renderEntity) {
//		HealthRenderer.renderHealth(renderEntity);
//		System.out.println(renderEntity.getPartialRenderTick());
	}

	@SubscribeEvent
	public void renderEntity(RenderLivingEvent.Post renderEntity) {
		HealthRenderer.renderHealth(renderEntity);
	}
}
