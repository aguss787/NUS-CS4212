package dev.aguss787.cs4212.assignment3.asm.statement;

import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;

public class Or implements Statement {
    private final Atom target;
    private final Atom left;
    private final Atom right;

    public Or(Atom target, Atom left, Atom right) {
        this.target = target;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return String.format("\torr %s, %s, %s", target, left, right);
    }
}
