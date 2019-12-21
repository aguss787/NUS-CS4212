package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment1.utils.PrettyPrintable;

import java.util.Objects;

public class Declaration implements PrettyPrintable {
    private final Type type;
    private final String name;

    public Declaration(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return type + " => " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Declaration that = (Declaration) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name);
    }
}
