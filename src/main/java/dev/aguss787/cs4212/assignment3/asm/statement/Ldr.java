package dev.aguss787.cs4212.assignment3.asm.statement;

public class Ldr implements Statement {
    private final String register;
    private final String mem;

    public Ldr(String register, String mem) {
        this.register = register;
        this.mem = mem;
    }

    public String getRegister() {
        return register;
    }

    public String getMem() {
        return mem;
    }

    @Override
    public String toString() {
        return String.format("\tldr %s, %s", register, mem);
    }
}
