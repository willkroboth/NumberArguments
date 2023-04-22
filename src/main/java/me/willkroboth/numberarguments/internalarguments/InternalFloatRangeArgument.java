package me.willkroboth.numberarguments.internalarguments;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.FloatRangeArgument;
import dev.jorel.commandapi.wrappers.FloatRange;
import me.willkroboth.configcommands.exceptions.CommandRunException;
import me.willkroboth.configcommands.functions.*;
import me.willkroboth.configcommands.internalarguments.CommandArgument;
import me.willkroboth.configcommands.internalarguments.InternalArgument;
import me.willkroboth.configcommands.internalarguments.InternalBooleanArgument;
import me.willkroboth.configcommands.internalarguments.InternalIntegerArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

public class InternalFloatRangeArgument extends InternalArgument implements CommandArgument {
    private FloatRange value;

    public InternalFloatRangeArgument() {
    }

    public InternalFloatRangeArgument(FloatRange value) {
        super(value);
    }

    @Override
    public Argument<?> createArgument(String name, @Nullable Object argumentInfo, boolean localDebug) {
        return new FloatRangeArgument(name);
    }

    @Override
    public boolean editArgumentInfo(CommandSender sender, String message, ConfigurationSection argument, @Nullable Object argumentInfo) {
        sender.sendMessage("There are no options to configure for a FloatRangeArgument");
        return true;
    }

    @Override
    public String[] formatArgumentInfo(Object argumentInfo) {
        return new String[]{"There are no options to configure for a FloatRangeArgument"};
    }

    @Override
    public void setValue(Object arg) {
        value = (FloatRange) arg;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(InternalArgument arg) {
        value = (FloatRange) arg.getValue();
    }

    @Override
    public String forCommand() {
        return value.toString();
    }

    private FloatRange getFloatRange(InternalArgument target) {
        return (FloatRange) target.getValue();
    }

    @Override
    public InstanceFunctionList getInstanceFunctions() {
        return merge(super.getInstanceFunctions(),
                functions(
                        new InstanceFunction("getUpperBound")
                                .returns(InternalFloatArgument.class, "The upper bound set for this FloatRange")
                                .executes((target, parameters) -> {
                                    return new InternalFloatArgument(getFloatRange(target).getUpperBound());
                                }),
                        new InstanceFunction("setUpperBound")
                                .withDescription("Sets the upper bound of this FloatRange to the given float")
                                .withParameters(new Parameter(InternalFloatArgument.class, "upperBound", "The new upper bound"))
                                .executes((target, parameters) -> {
                                    float low = getFloatRange(target).getLowerBound();
                                    float high = (float) parameters.get(0).getValue();

                                    FloatRange newRange = new FloatRange(low, high);
                                    target.setValue(newRange);
                                }),
                        new InstanceFunction("getLowerBound")
                                .returns(InternalFloatArgument.class, "The lower bound set for this FloatRange")
                                .executes((target, parameters) -> {
                                    return new InternalFloatArgument(getFloatRange(target).getLowerBound());
                                }),
                        new InstanceFunction("setLowerBound")
                                .withDescription("Sets the lower bound of this FloatRange to the given float")
                                .withParameters(new Parameter(InternalFloatArgument.class, "lowerBound", "The new lower bound"))
                                .executes((target, parameters) -> {
                                    float high = getFloatRange(target).getUpperBound();
                                    float low = (float) parameters.get(0).getValue();

                                    FloatRange newRange = new FloatRange(low, high);
                                    target.setValue(newRange);
                                }),
                        new InstanceFunction("isInRange")
                                .withDescription("Checks if the given number is within the range defined by this FloatRange, inclusively")
                                .withParameters(new Parameter(InternalDoubleArgument.class, "n", "The double to check"))
                                .withParameters(new Parameter(InternalFloatArgument.class, "n", "The float to check"))
                                .withParameters(new Parameter(InternalIntegerArgument.class, "n", "The integer to check"))
                                .withParameters(new Parameter(InternalLongArgument.class, "n", "The long to check"))
                                .returns(InternalBooleanArgument.class, "True if the given number is within this range, and false otherwise")
                                .executes((target, parameters) -> {
                                    return new InternalBooleanArgument(getFloatRange(target).isInRange((float) parameters.get(0).getValue()));
                                })
                                .withExamples(
                                        "<range> = FloatRange.(Float.(\"1\"), Float.(\"5\"))",
                                        "do <range>.isInRange(Float.(\"3\")) -> True",
                                        "do <range>.isInRange(Float.(\"10\")) -> False",
                                        "do <range>.isInRange(Float.(\"1\")) -> True",
                                        "do <range>.isInRange(Float.(\"5\")) -> True"
                                )
                )
        );
    }

    @Override
    public StaticFunctionList getStaticFunctions() {
        return merge(super.getStaticFunctions(),
                functions(
                        new StaticFunction("new")
                                .withAliases("")
                                .withParameters(
                                        new Parameter(InternalFloatArgument.class, "lowerBound"),
                                        new Parameter(InternalFloatArgument.class, "upperBound")
                                )
                                .returns(InternalFloatRangeArgument.class, "A new FloatRange with the given lower and upper bounds.")
                                .throwsException("Exception if the lower bound is greater than the upper bound")
                                .executes((parameters) -> {
                                    float low = (float) parameters.get(0).getValue();
                                    float high = (float) parameters.get(1).getValue();
                                    if (high < low)
                                        throw new CommandRunException("Low value (" + low + ") must be greater than or equal to high value (" + high + ")");

                                    return new InternalFloatRangeArgument(new FloatRange(low, high));
                                })
                                .withExamples(
                                        "FloatRange.new(Float.(\"1\"), Float.(\"5\")) -> [1, 5]",
                                        "FloatRange.(Float.(\"1.41\"), Float.(\"3.14\")) -> [1.41, 3.14]",
                                        "FloatRange.(Float.(\"3.14\"), Float.(\"1.41\")) -> Exception"
                                ),
                        new StaticFunction("newGreaterThanOrEqual")
                                .withDescription("Creates a new FloatRange that contains all numbers greater than or equal to a given lower bound")
                                .withParameters(new Parameter(InternalFloatRangeArgument.class, "lowerBound"))
                                .returns(InternalFloatRangeArgument.class, "A new FloatRange with the given lower bound, and infinity as the upper bound")
                                .executes((parameters) -> {
                                    float min = (float) parameters.get(0).getValue();
                                    return new InternalFloatRangeArgument(FloatRange.floatRangeGreaterThanOrEq(min));
                                })
                                .withExamples(
                                        "FloatRange.newGreaterThanOrEqual(Float.(\"3.14\")) -> [3.14, inf.]",
                                        "FloatRange.newGreaterThanOrEqual(Float.(\"100\")) -> [100, inf.]"
                                ),
                        new StaticFunction("newLessThanOrEqual")
                                .withDescription("Creates a new FloatRange that contains all numbers less than or equal to a given upper bound")
                                .withParameters(new Parameter(InternalFloatRangeArgument.class, "upperBound"))
                                .returns(InternalFloatRangeArgument.class, "A new FloatRange with the given upper bound, and negative infinity as the lower bound")
                                .executes((parameters) -> {
                                    float max = (float) parameters.get(0).getValue();

                                    return new InternalFloatRangeArgument(FloatRange.floatRangeLessThanOrEq(max));
                                })
                                .withExamples(
                                        "FloatRange.newLessThanOrEqual(Float.(\"3.14\")) -> [-inf., 3.14]",
                                        "FloatRange.newLessThanOrEqual(Float.(\"100\")) -> [-inf., 100]"
                                )
                )
        );
    }
}
