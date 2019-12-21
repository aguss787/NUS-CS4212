package dev.aguss787.cs4212.assignment3.asm.statement;

import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;
import dev.aguss787.cs4212.assignment3.asm.Atom.Register;

public class DataInstruction implements Statement {
    private final String operation;
    private final Register target;
    private final Atom left;
    private final Atom right;

    public DataInstruction(String operation, Register target, Atom left, Atom right) {
        this.operation = operation;
        this.target = target;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return String.format("\t%s %s, %s, %s", operation, target, left, right);
    }
}
