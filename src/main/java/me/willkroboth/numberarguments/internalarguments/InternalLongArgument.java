package me.willkroboth.numberarguments.internalarguments;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LongArgument;
import me.willkroboth.configcommands.ConfigCommandsHandler;
import me.willkroboth.configcommands.exceptions.CommandRunException;
import me.willkroboth.configcommands.exceptions.IncorrectArgumentKey;
import me.willkroboth.configcommands.functions.InstanceFunctionList;
import me.willkroboth.configcommands.functions.StaticFunctionList;
import me.willkroboth.configcommands.internalarguments.CommandArgument;
import me.willkroboth.configcommands.internalarguments.InternalArgument;
import me.willkroboth.configcommands.internalarguments.InternalBooleanArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InternalLongArgument extends InternalArgument implements NumberFunctions, CommandArgument {
    private long value;

    public InternalLongArgument() {
    }

    public InternalLongArgument(long value) {
        super(value);
    }

    @Override
    public Argument<?> createArgument(String name, @Nullable Object argumentInfo, boolean localDebug) throws IncorrectArgumentKey {
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        if (argumentInfo != null) {
            ConfigurationSection info = assertArgumentInfoClass(argumentInfo, ConfigurationSection.class, name);
            min = info.getLong("min", Long.MAX_VALUE);
            max = info.getLong("max", Long.MIN_VALUE);
        }
        ConfigCommandsHandler.logDebug(localDebug, "Arg has %s, %s",
                min == Long.MAX_VALUE ? "no min" : "min: " + min,
                max == Long.MIN_VALUE ? "no max" : "max: " + max
        );
        return new LongArgument(name, min, max);
    }

    @Override
    public boolean editArgumentInfo(CommandSender sender, String message, ConfigurationSection argument, @Nullable Object argumentInfo) {
        ConfigurationSection info;
        if (argumentInfo == null) {
            info = argument.createSection("argumentInfo");
        } else {
            try {
                info = assertArgumentInfoClass(argumentInfo, ConfigurationSection.class, "");
            } catch (IncorrectArgumentKey ignored) {
                argument.set("argumentInfo", null);
                info = argument.createSection("argumentInfo");
            }
        }

        long min = info.getLong("min", Long.MAX_VALUE);
        long max = info.getLong("max", Long.MIN_VALUE);
        if (message.isBlank()) {
            if (min == Long.MAX_VALUE) {
                sender.sendMessage("There is no min value");
            } else {
                sender.sendMessage("min is currently " + min);
            }
            if (max == Long.MIN_VALUE) {
                sender.sendMessage("There is no max value");
            } else {
                sender.sendMessage("max is currently " + max);
            }
            sender.sendMessage("Type the key to change and the value you want to change it to");
            sender.sendMessage("Eg. \"min 0\" or \"max 10\"");
        } else if (message.startsWith("min ")) {
            String value = message.substring(4);
            long newMin;
            try {
                newMin = Long.parseLong(value);
            } catch (NumberFormatException ignored) {
                sender.sendMessage("\"" + value + "\" cannot be interpreted as an Long");
                return false;
            }
            if (newMin > max) {
                sender.sendMessage("min (" + newMin + ") cannot be larger than the max (" + max + ")");
                return false;
            }
            info.set("min", newMin);
            ConfigCommandsHandler.saveConfigFile();
            sender.sendMessage("min is now " + newMin);
        } else if (message.startsWith("max ")) {
            String value = message.substring(4);
            long newMax;
            try {
                newMax = Long.parseLong(value);
            } catch (NumberFormatException ignored) {
                sender.sendMessage("\"" + value + "\" cannot be interpreted as an Long");
                return false;
            }
            if (newMax < min) {
                sender.sendMessage("max (" + newMax + ") cannot be smaller than the min (" + min + ")");
                return false;
            }
            info.set("max", newMax);
            ConfigCommandsHandler.saveConfigFile();
            sender.sendMessage("max is now " + newMax);
        } else {
            sender.sendMessage("Sorry, I don't understand that message...");
        }
        return false;
    }

    @Override
    public String[] formatArgumentInfo(Object argumentInfo) {
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        if (argumentInfo != null) {
            try {
                ConfigurationSection info = assertArgumentInfoClass(argumentInfo, ConfigurationSection.class, "");
                min = info.getLong("min", Long.MAX_VALUE);
                max = info.getLong("max", Long.MIN_VALUE);
            } catch (IncorrectArgumentKey ignored) {
            }
        }
        String[] out = new String[2];
        if (min == Long.MIN_VALUE) {
            out[0] = "There is no min";
        } else {
            out[0] = "min: " + min;
        }
        if (max == Long.MAX_VALUE) {
            out[1] = "There is no max";
        } else {
            out[1] = "max: " + max;
        }
        return out;
    }

    @Override
    public void setValue(Object arg) {
        value = (long) arg;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(InternalArgument arg) {
        value = (long) arg.getValue();
    }

    @Override
    public String forCommand() {
        return String.valueOf(value);
    }

    @Override
    public InstanceFunctionList getInstanceFunctions() {
        return merge(super.getInstanceFunctions(),
                generateFunctions()
        );
    }

    @Override
    public InternalBooleanArgument lessThan(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((long) target.getValue() < (long) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument lessThanOrEqual(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((long) target.getValue() <= (long) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument greaterThan(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((long) target.getValue() > (long) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument greaterThanOrEqual(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((long) target.getValue() >= (long) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument equalTo(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((long) target.getValue() == (long) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument notEqualTo(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((long) target.getValue() != (long) arguments.get(0).getValue());
    }

    @Override
    public InternalLongArgument add(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalLongArgument((long) target.getValue() + (long) arguments.get(0).getValue());
    }

    @Override
    public InternalLongArgument subtract(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalLongArgument((long) target.getValue() - (long) arguments.get(0).getValue());
    }

    @Override
    public InternalLongArgument multiply(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalLongArgument((long) target.getValue() * (long) arguments.get(0).getValue());
    }

    @Override
    public InternalLongArgument divide(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalLongArgument((long) target.getValue() / (long) arguments.get(0).getValue());
    }

    @Override
    public StaticFunctionList getStaticFunctions() {
        return merge(super.getStaticFunctions(),
                generateStaticFunctions()
        );
    }

    @Override
    public InternalLongArgument maxValue(List<InternalArgument> parameters) {
        return new InternalLongArgument(Long.MAX_VALUE);
    }

    @Override
    public InternalLongArgument minValue(List<InternalArgument> parameters) {
        return new InternalLongArgument(Long.MIN_VALUE);
    }

    @Override
    public InternalLongArgument initialize(List<InternalArgument> parameters) {
        long result = 0;
        if (parameters.size() == 1) {
            try {
                result = Long.parseLong((String) parameters.get(0).getValue());
            } catch (NumberFormatException e) {
                throw new CommandRunException(e);
            }
        }

        return new InternalLongArgument(result);
    }
}
