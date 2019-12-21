package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.typecheck.LocalEnv;
import dev.aguss787.cs4212.assignment2.typecheck.TypeMap;
import dev.aguss787.cs4212.assignment2.typecheck.TypeWithErrors;

public class AtomizedExpression implements Atom {
    private final Expression expr;
    private Type savedType;

    public AtomizedExpression(Expression expr) {
        this.expr = expr;
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public String prettyPrint(int indent) {
        return indent(indent) + "AtomizedExpression{\n" +
                indent(indent  + 2) + "expr=\n" + expr.prettyPrint(indent + 4) + "\n" +
                indent(indent) + '}';
    }

    public TypeWithErrors checkWithType(TypeMap typeMap, LocalEnv localEnv) {
        TypeWithErrors tmp = expr.checkWithType(typeMap, localEnv);
        savedType = tmp.getType();
        return tmp;
    }

    @Override
    public Type getSavedType() {
        return savedType;
    }

    @Override
    public String getLabel() {
        return expr.getLabel();
    }

    @Override
    public IR3Atom buildIR3Expression(IR3Function ir3Function) {
        return expr.buildIR3Expression(ir3Function);
    }
}
