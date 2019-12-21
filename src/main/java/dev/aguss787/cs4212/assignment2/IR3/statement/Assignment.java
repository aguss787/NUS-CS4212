package dev.aguss787.cs4212.assignment2.IR3.statement;

import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Dot;
import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;
import dev.aguss787.cs4212.assignment3.asm.Atom.Memory;
import dev.aguss787.cs4212.assignment3.asm.Atom.Register;

import java.util.ArrayList;
import java.util.List;

public class Assignment extends Statement {
    private final IR3Atom target;
    private IR3Atom right;

    public Assignment(IR3Atom target, IR3Atom right) {
        this.target = target;
        this.right = right;
    }

    public IR3Atom getTarget() {
        return target;
    }

    public IR3Atom getRight() {
        return right;
    }

    public void setRight(IR3Atom right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return String.format("%s = %s;", target.toString(), right.toString());
    }

    @Override
    public void buildAsm(AssemblyBuilder assemblyBuilder) {
        Atom rightAtom = right.getAsmReference(assemblyBuilder);
//        assemblyBuilder.moveData(new Register("a2"), rightAtom);

        Atom targetAtom = target.getAsmReference(assemblyBuilder);
        if (targetAtom instanceof Memory) {
            targetAtom = assemblyBuilder.loadFromMemory(target.getValue().toString());
        }

        assemblyBuilder.moveData(targetAtom, rightAtom);

        if(target instanceof IR3Dot) {
            assemblyBuilder.storePtr((IR3Dot) target, targetAtom);
        }
    }

    @Override
    public List<String> getInvolvedVariable() {
        List<String> res = target.getInvolvedVariable();
        res.addAll(right.getInvolvedVariable());
        return res;
    }

    @Override
    public List<String> getUsedVariable() {
        List<String> res = new ArrayList<>();
        if(target instanceof IR3Dot) {
            res = target.getUsedVariable();
        }
        res.addAll(right.getUsedVariable());
        return res;
    }

    @Override
    public List<String> getWrittenVariable() {
        if(target instanceof IR3Dot) {
            return new ArrayList<>();
        }
        return target.getUsedVariable();
    }
}
