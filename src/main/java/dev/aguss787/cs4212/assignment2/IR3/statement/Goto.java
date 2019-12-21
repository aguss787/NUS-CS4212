package dev.aguss787.cs4212.assignment2.IR3.statement;

import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.statement.Label;

public class Goto extends Statement implements Jump {
    private final dev.aguss787.cs4212.assignment2.IR3.statement.Label label;

    public Goto(String label) {
        this.label = new dev.aguss787.cs4212.assignment2.IR3.statement.Label(label);
    }

    public dev.aguss787.cs4212.assignment2.IR3.statement.Label getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "goto " + label.getValue() + ";";
    }

    @Override
    public void buildAsm(AssemblyBuilder assemblyBuilder) {
        assemblyBuilder.addJump(new Label(label.getValue()));
    }
}
