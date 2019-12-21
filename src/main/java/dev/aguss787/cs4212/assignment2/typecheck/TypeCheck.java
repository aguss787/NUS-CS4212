package dev.aguss787.cs4212.assignment2.typecheck;

import dev.aguss787.cs4212.assignment1.ast.*;

public class TypeCheck {
    public static String getTypeName(Type type) {
        // Is primitive?
        if (type instanceof PrimitiveType) {
            return type.toString();
        }

        // Is function?
        if (type instanceof Function) {
            return type.toString();
        }

        // Is class?
        if (type instanceof ClassType) {
            return ((ClassType)type).getName();
        }

        return type.toString();
    }
}
