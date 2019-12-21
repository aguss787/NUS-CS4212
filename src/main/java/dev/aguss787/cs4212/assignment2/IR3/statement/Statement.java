package dev.aguss787.cs4212.assignment2.IR3.statement;

import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract public class Statement {
    public String getSpacing() {
        return "    ";
    }

    public void buildAsm(AssemblyBuilder assemblyBuilder) {
        String className = this.getClass().getName();
        throw new Error(className + " Not Implemented");
    }

    public List<String> getInvolvedVariable() {
        return new ArrayList<>();
    }

    public List<String> getUsedVariable() {
        return getInvolvedVariable();
    }

    public List<String> getWrittenVariable() {
        return new ArrayList<>();
    }
}
