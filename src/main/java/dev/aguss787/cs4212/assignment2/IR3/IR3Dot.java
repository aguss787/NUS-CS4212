package dev.aguss787.cs4212.assignment2.IR3;

import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;
import dev.aguss787.cs4212.assignment3.asm.Atom.Register;
import dev.aguss787.cs4212.assignment3.asm.statement.Str;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IR3Dot extends IR3Atom {
    private final IR3Atom left;
    private final IR3Atom right;

    public IR3Dot(IR3Atom left, IR3Atom right) {
        super("");
        this.left = left;
        this.right = right;
    }

    public IR3Atom getLeft() {
        return left;
    }

    public IR3Atom getRight() {
        return right;
    }

    @Override
    public String toString() {
        return String.format("%s.%s", left.toString(), right.toString());
    }

    @Override
    public String getValue() {
        return toString();
    }

    @Override
    public Atom getAsmReference(AssemblyBuilder assemblyBuilder) {
        return assemblyBuilder.loadFromMemory2x(this);
    }

    @Override
    public List<String> getUsedVariable() {
        return left.getInvolvedVariable();
//        List<String> res = new ArrayList<>();
//        res.add(toString());
//        return res;
    }

    @Override
    public List<String> getInvolvedVariable() {
        return left.getInvolvedVariable();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IR3Dot ir3Dot = (IR3Dot) o;
        return Objects.equals(left, ir3Dot.left) &&
                Objects.equals(right, ir3Dot.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
