package dev.aguss787.cs4212.assignment2.IR3;

import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.AsmInteger;
import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;
import dev.aguss787.cs4212.assignment3.asm.Atom.Register;

import java.util.ArrayList;
import java.util.List;

public class IR3New extends IR3Atom {
    private final String name;

    public IR3New(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("new %s()", name);
    }

    @Override
    public String getValue() {
        return toString();
    }

    @Override
    public Atom getAsmReference(AssemblyBuilder assemblyBuilder) {
        assemblyBuilder.addMov(
                new Register("a1"),
                new AsmInteger(assemblyBuilder.getClassSize(name))
        );
        assemblyBuilder.addCallWithLink("malloc");
        return new Register("a1");
    }

    @Override
    public List<String> getUsedVariable() {
        return new ArrayList<>();
    }
}
