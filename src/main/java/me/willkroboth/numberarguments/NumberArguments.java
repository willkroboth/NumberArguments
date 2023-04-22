package me.willkroboth.numberarguments;

import me.willkroboth.configcommands.internalarguments.InternalArgument;
import org.bukkit.plugin.java.JavaPlugin;

public class NumberArguments extends JavaPlugin {
    @Override
    public void onLoad() {
        InternalArgument.registerFromJavaPlugin(this, "me.willkroboth.numberarguments.internalarguments");
    }
}
