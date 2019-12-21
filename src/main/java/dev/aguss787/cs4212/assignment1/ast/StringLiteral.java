package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.IR3.IR3String;
import dev.aguss787.cs4212.assignment2.typecheck.LocalEnv;
import dev.aguss787.cs4212.assignment2.typecheck.TypeMap;
import dev.aguss787.cs4212.assignment2.typecheck.TypeWithErrors;

public class StringLiteral implements Expression {
    private final String value;
    private final Type TYPE = PrimitiveType.STRING;

    public String getValue() {
        return value;
    }

    public StringLiteral(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "StringLiteral{" +
                "value='" + value + '\'' +
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
        return "\"" + value + "\"";
    }

    @Override
    public IR3Atom buildIR3Expression(IR3Function ir3Function) {
        return new IR3String("\"" + value + "\"");
    }
}
