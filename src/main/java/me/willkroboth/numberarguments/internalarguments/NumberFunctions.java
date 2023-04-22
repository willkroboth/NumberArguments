package me.willkroboth.numberarguments.internalarguments;

import me.willkroboth.configcommands.functions.*;
import me.willkroboth.configcommands.internalarguments.InternalArgument;
import me.willkroboth.configcommands.internalarguments.InternalBooleanArgument;
import me.willkroboth.configcommands.internalarguments.InternalIntegerArgument;
import me.willkroboth.configcommands.internalarguments.InternalStringArgument;

import java.util.List;

public interface NumberFunctions extends FunctionCreator {
    default InstanceFunctionList generateFunctions() {
        return functions(
                new InstanceFunction("lessThan")
                        .withAliases("<")
                        .withParameters(new Parameter(InternalDoubleArgument.class, "other", "The double to compare with"))
                        .withParameters(new Parameter(InternalFloatArgument.class, "other", "The float to compare with"))
                        .withParameters(new Parameter(InternalIntegerArgument.class, "other", "The integer to compare with"))
                        .withParameters(new Parameter(InternalLongArgument.class, "other", "The long to compare with"))
                        .returns(InternalBooleanArgument.class, "True if this number is less than the given number, and false otherwise")
                        .executes(this::lessThan)
                        .withExamples(
                                "do Integer.(\"1\").lessThan(Integer.(\"2\")) -> True",
                                "do Float.(\"3.5\").<(Long.(\"-10\")) -> False"
                        ),
                new InstanceFunction("lessThanOrEqualTo")
                        .withAliases("<=")
                        .withParameters(new Parameter(InternalDoubleArgument.class, "other", "The double to compare with"))
                        .withParameters(new Parameter(InternalFloatArgument.class, "other", "The float to compare with"))
                        .withParameters(new Parameter(InternalIntegerArgument.class, "other", "The integer to compare with"))
                        .withParameters(new Parameter(InternalLongArgument.class, "other", "The long to compare with"))
                        .returns(InternalBooleanArgument.class, "True if this number is less than or equal to the given number, and false otherwise")
                        .executes(this::lessThanOrEqual)
                        .withExamples(
                                "do Integer.(\"1\").lessThanOrEqualTo(Integer.(\"2\")) -> True",
                                "do Double.(\"10.8\").<=(Double.(\"10.8\")) -> True",
                                "do Float.(\"3.5\").<=(Long.(\"-10\")) -> False"
                        ),
                new InstanceFunction("greaterThan")
                        .withAliases(">")
                        .withParameters(new Parameter(InternalDoubleArgument.class, "other", "The double to compare with"))
                        .withParameters(new Parameter(InternalFloatArgument.class, "other", "The float to compare with"))
                        .withParameters(new Parameter(InternalIntegerArgument.class, "other", "The integer to compare with"))
                        .withParameters(new Parameter(InternalLongArgument.class, "other", "The long to compare with"))
                        .returns(InternalBooleanArgument.class, "True if this number is greater than the given number, and false otherwise")
                        .executes(this::greaterThan)
                        .withExamples(
                                "do Integer.(\"2\").greaterThan(Integer.(\"1\")) -> True",
                                "do Long.(\"-10\")).>(Float.(\"3.5\") -> False"
                        ),
                new InstanceFunction("greaterThanOrEqualTo")
                        .withAliases(">=")
                        .withParameters(new Parameter(InternalDoubleArgument.class, "other", "The double to compare with"))
                        .withParameters(new Parameter(InternalFloatArgument.class, "other", "The float to compare with"))
                        .withParameters(new Parameter(InternalIntegerArgument.class, "other", "The integer to compare with"))
                        .withParameters(new Parameter(InternalLongArgument.class, "other", "The long to compare with"))
                        .returns(InternalBooleanArgument.class, "True if this number is greater than or equal to the given number, and false otherwise")
                        .executes(this::greaterThanOrEqual)
                        .withExamples(
                                "do Integer.(\"2\").greaterThanOrEqualTo(Integer.(\"1\")) -> True",
                                "do Double.(\"10.8\").>=(Double.(\"10.8\")) -> True",
                                "do Long.(\"-10\").>=(Float.(\"3.5\")) -> False"
                        ),
                new InstanceFunction("equalTo")
                        .withAliases("==")
                        .withParameters(new Parameter(InternalDoubleArgument.class, "other", "The double to compare with"))
                        .withParameters(new Parameter(InternalFloatArgument.class, "other", "The float to compare with"))
                        .withParameters(new Parameter(InternalIntegerArgument.class, "other", "The integer to compare with"))
                        .withParameters(new Parameter(InternalLongArgument.class, "other", "The long to compare with"))
                        .returns(InternalBooleanArgument.class, "True if this number is equal to the given number, and false otherwise")
                        .executes(this::equalTo)
                        .withExamples(
                                "do Integer.(\"1\").equalTo(Integer.(\"1\")) -> True",
                                "do Float.(\"3.5\").==(Long.(\"-10\")) -> False"
                        ),
                new InstanceFunction("notEqualTo")
                        .withAliases("!=")
                        .withParameters(new Parameter(InternalDoubleArgument.class, "other", "The double to compare with"))
                        .withParameters(new Parameter(InternalFloatArgument.class, "other", "The float to compare with"))
                        .withParameters(new Parameter(InternalIntegerArgument.class, "other", "The integer to compare with"))
                        .withParameters(new Parameter(InternalLongArgument.class, "other", "The long to compare with"))
                        .returns(InternalBooleanArgument.class, "True if this number is not equal to the given number, and false otherwise")
                        .executes(this::notEqualTo)
                        .withExamples(
                                "do Float.(\"3.5\").notEqualTo(Long.(\"-10\")) -> True",
                                "do Integer.(\"1\").!=(Integer.(\"1\")) -> False"
                        ),
                new InstanceFunction("add")
                        .withAliases("+")
                        .withParameters(new Parameter(InternalDoubleArgument.class, "other", "The double to add"))
                        .withParameters(new Parameter(InternalFloatArgument.class, "other", "The float to add"))
                        .withParameters(new Parameter(InternalIntegerArgument.class, "other", "The integer to add"))
                        .withParameters(new Parameter(InternalLongArgument.class, "other", "The long to add"))
                        .returns(myClass(), "The sum of this number and the given number")
                        .executes(this::add)
                        .withExamples(
                                "do Integer.(\"1\").add(Integer.(\"1\")) -> 2",
                                "do Float.(\"3.5\").+(Long.(\"100\")) -> 103.5"
                        ),
                new InstanceFunction("subtract")
                        .withAliases("-")
                        .withParameters(new Parameter(InternalDoubleArgument.class, "other", "The double to subtract"))
                        .withParameters(new Parameter(InternalFloatArgument.class, "other", "The float to subtract"))
                        .withParameters(new Parameter(InternalIntegerArgument.class, "other", "The integer to subtract"))
                        .withParameters(new Parameter(InternalLongArgument.class, "other", "The long to subtract"))
                        .returns(myClass(), "This number minus the given number")
                        .executes(this::subtract)
                        .withExamples(
                                "do Integer.(\"1\").subtract(Integer.(\"1\")) -> 1",
                                "do Float.(\"10.4\").-(Long.(\"10\")) -> 0.4"
                        ),
                new InstanceFunction("multiply")
                        .withAliases("*")
                        .withParameters(new Parameter(InternalDoubleArgument.class, "other", "The double to multiply"))
                        .withParameters(new Parameter(InternalFloatArgument.class, "other", "The float to multiply"))
                        .withParameters(new Parameter(InternalIntegerArgument.class, "other", "The integer to multiply"))
                        .withParameters(new Parameter(InternalLongArgument.class, "other", "The long to multiply"))
                        .returns(myClass(), "The product of this number and the given number")
                        .executes(this::multiply)
                        .withExamples(
                                "do Integer.(\"2\").multiply(Integer.(\"2\")) -> 4",
                                "do Float.(\"3.56\").*(Long.(\"10\")) -> 35.6"
                        ),
                new InstanceFunction("divide")
                        .withAliases("/")
                        .withParameters(new Parameter(InternalDoubleArgument.class, "other", "The double to divide by"))
                        .withParameters(new Parameter(InternalFloatArgument.class, "other", "The float to divide by"))
                        .withParameters(new Parameter(InternalIntegerArgument.class, "other", "The integer to divide by"))
                        .withParameters(new Parameter(InternalLongArgument.class, "other", "The long to divide by"))
                        .returns(myClass(), "The sum of this number and the given number")
                        .executes(this::divide)
                        .withExamples(
                                "do Integer.(\"4\").divide(Integer.(\"2\")) -> 2",
                                "do Float.(\"15.0\")./(Long.(\"4\")) -> 3.75"
                        ),
                new InstanceFunction("toDouble")
                        .returns(InternalDoubleArgument.class, "This number, converted to a double")
                        .executes(this::toDouble)
                        .withExamples(
                                "do Double.(\"3.14\").toDouble() -> 3.14",
                                "do Float.(\"1.41\").toDouble() -> 1.41",
                                "do Integer.(\"4\").toDouble() -> 4.0",
                                "do Long.(\"100\").toDouble() -> 100.0"
                        ),
                new InstanceFunction("toFloat")
                        .returns(InternalFloatArgument.class, "This number, converted to a float")
                        .executes(this::toFloat)
                        .withExamples(
                                "do Double.(\"3.14\").toFloat() -> 3.14",
                                "do Float.(\"1.41\").toFloat() -> 1.41",
                                "do Integer.(\"4\").toFloat() -> 4.0",
                                "do Long.(\"100\").toFloat() -> 100.0"
                        ),
                new InstanceFunction("toInt")
                        .returns(InternalIntegerArgument.class, "This number, converted to a integer")
                        .executes(this::toInt)
                        .withExamples(
                                "do Double.(\"3.14\").toInt() -> 3",
                                "do Float.(\"1.41\").toInt() -> 1",
                                "do Integer.(\"4\").toInt() -> 4",
                                "do Long.(\"100\").toInt() -> 100"
                        ),
                new InstanceFunction("toLong")
                        .returns(InternalLongArgument.class, "This number, converted to a long")
                        .executes(this::toLong)
                        .withExamples(
                                "do Double.(\"3.14\").toLong() -> 3",
                                "do Float.(\"1.41\").toLong() -> 1",
                                "do Integer.(\"4\").toLong() -> 4",
                                "do Long.(\"100\").toLong() -> 100"
                        )
        );
    }

    InternalBooleanArgument lessThan(InternalArgument target, List<InternalArgument> parameters);

    InternalBooleanArgument lessThanOrEqual(InternalArgument target, List<InternalArgument> parameters);

    InternalBooleanArgument greaterThan(InternalArgument target, List<InternalArgument> parameters);

    InternalBooleanArgument greaterThanOrEqual(InternalArgument target, List<InternalArgument> parameters);

    InternalBooleanArgument equalTo(InternalArgument target, List<InternalArgument> parameters);

    InternalBooleanArgument notEqualTo(InternalArgument target, List<InternalArgument> parameters);

    InternalArgument add(InternalArgument target, List<InternalArgument> parameters);

    InternalArgument subtract(InternalArgument target, List<InternalArgument> parameters);

    InternalArgument multiply(InternalArgument target, List<InternalArgument> parameters);

    InternalArgument divide(InternalArgument target, List<InternalArgument> parameters);

    default InternalDoubleArgument toDouble(InternalArgument target, List<InternalArgument> parameters) {
        return new InternalDoubleArgument((Double) target.getValue());
    }

    default InternalFloatArgument toFloat(InternalArgument target, List<InternalArgument> parameters) {
        return new InternalFloatArgument((Float) target.getValue());
    }

    default InternalIntegerArgument toInt(InternalArgument target, List<InternalArgument> parameters) {
        return new InternalIntegerArgument((Integer) target.getValue());
    }

    default InternalLongArgument toLong(InternalArgument target, List<InternalArgument> parameters) {
        return new InternalLongArgument((Long) target.getValue());
    }

    default StaticFunctionList generateStaticFunctions() {
        return functions(
                new StaticFunction("maxValue")
                        .returns(myClass(), "The maximum value representable by this number")
                        .executes(this::maxValue),
                new StaticFunction("minValue")
                        .returns(myClass(), "The minimum value representable by this number")
                        .executes(this::minValue),
                new StaticFunction("new")
                        .withAliases("")
                        .withParameters()
                        .withParameters(new Parameter(InternalStringArgument.class, "value", "A string representing the value for this new number"))
                        .returns(myClass(), "A new number with the given value, or 0 if no value is given")
                        .executes(this::initialize)
                        .throwsException("NumberFormatException if the given value cannot be interpreted as a proper number")
        );
    }

    InternalArgument initialize(List<InternalArgument> parameters);

    InternalArgument minValue(List<InternalArgument> parameters);

    InternalArgument maxValue(List<InternalArgument> parameters);
}
