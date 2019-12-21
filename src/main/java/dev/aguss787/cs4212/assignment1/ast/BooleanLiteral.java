package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Bool;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.typecheck.LocalEnv;
import dev.aguss787.cs4212.assignment2.typecheck.TypeMap;
import dev.aguss787.cs4212.assignment2.typecheck.TypeWithErrors;

public class BooleanLiteral implements Expression {
    private final Boolean value;
    private final Type TYPE = PrimitiveType.BOOL;

    public BooleanLiteral(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "BooleanLiteral{" +
                "value=" + value +
                '}';
    }

    public TypeWithErrors checkWithType(TypeMap typeMap, LocalEnv localEnv) {
        return new TypeWithErrors(TYPE);
    }

    @Override
    public Type getSavedType() {
        return TYPE;
    }

    @Override
    public String getLabel() {
        return value.toString();
    }

    @Override
    public IR3Atom buildIR3Expression(IR3Function ir3Function) {
        return new IR3Bool(value.toString().equals("true"));
    }
}
