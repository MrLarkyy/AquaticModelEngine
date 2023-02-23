package xyz.larkyy.aquaticmodelengine.commands.impl;

import org.bukkit.command.CommandSender;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.commands.ICommand;

public class ReloadCommand implements ICommand {
    @Override
    public void run(CommandSender sender, String[] args) {
        if (!sender.hasPermission("aquaticmodelengine.reload")) {
            return;
        }
        AquaticModelEngine.getInstance().getModelGenerator().generateModels();
        sender.sendMessage("Engine has been reloaded!");

    }
}
