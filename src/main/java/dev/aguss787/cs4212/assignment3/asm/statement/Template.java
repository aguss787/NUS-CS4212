package dev.aguss787.cs4212.assignment3.asm.statement;

public class Template implements Statement {
    private final String str;

    public Template(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
