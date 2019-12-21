package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.IR3.IR3UnaryOperation;
import dev.aguss787.cs4212.assignment2.typecheck.ErrorList;
import dev.aguss787.cs4212.assignment2.typecheck.LocalEnv;
import dev.aguss787.cs4212.assignment2.typecheck.TypeMap;
import dev.aguss787.cs4212.assignment2.typecheck.TypeWithErrors;

import java.util.Optional;

import static dev.aguss787.cs4212.assignment2.typecheck.TypeCheck.getTypeName;

public class UnaryOperation implements Expression {
    private final Expression operand;
    private final Operator operator;
    private Type savedType;

    public UnaryOperation(Expression operand, Operator operator) {
        this.operand = operand;
        this.operator = operator;
    }

    public Expression getOperand() {
        return operand;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setSavedType(Type savedType) {
        this.savedType = savedType;
    }

    @Override
    public String prettyPrint(int indent) {
        return indent(indent) + "UnaryOperation{\n" +
                indent(indent + 2) + "operand=\n" + operand.prettyPrint(indent + 4) + "\n" +
                indent(indent + 2) + ", operator=" + operator + "\n" +
                indent(indent) + '}';
    }

    @Override
    public TypeWithErrors checkWithType(TypeMap typeMap, LocalEnv localEnv) {
        TypeWithErrors leftResult = operand.checkWithType(typeMap, localEnv);

        if (leftResult.hasError()) {
            ErrorList errors = new ErrorList();
            errors.addIfNotNull(leftResult.getErrors());
            return new TypeWithErrors(errors);
        }

        Optional<Type> ret = operator.operate(leftResult.getType(), leftResult.getType());
        if (ret.isEmpty()) {
            ErrorList errors = new ErrorList();
            errors.addIfNotNull(new Error(String.format(
                    "Incompatible type for operator %s: %s [%s]",
                    operator.toString(),
                    getTypeName(leftResult.getType()),
                    getLabel()
            )));
            return new TypeWithErrors(errors);
        }

        savedType = ret.get();
        return new TypeWithErrors(ret.get());
    }

    @Override
    public Type getSavedType() {
        return savedType;
    }

    @Override
    public String getLabel() {
        return operand.getLabel() + operand.getLabel();
    }

    @Override
    public IR3Atom buildIR3Expression(IR3Function ir3Function) {
        IR3Atom id = ir3Function.getNewIdentifier(savedType);
        ir3Function.putAssignment(
                id,
                new IR3UnaryOperation(operator, operand.buildIR3Expression(ir3Function))
        );
        return id;
    }
}
