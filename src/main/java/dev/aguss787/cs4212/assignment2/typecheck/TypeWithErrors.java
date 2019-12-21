package dev.aguss787.cs4212.assignment2.typecheck;

import dev.aguss787.cs4212.assignment1.ast.Type;

import java.util.List;

public class TypeWithErrors {
    private final ErrorList errors = new ErrorList();
    private Type type;

    public TypeWithErrors(Type type) {
        this.type = type;
    }

    public TypeWithErrors(List<Error> errors) {
        this.errors.addIfNotNull(errors);
    }

    public ErrorList getErrors() {
        return errors;
    }

    public Type getType() {
        return type;
    }

    public boolean hasError() {
        return errors.size() > 0;
    }
}
