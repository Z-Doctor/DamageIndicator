package zdoctor.damageindicator.client.render;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Ordering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import zdoctor.damageindicator.client.RenderHelper;

public class HealthRenderer {

	public static void renderHealth(RenderLivingEvent renderEntity) {
		EntityLivingBase entity = renderEntity.getEntity();
		if(entity.isInvisible())
			return;
		
		preDraw(renderEntity);

		drawHealthString(renderEntity);

		drawActivePotionEffects(renderEntity);

		drawHealth(renderEntity);

		postDraw(renderEntity);

	}

	protected static void drawHealthString(RenderLivingEvent renderEntity) {

	}

	protected static void drawActivePotionEffects(RenderLivingEvent renderEntity) {
		EntityLivingBase entity = renderEntity.getEntity();
		FontRenderer fontRenderer = renderEntity.getRenderer().getFontRendererFromRenderManager();

		IAttributeInstance attributes = entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		float maxHealth = (float) attributes.getAttributeValue();
		float absorption = entity.getAbsorptionAmount();

		String status = String.format("%d/%d", MathHelper.ceil(entity.getHealth()),
				MathHelper.ceil(maxHealth + absorption));

		int width = fontRenderer.getStringWidth(status);

		fontRenderer.drawString(status, -width / 2, -fontRenderer.FONT_HEIGHT, 0);

		Collection<PotionEffect> collection = entity.getActivePotionEffects();
		if (!collection.isEmpty()) {

			GlStateManager.pushMatrix();
			GlStateManager.translate(collection.size() / 2 * -9, -fontRenderer.FONT_HEIGHT * 2, 0);
//			GlStateManager.translate(width / 2, -15 + fontRenderer.FONT_HEIGHT / 2, 0);
			GlStateManager.scale(0.5, 0.5, 0.5);
			RenderHelper.bindTexture(GuiContainer.INVENTORY_BACKGROUND);

			List<PotionEffect> potionEffects = Ordering.natural().sortedCopy(collection);
			for (int effect = 0; effect < potionEffects.size(); effect++) {
				PotionEffect potioneffect = potionEffects.get(effect);
				Potion potion = potioneffect.getPotion();
				if (!potion.shouldRender(potioneffect))
					continue;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//				RenderHelper.drawTexturedModalRect(0, 0, 0, 166, 140, 32);

				if (potion.hasStatusIcon()) {
					int i1 = potion.getStatusIconIndex();
					RenderHelper.drawTexturedModalRect(effect * 18, 0, 0 + i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
				}
			}
			GlStateManager.popMatrix();
		}

	}

	protected static void drawHealth(RenderLivingEvent renderEntity) {
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
//		float lasthealth = entity.getLastHealth();
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

//			if (hurtResistant) {
//				if (heart * 2 + 1 < lasthealth) {
//					RenderHelper.drawTexturedModalRect(xOffset, yOffset, heartGui + 54, 0, 9, 9);
//				}
//
//				if (heart * 2 + 1 == lasthealth) {
//					RenderHelper.drawTexturedModalRect(xOffset, yOffset, heartGui + 63, 0, 9, 9);
//				}
//
//			}

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

	protected static void preDraw(RenderLivingEvent renderEntity) {
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

	protected static void postDraw(RenderLivingEvent renderEntity) {
		GlStateManager.popMatrix();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GlStateManager.color(1, 1, 1, 1);
	}

}
