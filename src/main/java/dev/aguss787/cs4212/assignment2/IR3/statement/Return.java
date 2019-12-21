package dev.aguss787.cs4212.assignment2.IR3.statement;

import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.Register;
import dev.aguss787.cs4212.assignment3.asm.statement.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Return extends Statement {
    private final Optional<IR3Atom> value;

    public Return(IR3Atom value) {
        this.value = Optional.of(value);
    }

    public Return() {
        this.value = Optional.empty();
    }

    @Override
    public String toString() {
        if (value.isPresent()) {
            return String.format("return %s;", value.get().toString());
        }
        return "return;";
    }

    @Override
    public void buildAsm(AssemblyBuilder assemblyBuilder) {
        value.ifPresent(ir3Atom -> assemblyBuilder.moveData(new Register("a1"), ir3Atom.getAsmReference(assemblyBuilder)));
        assemblyBuilder.addJump(assemblyBuilder.getReturnLabel());
    }

    @Override
    public List<String> getInvolvedVariable() {
        if (value.isPresent()) {
            return value.get().getUsedVariable();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> getUsedVariable() {
        return getInvolvedVariable();
    }

    @Override
    public List<String> getWrittenVariable() {
        return new ArrayList<>();
    }
}
