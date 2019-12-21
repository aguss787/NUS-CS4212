package dev.aguss787.cs4212.assignment3.asm.statement;

import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;

public class Cbnz implements Statement {
    Atom cond;
    Label label;

    public Cbnz(Atom cond, Label label) {
        this.cond = cond;
        this.label = label;
    }

    @Override
    public String toString() {
        return String.format("\tCMP %s, #0\n\tbne %s", cond, label.getLabel());
    }
}
