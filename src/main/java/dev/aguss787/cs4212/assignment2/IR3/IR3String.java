package dev.aguss787.cs4212.assignment2.IR3;

import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;
import dev.aguss787.cs4212.assignment3.asm.Atom.LabelAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IR3String extends IR3Atom {
    public IR3String(String value) {
        super(value);
    }

    @Override
    public Atom getAsmReference(AssemblyBuilder assemblyBuilder) {
        return new LabelAddress(assemblyBuilder.addStringConstant(getValue()));
    }

    @Override
    public List<String> getUsedVariable() {
        return new ArrayList<>();
    }

    @Override
    public IR3Atom eval(Map<String, IR3Atom> map) {
        return this;
    }
}
