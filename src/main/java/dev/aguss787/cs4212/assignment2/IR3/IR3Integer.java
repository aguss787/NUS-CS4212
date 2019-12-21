package dev.aguss787.cs4212.assignment2.IR3;

import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.AsmInteger;
import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IR3Integer extends IR3Atom {
    public IR3Integer(Integer value) {
        super(value.toString());
    }

    @Override
    public Atom getAsmReference(AssemblyBuilder assemblyBuilder) {
        return new AsmInteger(Integer.parseInt(getValue()));
    }

    @Override
    public List<String> getUsedVariable() {
        return new ArrayList<>();
    }

    public Integer asInteger() {
        return Integer.parseInt(getValue());
    }

    @Override
    public IR3Atom eval(Map<String, IR3Atom> map) {
        return this;
    }
}
