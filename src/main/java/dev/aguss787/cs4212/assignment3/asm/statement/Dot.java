package dev.aguss787.cs4212.assignment3.asm.statement;

public class Dot implements Statement {
    private final String left;
    private final String[] right;

    public Dot(String left, String... right) {
        this.left = left;
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public String[] getRight() {
        return right;
    }

    @Override
    public String toString() {
        return String.format("\t.%s %s", left, String.join(", ", right));
    }
}
