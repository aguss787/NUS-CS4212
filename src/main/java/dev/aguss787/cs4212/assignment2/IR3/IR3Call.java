package dev.aguss787.cs4212.assignment2.IR3;

import dev.aguss787.cs4212.assignment2.IR3.statement.Call;
import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;
import dev.aguss787.cs4212.assignment3.asm.Atom.Register;

import java.util.List;

public class IR3Call extends IR3Atom {
    private final IR3CallableAtom func;
    private final List<IR3Atom> args;

    public IR3Call(IR3CallableAtom func, List<IR3Atom> args) {
        this.func = func;
        this.args = args;
    }

    @Override
    public String toString() {
        return new Call(func, args).toString().replaceFirst(".$","");
    }

    @Override
    public String getValue() {
        return toString();
    }

    @Override
    public Atom getAsmReference(AssemblyBuilder assemblyBuilder) {
        Call call = new Call(func, args);
        call.buildAsm(assemblyBuilder);
        return new Register("a1");
    }

    @Override
    public List<String> getUsedVariable() {
        return new Call(func, args).getInvolvedVariable();
    }
}
