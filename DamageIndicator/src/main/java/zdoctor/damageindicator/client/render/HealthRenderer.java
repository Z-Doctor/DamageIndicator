package zdoctor.damageindicator.client.render;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import zdoctor.damageindicator.client.RenderHelper;

public class HealthRenderer {

	public static void renderHealth(RenderLivingEvent renderEntity) {
		preDraw(renderEntity);

		drawHealthString(renderEntity);

		EntityLivingBase entity = renderEntity.getEntity();

		drawHealth(renderEntity);

		postDraw(renderEntity);

	}

	public static void drawHealthString(RenderLivingEvent renderEntity) {
		EntityLivingBase entity = renderEntity.getEntity();
		FontRenderer fontRenderer = renderEntity.getRenderer().getFontRendererFromRenderManager();

		IAttributeInstance attributes = entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		float maxHealth = (float) attributes.getAttributeValue();
		float absorption = entity.getAbsorptionAmount();

		String status = String.format("%d/%d", MathHelper.ceil(entity.getHealth()),
				MathHelper.ceil(maxHealth + absorption));

		fontRenderer.drawString(status, -fontRenderer.getStringWidth(status) / 2, -fontRenderer.FONT_HEIGHT, 0);
	}

	public static void drawHealth(RenderLivingEvent renderEntity) {
		Random rand = new Random();
		EntityLivingBase entity = renderEntity.getEntity();

		GlStateManager.translate(-4.5 * (entity.getMaxHealth() / 2), 0, 0);
		GlStateManager.color(1, 1, 1, 1);
		RenderHelper.bindTexture(Gui.ICONS);

		rand.setSeed(entity.ticksExisted * 312871);

		IAttributeInstance attributes = entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		float maxHealth = (float) attributes.getAttributeValue();
		float absorption = entity.getAbsorptionAmount();
		int health = MathHelper.ceil(entity.getHealth());
		float lasthealth = entity.getLastHealth();
		int rows = MathHelper.ceil((maxHealth + absorption) / 2.0F / 10.0F);
		int rowSpacing = Math.max(10 - (rows - 2), 3);

//		if (entity.getHealth() != lasthealth)
//			System.out.println(entity.getHealth() + ":" + lasthealth);

//		System.out.println(entity.hurtResistantTime);
		boolean hurtResistant = entity.hurtResistantTime / 3 % 2 == 1;
		if (entity.hurtResistantTime < 10)
			hurtResistant = false;

		int regenCounter = -1;

		if (entity.isPotionActive(MobEffects.REGENERATION)) {
			regenCounter = entity.ticksExisted % MathHelper.ceil(maxHealth + 5.0F);
		}

		for (int heart = MathHelper.ceil((maxHealth + absorption) / 2.0F) - 1; heart >= 0; --heart) {
			int heartGui = 16;

			int xOffset = heart % 10 * 9;

			int heartRows = MathHelper.ceil(heart / 10);
			int yOffset = heartRows * rowSpacing;

			if (health != maxHealth && health <= 4) {
				yOffset += rand.nextInt(2);
			}

			if (heart == regenCounter) {
				yOffset -= 2;
			}

			if (entity.isPotionActive(MobEffects.POISON)) {
				heartGui += 36;
			} else if (entity.isPotionActive(MobEffects.WITHER)) {
				heartGui += 72;
			}

			RenderHelper.drawTexturedModalRect(xOffset, yOffset, hurtResistant ? 25 : 16, 0, 9, 9);

			if (hurtResistant) {
				if (heart * 2 + 1 < lasthealth) {
					RenderHelper.drawTexturedModalRect(xOffset, yOffset, heartGui + 54, 0, 9, 9);
				}

				if (heart * 2 + 1 == lasthealth) {
					RenderHelper.drawTexturedModalRect(xOffset, yOffset, heartGui + 63, 0, 9, 9);
				}

			}

			float absorb = absorption;
			if (absorb > 0.0F) {
				if (absorb == absorption && absorption > 0.0F) {
					if (absorption % 2.0F == 1.0F) {
						RenderHelper.drawTexturedModalRect(xOffset, yOffset, heartGui + 153, 0, 9, 9);
					} else {
						RenderHelper.drawTexturedModalRect(xOffset, yOffset, heartGui + 144, 0, 9, 9);
					}

					absorb -= 2.0F;
				}
			} else {
				// Heart
				if (heart * 2 + 1 < health) {
					RenderHelper.drawTexturedModalRect(xOffset, yOffset, heartGui + 36, 0, 9, 9);
				}
				// Half Heart
				if (heart * 2 + 1 == health) {
					RenderHelper.drawTexturedModalRect(xOffset, yOffset, heartGui + 45, 0, 9, 9);
				}
			}
		}

	}

	public static void preDraw(RenderLivingEvent renderEntity) {
		EntityLivingBase entity = renderEntity.getEntity();

		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();

		GlStateManager.translate(renderEntity.getX(), renderEntity.getY(), renderEntity.getZ());

		GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);

		GlStateManager.translate(0, entity.height + 0.25, 0);

		GlStateManager.scale(0.02, 0.02, 0.02);
		GlStateManager.rotate(180, 0, 0, 1);
		GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);

		GlStateManager.disableDepth();
	}

	public static void postDraw(RenderLivingEvent renderEntity) {
		GlStateManager.popMatrix();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GlStateManager.color(1, 1, 1, 1);
	}

}
