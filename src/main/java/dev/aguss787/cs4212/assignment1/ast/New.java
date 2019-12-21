package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.IR3.IR3New;
import dev.aguss787.cs4212.assignment2.typecheck.ErrorList;
import dev.aguss787.cs4212.assignment2.typecheck.LocalEnv;
import dev.aguss787.cs4212.assignment2.typecheck.TypeMap;
import dev.aguss787.cs4212.assignment2.typecheck.TypeWithErrors;

public class New implements Atom {
    private final String name;
    Type savedType;

    public New(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "New{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public TypeWithErrors checkWithType(TypeMap typeMap, LocalEnv localEnv) {
        ErrorList errors = new ErrorList();
        errors.addIfNotNull(typeMap.containsKey(name));
        if (errors.size() > 0) {
            return new TypeWithErrors(errors);
        }
        savedType = new ClassType(name);
        return new TypeWithErrors(new ClassType(name));
    }

    @Override
    public Type getSavedType() {
        return savedType;
    }

    @Override
    public String getLabel() {
        return "new " + name + "()";
    }

    @Override
    public IR3Atom buildIR3Expression(IR3Function ir3Function) {
        IR3Atom id = ir3Function.getNewIdentifier(savedType);
        ir3Function.putAssignment(id, new IR3New(name));
        return id;
    }
}
