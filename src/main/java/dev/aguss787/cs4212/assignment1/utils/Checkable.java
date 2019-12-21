package dev.aguss787.cs4212.assignment1.utils;

import dev.aguss787.cs4212.assignment2.typecheck.ErrorList;
import dev.aguss787.cs4212.assignment2.typecheck.LocalEnv;
import dev.aguss787.cs4212.assignment2.typecheck.TypeMap;

public interface Checkable {
    ErrorList check(TypeMap typeMap, LocalEnv localEnv);
}
