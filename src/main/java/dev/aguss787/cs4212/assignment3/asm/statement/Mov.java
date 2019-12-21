package dev.aguss787.cs4212.assignment3.asm.statement;

import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;

public class Mov implements Statement {
    public final Atom target;
    public final Atom origin;
    public final String suffix;

    public Mov(Atom target, Atom origin) {
        this.target = target;
        this.origin = origin;
        this.suffix = "";
    }

    public Mov(String suffix, Atom target, Atom origin) {
        this.target = target;
        this.origin = origin;
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return String.format("\tmov%s %s, %s", suffix, target, origin);
    }
}
