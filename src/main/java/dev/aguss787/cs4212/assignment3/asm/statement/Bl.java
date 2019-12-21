package dev.aguss787.cs4212.assignment3.asm.statement;

public class Bl implements Statement {
    private final String label;

    public Bl(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return String.format("\tbl %s(PLT)", label);
    }
}
