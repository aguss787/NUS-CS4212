package dev.aguss787.cs4212.assignment3.asm.statement;

public class Jump implements Statement {
    private final Label label;

    public Jump(Label label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return String.format("\tb %s", label.getLabel());
    }
}
