package dev.aguss787.cs4212.assignment3.asm.statement;

import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;
import dev.aguss787.cs4212.assignment3.asm.Atom.Register;

public class SingleDataInstruction implements Statement {
    private final String operation;
    private final Register target;
    private final Atom left;

    public SingleDataInstruction(String operation, Register target, Atom left) {
        this.operation = operation;
        this.target = target;
        this.left = left;
    }

    @Override
    public String toString() {
        return String.format("\t%s %s, %s", operation, target, left);
    }
}
