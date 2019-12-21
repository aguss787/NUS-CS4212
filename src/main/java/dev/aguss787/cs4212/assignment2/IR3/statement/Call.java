package dev.aguss787.cs4212.assignment2.IR3.statement;

import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3CallableAtom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Integer;
import dev.aguss787.cs4212.assignment2.IR3.IR3String;
import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.AsmInteger;
import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;
import dev.aguss787.cs4212.assignment3.asm.Atom.Memory;
import dev.aguss787.cs4212.assignment3.asm.Atom.Register;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Call extends Statement {
    private final IR3CallableAtom func;
    private final List<IR3Atom> args;


    public Call(IR3CallableAtom func, List<IR3Atom> args) {
        this.func = func;
        this.args = args;
    }

    @Override
    public String toString() {
        return String.format("%s(%s%s);", func.getMethod(), func.getCaller(), args.stream()
                .map(IR3Atom::toString)
                .map(s -> ", " + s)
                .collect(Collectors.joining())
        );
    }

    @Override
    public void buildAsm(AssemblyBuilder assemblyBuilder) {
        if (args.size() <= 3) {
            Register register = assemblyBuilder.loadFromMemory(func.getCaller());
            assemblyBuilder.moveData(new Register("a", 1), register);
            for (int i = 0; i < args.size(); i++) {
                assemblyBuilder.moveData(new Register("a" + Integer.toString(i + 2)),
                        args.get(i).getAsmReference(assemblyBuilder));
            }
            assemblyBuilder.addCallWithLink(func.getMethod());
        } else {
            Register register = assemblyBuilder.loadFromMemory(func.getCaller());
            assemblyBuilder.moveData(new Memory(new Register("sp"), new AsmInteger(-28)),
                    register);
            for (int i = 0; i < args.size(); i++) {
                Register tmp = new Register("a1");
                Atom val = args.get(i).getAsmReference(assemblyBuilder);
                assemblyBuilder.moveData(tmp, val);
                assemblyBuilder.moveData(new Memory(new Register("sp"), new AsmInteger(-28 - 4*(i + 1))),
                        tmp);
            }
            assemblyBuilder.addCallWithLink(func.getMethod());
        }
    }

    @Override
    public List<String> getInvolvedVariable() {
        List<String> res = func.getUsedVariable();
        args.stream()
                .map(IR3Atom::getUsedVariable)
                .forEach(res::addAll);
        return res;
    }

    @Override
    public List<String> getUsedVariable() {
        List<String> res = new ArrayList<>();
        args.stream()
                .map(IR3Atom::getUsedVariable)
                .forEach(res::addAll);
        return res;
    }
}
