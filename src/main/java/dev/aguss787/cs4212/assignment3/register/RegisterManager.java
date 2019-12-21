package dev.aguss787.cs4212.assignment3.register;

import dev.aguss787.cs4212.assignment1.ast.Type;
import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Dot;
import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.AsmInteger;
import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;
import dev.aguss787.cs4212.assignment3.asm.Atom.Memory;
import dev.aguss787.cs4212.assignment3.asm.Atom.Register;

import java.util.HashMap;
import java.util.Map;

public class RegisterManager {
    private final AssemblyBuilder assemblyBuilder;
    private Atom curMem = null;
    private String cur = null;

    private final Map<String, Register> registerMap;

    public RegisterManager(AssemblyBuilder assemblyBuilder) {
        this.assemblyBuilder = assemblyBuilder;
        registerMap = new HashMap<>();
    }

    public Atom getCurMem() {
        return curMem;
    }

    public String getCur() {
        return cur;
    }

    public void reset() {
        replaceRegister();
    }

    public void setRegisterPos(String varName, Register register) {
        registerMap.put(varName, register);
    }

    public Register getRegisterPos(String varName) {
        return registerMap.get(varName);
    }

    public void addArgRegister(String name) {

    }

    public void addMemoryAddress(String name) {

    }

    public void buildMapping() {}

    @Deprecated
    public Register loadToRegister(String name) {
        Atom location;
        Atom argRegister = assemblyBuilder.getArgRegister(name);
        if(argRegister != null) {
            location = argRegister;
        } else {
            location = assemblyBuilder.getMemory(name);
        }

        if(!location.equals(curMem)) {
            replaceRegister();

            if(argRegister != null) {
                assemblyBuilder.moveData(new Register("v1"), argRegister);
            } else {
                assemblyBuilder.moveData(new Register("v1"), location);
            }
            curMem = location;
            cur = name;
        }
        return new Register("v1");
    }

    public Register loadToRegister2x(IR3Dot ref) {
        IR3Atom left = ref.getLeft();
        IR3Atom right = ref.getRight();

        Register register = getRegisterPos(left.toString());

        Type type = assemblyBuilder.getType(left.getValue());
        Integer offset = assemblyBuilder.getClassFieldOffset(type.getLabel(), right.getValue());

        Register target = new Register("v6");
        assemblyBuilder.addLoadMemory(target, new Memory(register, new AsmInteger(offset)));
        return target;
    }

    public void storePtr(IR3Dot ref, Atom data)  {
        IR3Atom left = ref.getLeft();
        IR3Atom right = ref.getRight();

        Register register = getRegisterPos(left.toString());

        Type type = assemblyBuilder.getType(left.getValue());
        Integer offset = assemblyBuilder.getClassFieldOffset(type.getLabel(), right.getValue());
        assemblyBuilder.addStoreMemory((Register) data, new Memory(register, new AsmInteger(offset)));
    }

    public void replaceRegister() {
        if (curMem != null) {
            assemblyBuilder.moveData(curMem, new Register("v1"));
        }
        curMem = null;
    }
}
