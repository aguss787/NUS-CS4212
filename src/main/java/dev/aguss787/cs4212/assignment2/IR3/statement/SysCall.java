package dev.aguss787.cs4212.assignment2.IR3.statement;

import dev.aguss787.cs4212.assignment1.ast.PrimitiveType;
import dev.aguss787.cs4212.assignment1.ast.Type;
import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Integer;
import dev.aguss787.cs4212.assignment2.IR3.IR3String;
import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;
import dev.aguss787.cs4212.assignment3.asm.Atom.Memory;
import dev.aguss787.cs4212.assignment3.asm.Atom.Register;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SysCall extends Statement {
    private final String func;
    private final IR3Atom arg;

    public SysCall(String func, IR3Atom arg) {
        this.func = func;
        this.arg = arg;
    }

    @Override
    public String toString() {
        return String.format("%s(%s);", func, arg);
    }

    public void buildAsm(AssemblyBuilder assemblyBuilder) {
        if(func.equals("println")) {
            if(arg instanceof IR3String) {
                assemblyBuilder.addLoadString("a1", arg.toString());
                assemblyBuilder.addCallWithLink("puts");
            } else if(arg instanceof IR3Integer) {
                assemblyBuilder.addMov(new Register("a2"), arg.getAsmReference(assemblyBuilder));
                assemblyBuilder.addLoadString("a1", "\"%d\\n\"");
                assemblyBuilder.addCallWithLink("printf");
            } else {
                Type type = assemblyBuilder.getType(arg.toString());
                if (type == PrimitiveType.STRING) {
                    Register register = assemblyBuilder.loadFromMemory(arg.toString());
                    assemblyBuilder.addMov(new Register("a1"), register);
                    assemblyBuilder.addCallWithLink("puts");
                } else {
                    Atom register = arg.getAsmReference(assemblyBuilder);
                    assemblyBuilder.addMov(new Register("a2"), register);
                    assemblyBuilder.addLoadString("a1", "\"%d\\n\"");
                    assemblyBuilder.addCallWithLink("printf");
                }
            }
        } else if(func.equals("readln")) {
            Type type = assemblyBuilder.getType(arg.toString());
            if (type == PrimitiveType.STRING) {
                throw new Error(func + " syscall not implemented for string");
            } else {
                Atom register = arg.getAsmReference(assemblyBuilder);
                assemblyBuilder.addCallWithLink("_readln");
                assemblyBuilder.addMov(register, new Register("a1"));
                assemblyBuilder.setWritelnFlag();
            }
        } else {
            throw new Error(func + " syscall not implemented");
        }
    }

    @Override
    public List<String> getInvolvedVariable() {
        return arg.getUsedVariable();
    }

    @Override
    public List<String> getUsedVariable() {
        if(func.equals("println")) {
            return arg.getUsedVariable();
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getWrittenVariable() {
        if(func.equals("readln")) {
            return arg.getUsedVariable();
        }
        return new ArrayList<>();
    }
}
