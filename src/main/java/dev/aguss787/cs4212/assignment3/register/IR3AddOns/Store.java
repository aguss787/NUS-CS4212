package dev.aguss787.cs4212.assignment3.register.IR3AddOns;

import dev.aguss787.cs4212.assignment2.IR3.statement.Statement;
import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.Memory;
import dev.aguss787.cs4212.assignment3.asm.Atom.Register;

public class Store extends Statement {
    private final String varName;
    private final Integer register;

    public Store(String varName, Integer register) {
        this.varName = varName;
        this.register = register;
    }

    public String getVarName() {
        return varName;
    }

    public Integer getRegister() {
        return register;
    }

    @Override
    public String toString() {
        return "Store{" +
                "varName='" + varName + '\'' +
                ", register=" + register +
                '}';
    }

    @Override
    public void buildAsm(AssemblyBuilder assemblyBuilder) {
        assemblyBuilder.addStoreMemory(
                new Register("v", register + 1),
                (Memory) assemblyBuilder.getMemory(varName)
        );
    }
}
