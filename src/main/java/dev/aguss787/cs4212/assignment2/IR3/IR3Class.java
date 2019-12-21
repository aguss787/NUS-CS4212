package dev.aguss787.cs4212.assignment2.IR3;

import dev.aguss787.cs4212.assignment1.ast.CustomClass;
import dev.aguss787.cs4212.assignment1.ast.Declaration;
import dev.aguss787.cs4212.assignment1.ast.Function;
import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;

import java.util.ArrayList;

public class IR3Class {
    private final IR3Builder ir3Builder;
    private final String name;

    private final ArrayList<Declaration> declarations;
    private final ArrayList<IR3Function> functions = new ArrayList<>();

    public IR3Class(IR3Builder ir3Builder, CustomClass customClass) {
        this.ir3Builder = ir3Builder;
        this.name = customClass.getName();
        this.declarations = customClass.getDeclarations();
    }

    public ArrayList<Declaration> getDeclarations() {
        return declarations;
    }

    public ArrayList<IR3Function> getFunctions() {
        return functions;
    }

    public String getName() {
        return name;
    }

    public String getNewLabel() {
        return ir3Builder.getNewLabel();
    }

    public IR3Atom getNewIdentifier() {
        return ir3Builder.getNewIdentifier();
    }

    public String getClassFunction(String className, String fname) {
        return ir3Builder.getClassFunction(className, fname);
    }

    public IR3Function putFunction(Function function) {
        IR3Function ir3Function = new IR3Function(this, function);
        functions.add(ir3Function);
        return ir3Function;
    }

    public void buildDeclarationString(StringBuilder stringBuilder) {
        stringBuilder
                .append("class ")
                .append(name)
                .append("{\n");
        declarations.stream()
                .map(d -> String.format("    %s %s;\n", d.getType().getLabel(), d.getName()))
                .forEach(stringBuilder::append);
        stringBuilder
                .append("}\n\n");
    }

    public void buildFunctionString(StringBuilder stringBuilder) {
        functions.forEach(f -> f.buildString(stringBuilder));
    }

    public void buildAsm(AssemblyBuilder assemblyBuilder) {
        functions.forEach(f -> f.buildAsm(assemblyBuilder));
    }
}
