package dev.aguss787.cs4212.assignment3.asm.statement;

public class Pop implements Statement {
    private final String[] registers;

    public Pop(String... registers) {
        this.registers = registers;
    }

    public String[] getRegisters() {
        return registers;
    }

    @Override
    public String toString() {
        return String.format("\tpop {%s}", String.join(", ", registers));
    }
}
