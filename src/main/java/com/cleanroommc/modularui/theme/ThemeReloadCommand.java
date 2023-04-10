package com.cleanroommc.modularui.theme;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class ThemeReloadCommand extends CommandBase {

    @Override
    public @NotNull String getCommandName() {
        return "reloadThemes";
    }

    @Override
    public @NotNull String getCommandUsage(@NotNull ICommandSender sender) {
        return "/reloadThemes";
    }

    @Override
    public void processCommand(@NotNull ICommandSender sender, String @NotNull [] args) {
        try {
            ThemeManager.reload();
            sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Successfully reloaded themes"));
        } catch (Exception e) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Error reloaded themes:"));
            sender.sendMessage(new TextComponentString(TextFormatting.RED + e.getMessage()));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
