package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.typecheck.ErrorList;
import dev.aguss787.cs4212.assignment2.typecheck.LocalEnv;
import dev.aguss787.cs4212.assignment2.typecheck.TypeMap;
import dev.aguss787.cs4212.assignment2.typecheck.TypeWithErrors;

import java.util.Optional;

import static dev.aguss787.cs4212.assignment2.typecheck.TypeCheck.getTypeName;

public class Return implements Statement {
    private final Optional<Expression> expr;

    public Return(Expression expr) {
        this.expr = Optional.of(expr);
    }

    public Return() {
        this.expr = Optional.empty();
    }

    @Override
    public String prettyPrint(int indent) {
        String exprStr = "";
        if (expr.isPresent()) {
            exprStr = expr.get().prettyPrint(indent + 4);
        }
        return indent(indent) + "Return{\n" +
                indent(indent + 2) + "expr=\n" + exprStr + "\n" +
                indent(indent) + '}';
    }

    public Optional<Expression> getExpr() {
        return expr;
    }

    public ErrorList check(TypeMap typeMap, LocalEnv localEnv) {
        ErrorList errors = new ErrorList();

        Type type = PrimitiveType.VOID;
        if (expr.isPresent()) {
            TypeWithErrors typeWithErrors = expr.get().checkWithType(typeMap, localEnv);
            if (typeWithErrors.hasError()) {
                errors.addIfNotNull(typeWithErrors.getErrors());
                return errors;
            }
            type = typeWithErrors.getType();
        }

        if (!localEnv.getRet().equals(getTypeName(type))) {
            errors.addIfNotNull(new Error("Return type \"" + getTypeName(type)
                    + "\" found, \"" + localEnv.getRet() + "\" expected"));
        }

        return errors;
    }

    @Override
    public void buildIR3Statement(IR3Function ir3Function) {
        if (expr.isPresent()) {
            ir3Function.putReturn(expr.get().buildIR3Expression(ir3Function));
        } else {
            ir3Function.putReturn();
        }
    }


}
