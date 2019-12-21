package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.typecheck.ErrorList;
import dev.aguss787.cs4212.assignment2.typecheck.LocalEnv;
import dev.aguss787.cs4212.assignment2.typecheck.TypeMap;
import dev.aguss787.cs4212.assignment2.typecheck.TypeWithErrors;

public class ConstantAtom implements Atom {
    private Boolean isThis;
    private Boolean isNull;
    private Type savedType;

    public ConstantAtom() {
        this.isNull = false;
        this.isThis = false;
    }

    ConstantAtom(Boolean isThis, Boolean isNull) {
        this.isThis = isThis;
        this.isNull = isNull;
    }

    public ConstantAtom asThis() {
        return new ConstantAtom(true, false);
    }

    public ConstantAtom asNull() {
        return new ConstantAtom(false, true);
    }

    @Override
    public String toString() {
        return "ConstantAtom{" +
                ", isThis=" + isThis +
                ", isNull=" + isNull +
                '}';
    }

    public Boolean isThis() {
        return isThis;
    }

    public Boolean isNull() {
        return isNull;
    }

    public TypeWithErrors checkWithType(TypeMap typeMap, LocalEnv localEnv) {
        if (isThis()) {
            savedType = localEnv.get("this");
            return new TypeWithErrors(localEnv.get("this"));
        }
        if (isNull()) {
            savedType = PrimitiveType.NULL;
            return new TypeWithErrors(PrimitiveType.NULL);
        }
        ErrorList errors = new ErrorList();
        errors.addIfNotNull(new Error("Unknown atom"));
        return new TypeWithErrors(errors);
    }

    @Override
    public String getLabel() {
        if(isNull) {
            return "NULL";
        }

        if(isThis) {
            return "this";
        }

        return "";
    }

    @Override
    public Type getSavedType() {
        return savedType;
    }

    @Override
    public IR3Atom buildIR3Expression(IR3Function ir3Function) {
        return new IR3Atom(getLabel());
    }
}
