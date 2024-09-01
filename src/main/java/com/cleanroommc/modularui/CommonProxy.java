package com.cleanroommc.modularui;

import com.cleanroommc.modularui.factory.*;
import com.cleanroommc.modularui.holoui.HoloScreenEntity;
import com.cleanroommc.modularui.network.NetworkHandler;
import com.cleanroommc.modularui.test.ItemEditorGui;
import com.cleanroommc.modularui.test.TestBlock;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.Timer;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

    void preInit(FMLPreInitializationEvent event) {
        ModularUIConfig.init(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(new GuiManager());
        MinecraftForge.EVENT_BUS.register(new GuiManager());

        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);

        if (ModularUIConfig.enableTestGuis) {
            MinecraftForge.EVENT_BUS.register(TestBlock.class);
            TestBlock.preInit();
        }

        EntityRegistry.registerModEntity(HoloScreenEntity.class, "modular_screen", 0, ModularUI.INSTANCE, 0, 0, false);

        NetworkHandler.init();

        GuiFactories.init();
    }

    void postInit(FMLPostInitializationEvent event) {
    }

    void onServerLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new ItemEditorGui.Command());
    }

    @SideOnly(Side.CLIENT)
    public Timer getTimer60Fps() {
        throw new UnsupportedOperationException();
    }

    @SubscribeEvent
    public final void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(ModularUI.ID)) {
            ModularUIConfig.syncConfig();
        }
    }
}
