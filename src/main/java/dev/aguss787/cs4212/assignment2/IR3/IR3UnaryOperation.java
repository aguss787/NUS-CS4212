package dev.aguss787.cs4212.assignment2.IR3;

import dev.aguss787.cs4212.assignment1.ast.Operator;
import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.AsmInteger;
import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;
import dev.aguss787.cs4212.assignment3.asm.Atom.Register;

import java.util.ArrayList;
import java.util.List;

public class IR3UnaryOperation extends IR3Atom {
    private final Operator operator;
    private final IR3Atom operand;

    public IR3UnaryOperation(Operator operator, IR3Atom operand) {
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public String toString() {
        return String.format("%s%s", operator.getLabel(), operand);
    }

    @Override
    public String getValue() {
        return toString();
    }

    @Override
    public Atom getAsmReference(AssemblyBuilder assemblyBuilder) {
        Atom res = operand.getAsmReference(assemblyBuilder);
        Atom target = new Register("a3");

        if(res instanceof Register) {
            target = res;
        }

        if (operator == Operator.NEG) {
            assemblyBuilder.addMov(new Register("a4"), new AsmInteger(1));
            assemblyBuilder.addSub(target, new Register("a4"), res);
            return target;
        }

        if (operator == Operator.MINUS) {
            assemblyBuilder.addMov(new Register("a4"), new AsmInteger(0));
            assemblyBuilder.addSub(target, new Register("a4"), res);
            return target;
        }

        return null;
    }

    public List<String> getUsedVariable() {
        return operand.getUsedVariable();
    }

    @Override
    public List<String> getInvolvedVariable() {
        return operand.getInvolvedVariable();
    }
}
