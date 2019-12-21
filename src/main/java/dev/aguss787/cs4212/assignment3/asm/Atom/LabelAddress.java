package dev.aguss787.cs4212.assignment3.asm.Atom;

public class LabelAddress implements Atom {
    private final String label;

    public LabelAddress(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "=" + label;
    }
}
