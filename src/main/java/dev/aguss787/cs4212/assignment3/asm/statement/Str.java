package dev.aguss787.cs4212.assignment3.asm.statement;

import dev.aguss787.cs4212.assignment3.asm.Atom.Memory;
import dev.aguss787.cs4212.assignment3.asm.Atom.Register;

public class Str implements Statement {
    private final Register register;
    private final Memory memory;

    public Str(Register register, Memory memory) {
        this.register = register;
        this.memory = memory;
    }

    public Memory getMemory() {
        return memory;
    }

    public Register getRegister() {
        return register;
    }

    @Override
    public String toString() {
        return String.format("\tstr %s, %s", register.toString(), memory.toString());
    }
}
