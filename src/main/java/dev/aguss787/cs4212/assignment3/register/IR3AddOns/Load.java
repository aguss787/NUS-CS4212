package dev.aguss787.cs4212.assignment3.register.IR3AddOns;

import dev.aguss787.cs4212.assignment2.IR3.statement.Statement;
import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.Memory;
import dev.aguss787.cs4212.assignment3.asm.Atom.Register;

public class Load extends Statement {
    private final String varName;
    private final Integer register;

    private boolean setOnly = false;

    public Load(String varName, Integer register) {
        this.varName = varName;
        this.register = register;
    }

    public String getVarName() {
        return varName;
    }

    public Integer getRegister() {
        return register;
    }

    public void setSetOnly() {
        setOnly = true;
    }

    @Override
    public String toString() {
        return "Load{" +
                "varName='" + varName + '\'' +
                ", register=" + register +
                ", setOnly=" + setOnly +
                '}';
    }

    @Override
    public void buildAsm(AssemblyBuilder assemblyBuilder) {
        Register register = new Register("v", this.register + 1);
        assemblyBuilder.setRegisterPos(varName, register);
        if(!setOnly) {
            assemblyBuilder.addLoadMemory(
                    register,
                    (Memory) assemblyBuilder.getMemory(varName)
            );
        }
    }
}
