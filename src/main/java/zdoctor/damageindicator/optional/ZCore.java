package zdoctor.damageindicator.optional;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Ordering;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.RenderLivingEvent;
import zdoctor.damageindicator.ModMain;
import zdoctor.damageindicator.client.RenderHelper;
import zdoctor.zcoremod.helpers.EntityHelper;

public class ZCore {

	public static void renderLastHealth(EntityLivingBase entity, boolean hurtResistant, int heart, int xOffset,
			int yOffset, int heartGui) {
		ModMain.proxy.zcore.ifPresent(zcore -> {
			float lasthealth = EntityHelper.getLastHealth(entity);
			if (hurtResistant) {
				if (heart * 2 + 1 < lasthealth) {
					RenderHelper.drawTexturedModalRect(xOffset, yOffset, heartGui + 54, 0, 9, 9);
				}

				if (heart * 2 + 1 == lasthealth) {
					RenderHelper.drawTexturedModalRect(xOffset, yOffset, heartGui + 63, 0, 9, 9);
				}

			}
		});
	}

	public static void drawActivePotionEffects(EntityLivingBase entity, FontRenderer fontRenderer) {
		ModMain.proxy.zcore.ifPresent(zcore -> {
			Collection<PotionEffect> collection = entity.getActivePotionEffects();
			if (!collection.isEmpty()) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(collection.size() / 2 * -9 - (collection.size() % 2) * 5,
						-fontRenderer.FONT_HEIGHT * 2, 0);
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
		});
	}

}
