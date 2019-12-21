package dev.aguss787.cs4212.assignment2.IR3;

import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.AsmInteger;
import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IR3Bool extends IR3Atom{
    private final Integer value;

    public IR3Bool(Boolean value) {
        super(value.toString());
        this.value = value ? 1 : 0;
    }

    @Override
    public Atom getAsmReference(AssemblyBuilder assemblyBuilder) {
        return new AsmInteger(value);
    }

    @Override
    public List<String> getUsedVariable() {
        return new ArrayList<>();
    }

    @Override
    public IR3Atom eval(Map<String, IR3Atom> map) {
        return this;
    }

    public Boolean asBoolean() {
        return this.value == 1;
    }
}
