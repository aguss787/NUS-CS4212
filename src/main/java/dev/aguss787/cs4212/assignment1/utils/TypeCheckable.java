package dev.aguss787.cs4212.assignment1.utils;

import dev.aguss787.cs4212.assignment1.ast.Type;
import dev.aguss787.cs4212.assignment2.typecheck.LocalEnv;
import dev.aguss787.cs4212.assignment2.typecheck.TypeMap;
import dev.aguss787.cs4212.assignment2.typecheck.TypeWithErrors;

public interface TypeCheckable {
    default TypeWithErrors checkWithType(TypeMap typeMap, LocalEnv localEnv) {
        throw new Error("Not Implemented");
    }
    Type getSavedType();
}
