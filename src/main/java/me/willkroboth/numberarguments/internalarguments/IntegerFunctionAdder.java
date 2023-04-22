package me.willkroboth.numberarguments.internalarguments;

import me.willkroboth.configcommands.functions.InstanceFunctionList;
import me.willkroboth.configcommands.functions.StaticFunction;
import me.willkroboth.configcommands.functions.StaticFunctionList;
import me.willkroboth.configcommands.internalarguments.FunctionAdder;
import me.willkroboth.configcommands.internalarguments.InternalArgument;
import me.willkroboth.configcommands.internalarguments.InternalBooleanArgument;
import me.willkroboth.configcommands.internalarguments.InternalIntegerArgument;

import java.util.List;

public class IntegerFunctionAdder extends FunctionAdder implements NumberFunctions {
    @Override
    public Class<? extends InternalArgument> getClassToAddTo() {
        return InternalIntegerArgument.class;
    }

    @Override
    public InstanceFunctionList getAddedFunctions() {
        return generateFunctions();
    }

    @Override
    public InternalBooleanArgument lessThan(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((int) target.getValue() < (int) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument lessThanOrEqual(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((int) target.getValue() <= (int) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument greaterThan(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((int) target.getValue() > (int) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument greaterThanOrEqual(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((int) target.getValue() >= (int) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument equalTo(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((int) target.getValue() == (int) arguments.get(0).getValue());
    }

    @Override
    public InternalBooleanArgument notEqualTo(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalBooleanArgument((int) target.getValue() != (int) arguments.get(0).getValue());
    }

    @Override
    public InternalIntegerArgument add(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalIntegerArgument((int) target.getValue() + (int) arguments.get(0).getValue());
    }

    @Override
    public InternalIntegerArgument subtract(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalIntegerArgument((int) target.getValue() - (int) arguments.get(0).getValue());
    }

    @Override
    public InternalIntegerArgument multiply(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalIntegerArgument((int) target.getValue() * (int) arguments.get(0).getValue());
    }

    @Override
    public InternalIntegerArgument divide(InternalArgument target, List<InternalArgument> arguments) {
        return new InternalIntegerArgument((int) target.getValue() / (int) arguments.get(0).getValue());
    }

    @Override
    public StaticFunctionList getAddedStaticFunctions() {
        return functions(
                new StaticFunction("maxValue")
                        .returns(myClass(), "The maximum value representable by this number")
                        .executes(this::maxValue),
                new StaticFunction("minValue")
                        .returns(myClass(), "The minimum value representable by this number")
                        .executes(this::minValue)
        );
    }

    @Override
    public InternalIntegerArgument maxValue(List<InternalArgument> parameters) {
        return new InternalIntegerArgument(Integer.MAX_VALUE);
    }

    @Override
    public InternalIntegerArgument minValue(List<InternalArgument> parameters) {
        return new InternalIntegerArgument(Integer.MIN_VALUE);
    }

    @Override
    public InternalArgument initialize(List<InternalArgument> parameters) {
        // This shouldn't be called, since InternalIntegerArgument already has the initialize function in the base plugin
        throw new UnsupportedOperationException("IntegerFunctionAdder#initialize is not supposed to be used.");
    }
}
