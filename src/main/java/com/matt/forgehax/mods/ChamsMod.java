package com.matt.forgehax.mods;

import com.matt.forgehax.util.EntityUtils;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class ChamsMod extends ToggleMod {
    public Property players;
    public Property hostileMobs;
    public Property friendlyMobs;

    public ChamsMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    public boolean shouldDraw(EntityLivingBase entity) {
        return !entity.equals(MC.thePlayer) &&
                !entity.isDead && (
                        (hostileMobs.getBoolean() && EntityUtils.isHostileMob(entity)) || // check this first
                        (players.getBoolean() && EntityUtils.isPlayer(entity)) ||
                        (friendlyMobs.getBoolean() && EntityUtils.isFriendlyMob(entity))
        );
    }

    @Override
    public void loadConfig(Configuration configuration) {
        addSettings(
                players = configuration.get(getModName(),
                        "players",
                        true,
                        "Enables player chams"),
                hostileMobs = configuration.get(getModName(),
                        "hostile mobs",
                        true,
                        "Enables hostile mob chams"),
                friendlyMobs = configuration.get(getModName(),
                        "friendly mobs",
                        true,
                        "Enables friendly mob chams")
        );
    }

    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre event) {
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0F, -1000000);
    }

    @SubscribeEvent
    public void onPostRenderLiving(RenderLivingEvent.Post event) {
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        GlStateManager.doPolygonOffset(1.0F, 1000000);
        GlStateManager.disablePolygonOffset();
    }
}
