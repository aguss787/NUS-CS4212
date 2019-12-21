package dev.aguss787.cs4212.assignment3.asm.statement;

public class Push implements Statement {
    private final String[] registers;

    public Push(String... registers) {
        this.registers = registers;
    }

    public String[] getRegisters() {
        return registers;
    }

    @Override
    public String toString() {
        return String.format("\tpush {%s}", String.join(", ", registers));
    }
}
