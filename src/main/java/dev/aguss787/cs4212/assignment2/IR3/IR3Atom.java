package dev.aguss787.cs4212.assignment2.IR3;

import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;
import dev.aguss787.cs4212.assignment3.asm.Atom.Memory;
import dev.aguss787.cs4212.assignment3.asm.Atom.Register;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class IR3Atom {
    private final String value;

    public IR3Atom() {
        value = "";
    }

    public IR3Atom(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public Atom getAsmReference(AssemblyBuilder assemblyBuilder) {
        return assemblyBuilder.loadFromMemory(toString());
    }

    public List<String> getUsedVariable() {
        return new ArrayList<>(){{ add(value); }};
    }

    public List<String> getInvolvedVariable() {
        return getUsedVariable();
    }

    public IR3Atom eval(Map<String, IR3Atom> map) {
        return map.get(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IR3Atom ir3Atom = (IR3Atom) o;
        return Objects.equals(value, ir3Atom.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
