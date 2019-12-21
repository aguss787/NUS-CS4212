package dev.aguss787.cs4212.assignment3.asm.statement;

import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;

public class Cmp implements Statement {
    private final Atom left;
    private final Atom right;

    public Cmp(Atom left, Atom right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return String.format("\tcmp %s, %s", left, right);
    }
}
