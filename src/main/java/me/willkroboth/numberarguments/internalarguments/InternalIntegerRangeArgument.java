package me.willkroboth.numberarguments.internalarguments;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.IntegerRangeArgument;
import dev.jorel.commandapi.wrappers.IntegerRange;
import me.willkroboth.configcommands.exceptions.CommandRunException;
import me.willkroboth.configcommands.functions.*;
import me.willkroboth.configcommands.internalarguments.CommandArgument;
import me.willkroboth.configcommands.internalarguments.InternalArgument;
import me.willkroboth.configcommands.internalarguments.InternalBooleanArgument;
import me.willkroboth.configcommands.internalarguments.InternalIntegerArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

public class InternalIntegerRangeArgument extends InternalArgument implements CommandArgument {
    private IntegerRange value;

    public InternalIntegerRangeArgument() {
    }

    public InternalIntegerRangeArgument(IntegerRange value) {
        super(value);
    }

    @Override
    public Argument<?> createArgument(String name, @Nullable Object argumentInfo, boolean localDebug) {
        return new IntegerRangeArgument(name);
    }

    @Override
    public boolean editArgumentInfo(CommandSender sender, String message, ConfigurationSection argument, @Nullable Object argumentInfo) {
        sender.sendMessage("There are no options to configure for an IntegerRangeArgument");
        return true;
    }

    @Override
    public String[] formatArgumentInfo(Object argumentInfo) {
        return new String[]{"There are no options to configure for an IntegerRangeArgument"};
    }

    @Override
    public void setValue(Object arg) {
        value = (IntegerRange) arg;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(InternalArgument arg) {
        value = (IntegerRange) arg.getValue();
    }

    @Override
    public String forCommand() {
        return value.toString();
    }

    private IntegerRange getIntegerRange(InternalArgument target) {
        return (IntegerRange) target.getValue();
    }

    @Override
    public InstanceFunctionList getInstanceFunctions() {
        return merge(super.getInstanceFunctions(),
                functions(
                        new InstanceFunction("getUpperBound")
                                .returns(InternalIntegerArgument.class, "The upper bound set for this IntegerRange")
                                .executes((target, parameters) -> {
                                    return new InternalIntegerArgument(getIntegerRange(target).getUpperBound());
                                }),
                        new InstanceFunction("setUpperBound")
                                .withDescription("Sets the upper bound of this IntegerRange to the given int")
                                .withParameters(new Parameter(InternalIntegerArgument.class, "upperBound", "The new upper bound"))
                                .executes((target, parameters) -> {
                                    int low = getIntegerRange(target).getLowerBound();
                                    int high = (int) parameters.get(0).getValue();

                                    IntegerRange newRange = new IntegerRange(low, high);
                                    target.setValue(newRange);
                                }),
                        new InstanceFunction("getLowerBound")
                                .returns(InternalIntegerArgument.class, "The lower bound set for this IntegerRange")
                                .executes((target, parameters) -> {
                                    return new InternalIntegerArgument(getIntegerRange(target).getLowerBound());
                                }),
                        new InstanceFunction("setLowerBound")
                                .withDescription("Sets the lower bound of this IntegerRange to the given int")
                                .withParameters(new Parameter(InternalIntegerArgument.class, "lowerBound", "The new lower bound"))
                                .executes((target, parameters) -> {
                                    int high = getIntegerRange(target).getUpperBound();
                                    int low = (int) parameters.get(0).getValue();

                                    IntegerRange newRange = new IntegerRange(low, high);
                                    target.setValue(newRange);
                                }),
                        new InstanceFunction("isInRange")
                                .withDescription("Checks if the given number is within the range defined by this IntegerRange, inclusively")
                                .withParameters(new Parameter(InternalDoubleArgument.class, "n", "The double to check"))
                                .withParameters(new Parameter(InternalFloatArgument.class, "n", "The float to check"))
                                .withParameters(new Parameter(InternalIntegerArgument.class, "n", "The integer to check"))
                                .withParameters(new Parameter(InternalLongArgument.class, "n", "The long to check"))
                                .returns(InternalBooleanArgument.class, "True if the given number is within this range, and false otherwise")
                                .executes((target, parameters) -> {
                                    return new InternalBooleanArgument(getIntegerRange(target).isInRange((int) parameters.get(0).getValue()));
                                })
                                .withExamples(
                                        "<range> = IntegerRange.(Integer.(\"1\"), Integer.(\"5\"))",
                                        "do <range>.isInRange(Integer.(\"3\")) -> True",
                                        "do <range>.isInRange(Integer.(\"10\")) -> False",
                                        "do <range>.isInRange(Integer.(\"1\")) -> True",
                                        "do <range>.isInRange(Integer.(\"5\")) -> True"
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
                                        new Parameter(InternalIntegerArgument.class, "lowerBound"),
                                        new Parameter(InternalIntegerArgument.class, "upperBound")
                                )
                                .returns(InternalIntegerRangeArgument.class, "A new IntegerRange with the given lower and upper bounds.")
                                .throwsException("Exception if the lower bound is greater than the upper bound")
                                .executes((parameters) -> {
                                    int low = (int) parameters.get(0).getValue();
                                    int high = (int) parameters.get(1).getValue();
                                    if (high < low)
                                        throw new CommandRunException("Low value (" + low + ") must be greater than or equal to high value (" + high + ")");

                                    return new InternalIntegerRangeArgument(new IntegerRange(low, high));
                                })
                                .withExamples(
                                        "IntegerRange.new(Integer.(\"1\"), Integer.(\"5\")) -> [1, 5]",
                                        "IntegerRange.(Integer.(\"5\"), Integer.(\"1\")) -> Exception"
                                ),
                        new StaticFunction("newGreaterThanOrEqual")
                                .withDescription("Creates a new IntegerRange that contains all numbers greater than or equal to a given lower bound")
                                .withParameters(new Parameter(InternalIntegerRangeArgument.class, "lowerBound"))
                                .returns(InternalIntegerRangeArgument.class, "A new IntegerRange with the given lower bound, and infinity as the upper bound")
                                .executes((parameters) -> {
                                    int min = (int) parameters.get(0).getValue();
                                    return new InternalIntegerRangeArgument(IntegerRange.integerRangeGreaterThanOrEq(min));
                                })
                                .withExamples(
                                        "IntegerRange.newGreaterThanOrEqual(Integer.(\"3\")) -> [3, inf.]",
                                        "IntegerRange.newGreaterThanOrEqual(Integer.(\"100\")) -> [100, inf.]"
                                ),
                        new StaticFunction("newLessThanOrEqual")
                                .withDescription("Creates a new IntegerRange that contains all numbers less than or equal to a given upper bound")
                                .withParameters(new Parameter(InternalIntegerRangeArgument.class, "upperBound"))
                                .returns(InternalIntegerRangeArgument.class, "A new IntegerRange with the given upper bound, and negative infinity as the lower bound")
                                .executes((parameters) -> {
                                    int max = (int) parameters.get(0).getValue();

                                    return new InternalIntegerRangeArgument(IntegerRange.integerRangeLessThanOrEq(max));
                                })
                                .withExamples(
                                        "IntegerRange.newLessThanOrEqual(Integer.(\"3\")) -> [-inf., 3]",
                                        "IntegerRange.newLessThanOrEqual(Integer.(\"100\")) -> [-inf., 100]"
                                )
                )
        );
    }
}
