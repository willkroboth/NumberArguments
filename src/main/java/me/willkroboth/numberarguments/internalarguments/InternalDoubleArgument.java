package me.willkroboth.numberarguments.internalarguments;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.DoubleArgument;
import me.willkroboth.configcommands.ConfigCommandsHandler;
import me.willkroboth.configcommands.exceptions.CommandRunException;
import me.willkroboth.configcommands.exceptions.IncorrectArgumentKey;
import me.willkroboth.configcommands.functions.InstanceFunctionList;
import me.willkroboth.configcommands.functions.StaticFunctionList;
import me.willkroboth.configcommands.internalarguments.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InternalDoubleArgument extends InternalArgument implements NumberFunctions, CommandArgument {
    private double value;

    public InternalDoubleArgument() {
    }

    public InternalDoubleArgument(double value) {
        super(value);
    }

    @Override
    public Argument<?> createArgument(String name, @Nullable Object argumentInfo, boolean localDebug) throws IncorrectArgumentKey {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        if (argumentInfo != null) {
            ConfigurationSection info = assertArgumentInfoClass(argumentInfo, ConfigurationSection.class, name);
            min = info.getDouble("min", Double.POSITIVE_INFINITY);
            max = info.getDouble("max", Double.NEGATIVE_INFINITY);
        }
        ConfigCommandsHandler.logDebug(localDebug, "Arg has %s, %s",
                min == Double.POSITIVE_INFINITY ? "no min" : "min: " + min,
                max == Double.NEGATIVE_INFINITY ? "no max" : "max: " + max
        );
        return new DoubleArgument(name, min, max);
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

        double min = info.getDouble("min", Double.POSITIVE_INFINITY);
        double max = info.getDouble("max", Double.NEGATIVE_INFINITY);
        if (message.isBlank()) {
            if (min == Double.POSITIVE_INFINITY) {
                sender.sendMessage("There is no min value");
            } else {
                sender.sendMessage("min is currently " + min);
            }
            if (max == Double.NEGATIVE_INFINITY) {
                sender.sendMessage("There is no max value");
            } else {
                sender.sendMessage("max is currently " + max);
            }
            sender.sendMessage("Type the key to change and the value you want to change it to");
            sender.sendMessage("Eg. \"min 0\" or \"max 10\"");
        } else if (message.startsWith("min ")) {
            String value = message.substring(4);
            double newMin;
            try {
                newMin = Double.parseDouble(value);
            } catch (NumberFormatException ignored) {
                sender.sendMessage("\"" + value + "\" cannot be interpreted as an Double");
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
            double newMax;
            try {
                newMax = Double.parseDouble(value);
            } catch (NumberFormatException ignored) {
                sender.sendMessage("\"" + value + "\" cannot be interpreted as an Double");
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
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        if (argumentInfo != null) {
            try {
                ConfigurationSection info = assertArgumentInfoClass(argumentInfo, ConfigurationSection.class, "");
                min = info.getDouble("min", Double.POSITIVE_INFINITY);
                max = info.getDouble("max", Double.NEGATIVE_INFINITY);
            } catch (IncorrectArgumentKey ignored) {
            }
        }
        String[] out = new String[2];
        if (min == Double.NEGATIVE_INFINITY) {
            out[0] = "There is no min";
        } else {
            out[0] = "min: " + min;
        }
        if (max == Double.POSITIVE_INFINITY) {
            out[1] = "There is no max";
        } else {
            out[1] = "max: " + max;
        }
        return out;
    }

    @Override
    public void setValue(Object arg) {
        value = (double) arg;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(InternalArgument arg) {
        value = (double) arg.getValue();
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
        return new InternalBooleanArgument((double) target.getValue() < (double) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument lessThanOrEqual(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((double) target.getValue() <= (double) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument greaterThan(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((double) target.getValue() > (double) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument greaterThanOrEqual(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((double) target.getValue() >= (double) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument equalTo(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((double) target.getValue() == (double) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument notEqualTo(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((double) target.getValue() != (double) arguments.get(0).getValue());
    }

    @Override
    public InternalDoubleArgument add(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalDoubleArgument((double) target.getValue() + (double) arguments.get(0).getValue());
    }

    @Override
    public InternalDoubleArgument subtract(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalDoubleArgument((double) target.getValue() - (double) arguments.get(0).getValue());
    }

    @Override
    public InternalDoubleArgument multiply(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalDoubleArgument((double) target.getValue() * (double) arguments.get(0).getValue());
    }

    @Override
    public InternalDoubleArgument divide(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalDoubleArgument((double) target.getValue() / (double) arguments.get(0).getValue());
    }

    @Override
    public StaticFunctionList getStaticFunctions() {
        return merge(super.getStaticFunctions(),
                generateStaticFunctions()
        );
    }

    @Override
    public InternalDoubleArgument maxValue(List<InternalArgument> parameters) {
        return new InternalDoubleArgument(Double.POSITIVE_INFINITY);
    }

    @Override
    public InternalDoubleArgument minValue(List<InternalArgument> parameters) {
        return new InternalDoubleArgument(Double.NEGATIVE_INFINITY);
    }

    @Override
    public InternalDoubleArgument initialize(List<InternalArgument> parameters) {
        double result = 0;
        if (parameters.size() == 1) {
            try {
                result = Double.parseDouble((String) parameters.get(0).getValue());
            } catch (NumberFormatException e) {
                throw new CommandRunException(e);
            }
        }

        return new InternalDoubleArgument(result);
    }
}
