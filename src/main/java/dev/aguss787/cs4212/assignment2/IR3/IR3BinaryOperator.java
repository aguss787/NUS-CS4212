package dev.aguss787.cs4212.assignment2.IR3;

import dev.aguss787.cs4212.assignment1.ast.Operator;
import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;
import dev.aguss787.cs4212.assignment3.asm.Atom.Register;

import java.util.List;
import java.util.Map;

public class IR3BinaryOperator extends IR3Atom {
    private final IR3Atom left;
    private final Operator operator;
    private final IR3Atom right;

    public IR3BinaryOperator(IR3Atom left, Operator operator, IR3Atom right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", left, operator.getLabel(), right);
    }

    @Override
    public String getValue() {
        return toString();
    }

    @Override
    public List<String> getUsedVariable() {
        List<String> res = left.getUsedVariable();
        res.addAll(right.getUsedVariable());
        return res;
    }

    @Override
    public List<String> getInvolvedVariable() {
        List<String> res = left.getInvolvedVariable();
        res.addAll(right.getInvolvedVariable());
        return res;
    }

    @Override
    public Atom getAsmReference(AssemblyBuilder assemblyBuilder) {
        Atom leftAtom = new Register("a1");
        assemblyBuilder.moveData(leftAtom, left.getAsmReference(assemblyBuilder));
        Atom rightAtom = right.getAsmReference(assemblyBuilder);

        if (operator == Operator.OR) {
            assemblyBuilder.addOr(leftAtom, leftAtom, rightAtom);
            return leftAtom;
        }

        if (operator == Operator.AND) {
            assemblyBuilder.addAnd(leftAtom, leftAtom, rightAtom);
            return leftAtom;
        }

        if (operator == Operator.EQ) {
            assemblyBuilder.addEq(leftAtom, leftAtom, rightAtom);
            return leftAtom;
        }

        if (operator == Operator.NEQ) {
            assemblyBuilder.addNeq(leftAtom, leftAtom, rightAtom);
            return leftAtom;
        }

        if (operator == Operator.LT) {
            assemblyBuilder.addCmp("lt", leftAtom, leftAtom, rightAtom);
            return leftAtom;
        }

        if (operator == Operator.LTQ) {
            assemblyBuilder.addCmp("le", leftAtom, leftAtom, rightAtom);
            return leftAtom;
        }
        if (operator == Operator.GT) {
            assemblyBuilder.addCmp("gt", leftAtom, leftAtom, rightAtom);
            return leftAtom;
        }

        if (operator == Operator.GTQ) {
            assemblyBuilder.addCmp("ge", leftAtom, leftAtom, rightAtom);
            return leftAtom;
        }

        if (operator == Operator.PLUS) {
            assemblyBuilder.addAdd(leftAtom, leftAtom, rightAtom);
            return leftAtom;
        }

        if (operator == Operator.MINUS) {
            assemblyBuilder.addSub(leftAtom, leftAtom, rightAtom);
            return leftAtom;
        }

        if (operator == Operator.MUL) {
            Register tmp = new Register("a2");
            assemblyBuilder.moveData(tmp, rightAtom);
            assemblyBuilder.addMul(leftAtom, leftAtom, tmp);
            return leftAtom;
        }

        if (operator == Operator.DIV) {
            Register tmp = new Register("a2");
            assemblyBuilder.moveData(tmp, rightAtom);
            assemblyBuilder.addDiv(leftAtom, leftAtom, tmp);
            return leftAtom;
        }


        return null;
    }

    @Override
    public IR3Atom eval(Map<String, IR3Atom> map) {
        if (operator == Operator.OR) {
            return new IR3Bool(((IR3Bool) left.eval(map)).asBoolean() || ((IR3Bool) right.eval(map)).asBoolean());
        }

        if (operator == Operator.AND) {
            return new IR3Bool(((IR3Bool) left.eval(map)).asBoolean() && ((IR3Bool) right.eval(map)).asBoolean());
        }

        if (operator == Operator.EQ) {
            return new IR3Bool(((IR3Integer) left.eval(map)).asInteger().equals(((IR3Integer) right.eval(map)).asInteger()));
        }

        if (operator == Operator.NEQ) {
            return new IR3Bool(!((IR3Integer) left.eval(map)).asInteger().equals(((IR3Integer) right.eval(map)).asInteger()));
        }

        if (operator == Operator.LT) {
            return new IR3Bool(((IR3Integer) left.eval(map)).asInteger() < ((IR3Integer) right.eval(map)).asInteger());
        }

        if (operator == Operator.LTQ) {
            return new IR3Bool(((IR3Integer) left.eval(map)).asInteger() <= ((IR3Integer) right.eval(map)).asInteger());
        }
        if (operator == Operator.GT) {
            return new IR3Bool(((IR3Integer) left.eval(map)).asInteger() > ((IR3Integer) right.eval(map)).asInteger());
        }

        if (operator == Operator.GTQ) {
            return new IR3Bool(((IR3Integer) left.eval(map)).asInteger() >= ((IR3Integer) right.eval(map)).asInteger());
        }

        if (operator == Operator.PLUS) {
            return new IR3Integer(((IR3Integer) left.eval(map)).asInteger() + ((IR3Integer) right.eval(map)).asInteger());
        }

        if (operator == Operator.MINUS) {
            return new IR3Integer(((IR3Integer) left.eval(map)).asInteger() - ((IR3Integer) right.eval(map)).asInteger());
        }

        if (operator == Operator.MUL) {
            return new IR3Integer(((IR3Integer) left.eval(map)).asInteger() * ((IR3Integer) right.eval(map)).asInteger());
        }

        if (operator == Operator.DIV) {
            return new IR3Integer(((IR3Integer) left.eval(map)).asInteger() / ((IR3Integer) right.eval(map)).asInteger());
        }


        return null;
    }
}
