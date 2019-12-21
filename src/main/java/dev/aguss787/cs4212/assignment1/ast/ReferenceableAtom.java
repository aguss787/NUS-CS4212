package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3CallableAtom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Dot;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.typecheck.*;

public class ReferenceableAtom implements Referenceable {
    private final String name;
    Type savedType;

    public ReferenceableAtom(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ReferenceableAtom{" +
                "name='" + name + '\'' +
                '}';
    }

    public TypeWithErrors checkWithType(TypeMap typeMap, LocalEnv localEnv) {
        if (localEnv.containsKey(name) == null) {
            savedType = localEnv.get(name);
            return new TypeWithErrors(localEnv.get(name));
        }

        ErrorList errors = new ErrorList();
        errors.addIfNotNull(new Error(String.format(
                "Undefined variable \"%s\"",
                name
        )));
        return new TypeWithErrors(errors);
    }

    @Override
    public Type getSavedType() {
        return savedType;
    }

    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public IR3Atom buildIR3Expression(IR3Function ir3Function) {
        if (savedType instanceof FunctionSignature) {
            return new IR3CallableAtom("this", ir3Function.getFunction(name));
        }

        if (ir3Function.getDeclarations().stream().map(Declaration::getName).noneMatch(name::equals)
                && ir3Function.getArgs().stream().map(Declaration::getName).noneMatch(name::equals)) {
            IR3Atom id = ir3Function.getNewIdentifier(savedType);
            ir3Function.putAssignment(
                    id,
                    new IR3Dot(new IR3Atom("this"), new IR3Atom(name))
            );
            return id;
        }

        return new IR3Atom(name);
    }

    @Override
    public IR3Atom buildIR3Reference(IR3Function ir3Function) {
        if (ir3Function.getDeclarations().stream().map(Declaration::getName).noneMatch(name::equals)
                && ir3Function.getArgs().stream().map(Declaration::getName).noneMatch(name::equals)) {
            return new IR3Dot(new IR3Atom("this"), new IR3Atom(name));
        }
        return buildIR3Expression(ir3Function);
    }
}
