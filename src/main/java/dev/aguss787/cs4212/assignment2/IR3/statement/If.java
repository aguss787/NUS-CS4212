package dev.aguss787.cs4212.assignment2.IR3.statement;

import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;
import dev.aguss787.cs4212.assignment3.asm.statement.Label;

import java.util.List;

public class If extends Statement implements Jump {
    private final IR3Atom cond;
    private final dev.aguss787.cs4212.assignment2.IR3.statement.Label label;

    public If(IR3Atom cond, String label) {
        this.cond = cond;
        this.label = new dev.aguss787.cs4212.assignment2.IR3.statement.Label(label);
    }

    public IR3Atom getCond() {
        return cond;
    }

    @Override
    public String toString() {
        return String.format("if (%s) goto %s;", cond, label.getValue());
    }

    @Override
    public void buildAsm(AssemblyBuilder assemblyBuilder) {
        Atom condition = cond.getAsmReference(assemblyBuilder);
        assemblyBuilder.addBEQ(condition, new Label(label.getValue()));
    }

    public dev.aguss787.cs4212.assignment2.IR3.statement.Label getLabel() {
        return label;
    }

    @Override
    public List<String> getInvolvedVariable() {
        return cond.getUsedVariable();
    }

    @Override
    public List<String> getUsedVariable() {
        return cond.getUsedVariable();
    }
}
