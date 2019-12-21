package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.typecheck.ErrorList;
import dev.aguss787.cs4212.assignment2.typecheck.LocalEnv;
import dev.aguss787.cs4212.assignment2.typecheck.TypeMap;
import dev.aguss787.cs4212.assignment2.typecheck.TypeWithErrors;

import static dev.aguss787.cs4212.assignment2.typecheck.TypeCheck.getTypeName;

public class Assignment implements Statement {
    private final Referenceable target;
    private final Expression expr;

    public Assignment(Referenceable target, Expression expr) {
        this.target = target;
        this.expr = expr;
    }

    public Referenceable getTarget() {
        return target;
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public String prettyPrint(int indent) {
        return indent(indent) + "Assignment{\n" +
                indent(indent + 2) +  "target=\n" + target.prettyPrint(indent + 4) + "\n" +
                indent(indent + 2) +  ", expr=\n" + expr.prettyPrint(indent + 4) + "\n" +
                indent(indent) +  '}';
    }

    public ErrorList check(TypeMap typeMap, LocalEnv localEnv) {
        TypeWithErrors leftResult = target.checkWithType(typeMap, localEnv);
        TypeWithErrors rightResult = expr.checkWithType(typeMap, localEnv);

        if (leftResult.hasError() || rightResult.hasError()) {
            ErrorList errors = new ErrorList();
            errors.addIfNotNull(leftResult.getErrors());
            errors.addIfNotNull(rightResult.getErrors());
            return errors;
        }

        if (!leftResult.getType().equals(rightResult.getType())) {
            ErrorList errors = new ErrorList();
            errors.addIfNotNull(new Error(String.format(
                    "Cannot assign \"%s\" [%s] to \"%s\" [%s]",
                    expr.getLabel(),
                    getTypeName(rightResult.getType()),
                    target.getLabel(),
                    getTypeName(leftResult.getType())
            )));
            return errors;
        }
        return new ErrorList();
    }

    @Override
    public void buildIR3Statement(IR3Function ir3Function) {
        ir3Function.putAssignment(target.buildIR3Reference(ir3Function), expr.buildIR3Expression(ir3Function));
    }
}
