package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment1.utils.IR3able;
import dev.aguss787.cs4212.assignment1.utils.PrettyPrintable;
import dev.aguss787.cs4212.assignment1.utils.TopCheckable;
import dev.aguss787.cs4212.assignment2.IR3.IR3Builder;
import dev.aguss787.cs4212.assignment2.typecheck.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static dev.aguss787.cs4212.assignment2.typecheck.TypeCheck.getTypeName;

public class Program implements PrettyPrintable, TopCheckable, IR3able {
    private final ArrayList<CustomClass> classes;

    public Program(ArrayList<CustomClass> classes) {
        this.classes = classes;
    }

    public ArrayList<CustomClass> getClasses() {
        return classes;
    }

    public String prettyPrint(int indent) {
        return indent(indent) + "Program{" + "\n" + indent(indent + 2) +
                "classes=" + prettyPrintList(indent + 4, classes) + "\n" + indent(indent) +
                '}';
    }

    public ErrorList check() {
        // Init
        ErrorList errors = new ErrorList();
        TypeMap typeMap = new TypeMap();

        // Primitive type
        errors.addIfNotNull(typeMap.putOnce("INT", new TypeDescriptor()));
        errors.addIfNotNull(typeMap.putOnce("BOOL", new TypeDescriptor()));
        errors.addIfNotNull(typeMap.putOnce("VOID", new TypeDescriptor()));
        errors.addIfNotNull(typeMap.putOnce("STRING", new TypeDescriptor()));

        // New classes
        // Class name
        classes.forEach(c -> {
            errors.addIfNotNull(typeMap.putOnce(c.getName(), new TypeDescriptor()));
        });

        if (errors.size() > 0) {
            return errors;
        }

        // Class function and field
        classes.forEach(c -> {
            TypeDescriptor typeDescriptor = typeMap.get(c.getName());
            UniqueReducer<String> uniqueReducer = new UniqueReducer<>();
            c.getFunctions().forEach(f -> {
                errors.addIfNotNull(f.check(typeMap));
                typeDescriptor.addProp(f.getName(), new FunctionSignature(f));
                uniqueReducer.reduce(f.getName());
            });
            uniqueReducer.getDuplicate()
                    .forEach(d -> errors.add(new Error("Function \"" + d + "\" already declared")));
            uniqueReducer.getDuplicate().clear();

            c.getDeclarations().forEach(f -> {
                errors.addIfNotNull(typeMap.containsKey(getTypeName(f.getType())));
                typeDescriptor.addProp(f.getName(), f.getType());
                uniqueReducer.reduce(f.getName());
            });
            uniqueReducer.getDuplicate()
                    .forEach(d -> errors.add(new Error("Field \"" + d + "\" already declared")));
        });

        if (errors.size() > 0) {
            return errors;
        }

        // Check each class
        classes.stream()
                .map(c -> c.check(typeMap))
                .forEach(errors::addIfNotNull);

        return errors;
    }

    @Override
    public IR3Builder buildIR3(IR3Builder ir3Builder) {
        // register all classes
        classes.forEach(ir3Builder::registerClass);
        classes.forEach(c -> c.buildIR3Class(ir3Builder));
        return ir3Builder;
    }
}
