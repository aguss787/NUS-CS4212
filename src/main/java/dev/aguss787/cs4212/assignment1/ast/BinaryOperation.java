package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3BinaryOperator;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.typecheck.ErrorList;
import dev.aguss787.cs4212.assignment2.typecheck.LocalEnv;
import dev.aguss787.cs4212.assignment2.typecheck.TypeMap;
import dev.aguss787.cs4212.assignment2.typecheck.TypeWithErrors;

import java.util.Optional;

import static dev.aguss787.cs4212.assignment2.typecheck.TypeCheck.getTypeName;

public class BinaryOperation implements Expression {
    private final Expression left;
    private final Expression right;
    private final Operator operator;
    private Type savedType;

    public BinaryOperation(Expression left, Expression right, Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public String prettyPrint(int indent) {
        return indent(indent) + "ArithmeticOperation{\n" +
                indent(indent + 2) + "left=\n" + left.prettyPrint(indent + 4) + "\n" +
                indent(indent + 2) + ", right=\n" + right.prettyPrint(indent + 4) + "\n" +
                indent(indent + 2) + ", operator=" + operator + "\n" +
                indent(indent) + '}';
    }

    public TypeWithErrors checkWithType(TypeMap typeMap, LocalEnv localEnv) {
        TypeWithErrors leftResult = left.checkWithType(typeMap, localEnv);
        TypeWithErrors rightResult = right.checkWithType(typeMap, localEnv);

        if (leftResult.hasError() || rightResult.hasError()) {
            ErrorList errors = new ErrorList();
            errors.addIfNotNull(leftResult.getErrors());
            errors.addIfNotNull(rightResult.getErrors());
            return new TypeWithErrors(errors);
        }

        Optional<Type> ret = operator.operate(leftResult.getType(), rightResult.getType());
        if (ret.isEmpty()) {
            ErrorList errors = new ErrorList();
            errors.addIfNotNull(new Error(String.format(
                    "Incompatible type for operator %s: %s %s [%s]",
                    operator.toString(),
                    getTypeName(leftResult.getType()),
                    getTypeName(rightResult.getType()),
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
        return left.getLabel() + operator.getLabel() + right.getLabel();
    }

    @Override
    public IR3Atom buildIR3Expression(IR3Function ir3Function) {
        IR3Atom id = ir3Function.getNewIdentifier(savedType);
        ir3Function.putAssignment(
                id,
                new IR3BinaryOperator(left.buildIR3Expression(ir3Function), operator, right.buildIR3Expression(ir3Function))
        );
        return id;
    }
}
