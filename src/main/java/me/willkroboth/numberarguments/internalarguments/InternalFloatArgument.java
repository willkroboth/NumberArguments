package me.willkroboth.numberarguments.internalarguments;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.FloatArgument;
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

public class InternalFloatArgument extends InternalArgument implements NumberFunctions, CommandArgument {
    private float value;

    public InternalFloatArgument() {
    }

    public InternalFloatArgument(float value) {
        super(value);
    }

    @Override
    public Argument<?> createArgument(String name, @Nullable Object argumentInfo, boolean localDebug) throws IncorrectArgumentKey {
        float min = Float.POSITIVE_INFINITY;
        float max = Float.NEGATIVE_INFINITY;
        if (argumentInfo != null) {
            ConfigurationSection info = assertArgumentInfoClass(argumentInfo, ConfigurationSection.class, name);
            // For some reason ConfigurationSection#getFloat doesn't exist, so we have to take the double
            min = (float) info.getDouble("min", Float.POSITIVE_INFINITY);
            max = (float) info.getDouble("max", Float.NEGATIVE_INFINITY);
        }
        ConfigCommandsHandler.logDebug(localDebug, "Arg has %s, %s",
                min == Float.POSITIVE_INFINITY ? "no min" : "min: " + min,
                max == Float.NEGATIVE_INFINITY ? "no max" : "max: " + max
        );
        return new FloatArgument(name, min, max);
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

        float min = (float) info.getDouble("min", Float.POSITIVE_INFINITY);
        float max = (float) info.getDouble("max", Float.NEGATIVE_INFINITY);
        if (message.isBlank()) {
            if (min == Float.POSITIVE_INFINITY) {
                sender.sendMessage("There is no min value");
            } else {
                sender.sendMessage("min is currently " + min);
            }
            if (max == Float.NEGATIVE_INFINITY) {
                sender.sendMessage("There is no max value");
            } else {
                sender.sendMessage("max is currently " + max);
            }
            sender.sendMessage("Type the key to change and the value you want to change it to");
            sender.sendMessage("Eg. \"min 0\" or \"max 10\"");
        } else if (message.startsWith("min ")) {
            String value = message.substring(4);
            float newMin;
            try {
                newMin = Float.parseFloat(value);
            } catch (NumberFormatException ignored) {
                sender.sendMessage("\"" + value + "\" cannot be interpreted as an Float");
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
            float newMax;
            try {
                newMax = Float.parseFloat(value);
            } catch (NumberFormatException ignored) {
                sender.sendMessage("\"" + value + "\" cannot be interpreted as an Float");
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
        float min = Float.POSITIVE_INFINITY;
        float max = Float.NEGATIVE_INFINITY;
        if (argumentInfo != null) {
            try {
                ConfigurationSection info = assertArgumentInfoClass(argumentInfo, ConfigurationSection.class, "");
                min = (float) info.getDouble("min", Float.POSITIVE_INFINITY);
                max = (float) info.getDouble("max", Float.NEGATIVE_INFINITY);
            } catch (IncorrectArgumentKey ignored) {
            }
        }
        String[] out = new String[2];
        if (min == Float.NEGATIVE_INFINITY) {
            out[0] = "There is no min";
        } else {
            out[0] = "min: " + min;
        }
        if (max == Float.POSITIVE_INFINITY) {
            out[1] = "There is no max";
        } else {
            out[1] = "max: " + max;
        }
        return out;
    }

    @Override
    public void setValue(Object arg) {
        value = (float) arg;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(InternalArgument arg) {
        value = (float) arg.getValue();
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
        return new InternalBooleanArgument((float) target.getValue() < (float) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument lessThanOrEqual(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((float) target.getValue() <= (float) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument greaterThan(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((float) target.getValue() > (float) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument greaterThanOrEqual(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((float) target.getValue() >= (float) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument equalTo(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((float) target.getValue() == (float) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument notEqualTo(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((float) target.getValue() != (float) arguments.get(0).getValue());
    }

    @Override
    public InternalFloatArgument add(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalFloatArgument((float) target.getValue() + (float) arguments.get(0).getValue());
    }

    @Override
    public InternalFloatArgument subtract(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalFloatArgument((float) target.getValue() - (float) arguments.get(0).getValue());
    }

    @Override
    public InternalFloatArgument multiply(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalFloatArgument((float) target.getValue() * (float) arguments.get(0).getValue());
    }

    @Override
    public InternalFloatArgument divide(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalFloatArgument((float) target.getValue() / (float) arguments.get(0).getValue());
    }

    @Override
    public StaticFunctionList getStaticFunctions() {
        return merge(super.getStaticFunctions(),
                generateStaticFunctions()
        );
    }

    @Override
    public InternalFloatArgument maxValue(List<InternalArgument> parameters) {
        return new InternalFloatArgument(Float.POSITIVE_INFINITY);
    }

    @Override
    public InternalFloatArgument minValue(List<InternalArgument> parameters) {
        return new InternalFloatArgument(Float.NEGATIVE_INFINITY);
    }

    @Override
    public InternalFloatArgument initialize(List<InternalArgument> parameters) {
        float result = 0;
        if (parameters.size() == 1) {
            try {
                result = Float.parseFloat((String) parameters.get(0).getValue());
            } catch (NumberFormatException e) {
                throw new CommandRunException(e);
            }
        }

        return new InternalFloatArgument(result);
    }
}
