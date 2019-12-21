package dev.aguss787.cs4212.assignment2.IR3;

import dev.aguss787.cs4212.assignment1.ast.CustomClass;
import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IR3Builder {
    private final Map<String, IdentifierHelper<String>> classIdentifiers;
    private final ArrayList<IR3Class> classes;

    private int labelCounter;
    private final IdentifierHelper<Integer> labels;

    private int identifierCounter;
    private final IdentifierHelper<Integer> identifiers;

    public ArrayList<IR3Class> getClasses() {
        return classes;
    }

    public IR3Builder() {
        classIdentifiers = new HashMap<>();
        classes = new ArrayList<>();

        labelCounter = 0;
        labels = new IdentifierHelper<>("_label");

        identifierCounter = 0;
        identifiers = new IdentifierHelper<>("_id");
    }

    public String getNewLabel() {
        return labels.getIdentifier(labelCounter++);
    }

    public IR3Atom getNewIdentifier() {
        return new IR3Atom(identifiers.getIdentifier(identifierCounter++));
    }

    public String getClassFunction(String className, String fname) {
        return classIdentifiers.get(className).getIdentifier(fname);
    }

    public void registerClass(CustomClass customClass) {
        String className = customClass.getName();
        classIdentifiers.put(className, new IdentifierHelper<>("_" + className));
    }

    public IR3Class putClass(CustomClass customClass) {
        IR3Class ir3Class =  new IR3Class(this, customClass);
        classes.add(ir3Class);
        return ir3Class;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        classes.forEach(c -> c.buildDeclarationString(stringBuilder));
        classes.forEach(c -> c.buildFunctionString(stringBuilder));

        return stringBuilder.toString();
    }

    public AssemblyBuilder toAsm() {
        AssemblyBuilder assemblyBuilder = new AssemblyBuilder();
        classes.forEach(assemblyBuilder::registerClass);
        classes.forEach(c -> c.buildAsm(assemblyBuilder));
        assemblyBuilder.addMain(classes.get(0));
        return assemblyBuilder;
    }

}
