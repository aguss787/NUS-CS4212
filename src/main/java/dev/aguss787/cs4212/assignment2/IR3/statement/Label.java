package dev.aguss787.cs4212.assignment2.IR3.statement;

import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;

import java.util.Objects;

public class Label extends Statement {
    private final String value;

    public Label(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getSpacing() {
        return "  ";
    }

    @Override
    public String toString() {
        return value + ":";
    }

    @Override
    public void buildAsm(AssemblyBuilder assemblyBuilder) {
        assemblyBuilder.addLabel(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Label label = (Label) o;
        return Objects.equals(value, label.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
