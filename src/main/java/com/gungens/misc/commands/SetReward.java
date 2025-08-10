package com.gungens.misc.commands;

import com.gungens.misc.libs.RegisterCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@RegisterCommand(name = "setreward")
public class SetReward implements org.bukkit.command.CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        return true;
    }
}
