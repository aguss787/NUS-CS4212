package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment1.utils.IR3Classable;
import dev.aguss787.cs4212.assignment1.utils.LocalCheckable;
import dev.aguss787.cs4212.assignment1.utils.PrettyPrintable;
import dev.aguss787.cs4212.assignment2.IR3.IR3Builder;
import dev.aguss787.cs4212.assignment2.IR3.IR3Class;
import dev.aguss787.cs4212.assignment2.typecheck.*;

import java.util.ArrayList;
import java.util.Optional;

public class CustomClass implements PrettyPrintable, LocalCheckable, IR3Classable {
    private final String name;
    private final ArrayList<Declaration> declarations;
    private final ArrayList<Function> functions;

    private Error error;

    public CustomClass(Error error) {
        this.name = null;
        this.declarations = null;
        this.functions = null;
        this.error = error;
        throw this.error;
    }

    public CustomClass(String name, ArrayList<Declaration> declarations, ArrayList<Function> functions) {
        this.name = name;
        this.declarations = Optional.ofNullable(declarations).orElseGet(ArrayList::new);
        this.functions = Optional.ofNullable(functions).orElseGet(ArrayList::new);;
        this.error = null;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Declaration> getDeclarations() {
        return declarations;
    }

    public ArrayList<Function> getFunctions() {
        return functions;
    }

    public Error getError() {
        return error;
    }

    public CustomClass asMain() {
        if(functions.size() != 1) {
            this.error = new Error("Main should only have one method");
            throw this.error;
        }

        if(!functions.get(0).getName().equals("main")) {
            this.error = new Error("Main should only have a \"main\" method");
            throw this.error;
        }

        if(functions.get(0).getReturns() != PrimitiveType.VOID) {
            this.error = new Error("Main method should return Void");
            throw this.error;
        }

        return this;
    }

    @Override
    public String prettyPrint(int indent) {
        if (error != null) {
            return indent(indent) + error.toString();
        }
        return indent(indent) + "CustomClass{" + "\n" + indent(indent + 2) +
                "name='" + name + '\'' + "\n" + indent(indent + 2) +
                ", declarations=" + prettyPrintList(indent + 4, declarations) + "\n" + indent(indent + 2) +
                ", functions=" + prettyPrintList(indent + 4, functions) + "\n" + indent(indent) +
                '}';
    }

    public ErrorList check(TypeMap typeMap) {
        ErrorList errors = new ErrorList();

        // Init local env
        LocalEnv localEnv = new LocalEnv();

        // Mount declared variable
        declarations.forEach(d -> localEnv.put(d.getName(), d.getType()));
        declarations.stream()
                .map(Declaration::getType)
                .map(TypeCheck::getTypeName)
                .map(typeMap::containsKey)
                .forEach(errors::addIfNotNull);

        // Mount 'this'
        localEnv.put("this", new ClassType(this.name));
        functions.forEach(f -> localEnv.put(f.getName(), new FunctionSignature(f)));

        if (errors.size() > 0) {
            return errors;
        }

        // Check each function
        functions.stream()
                .map(f -> f.check(typeMap, localEnv))
                .forEach(errors::addIfNotNull);

        return errors;
    }

    @Override
    public void buildIR3Class(IR3Builder ir3Builder) {
        IR3Class ir3Class = ir3Builder.putClass(this);
        functions.forEach(f -> f.buildIR3Function(ir3Class));
    }
}
