package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment1.utils.*;
import dev.aguss787.cs4212.assignment2.IR3.IR3Builder;
import dev.aguss787.cs4212.assignment2.IR3.IR3Class;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.typecheck.*;

import java.util.ArrayList;

import static dev.aguss787.cs4212.assignment2.typecheck.TypeCheck.getTypeName;

public class Function implements PrettyPrintable, LocalCheckable, Checkable, IR3Functionable {
    private final Type returns;
    private final String name;
    private final ArrayList<Declaration> arguments;
    private final ArrayList<Declaration> declarations;
    private final ArrayList<Statement> statements;

    public ArrayList<Declaration> getDeclarations() {
        return declarations;
    }

    public Function(Type returns, String name, ArrayList<Declaration> arguments, ArrayList<Declaration> declarations, ArrayList<Statement> statements) {
        this.returns = returns;
        this.name = name;
        this.arguments = arguments;
        this.declarations = declarations;
        this.statements = statements;
    }

    public Type getReturns() {
        return returns;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Declaration> getArguments() {
        return arguments;
    }

    public ArrayList<Statement> getStatements() {
        return statements;
    }

    @Override
    public String prettyPrint(int indent) {
        return indent(indent) + "Function{" + "\n" + indent(indent + 2) +
                "returns=" + returns.prettyPrint() + "\n" + indent(indent + 2) +
                ", name='" + name + '\'' + "\n" + indent(indent + 2) +
                ", arguments=" + prettyPrintList(indent + 4, arguments) + "\n" + indent(indent + 2) +
                ", declarations=" + prettyPrintList(indent + 4, declarations) + "\n" + indent(indent + 2) +
                ", statements=" + prettyPrintList(indent + 4, statements) + "\n" + indent(indent) +
                '}';
    }

    public ErrorList check(TypeMap typeMap) {
        ErrorList errors = new ErrorList();
        errors.addIfNotNull(typeMap.containsKey(getTypeName(returns)));
        arguments.stream()
                .map(Declaration::getType)
                .map(TypeCheck::getTypeName)
                .map(typeMap::containsKey)
                .forEach(errors::addIfNotNull);

        UniqueReducer<String> uniqueReducer = new UniqueReducer<>();
        arguments.stream()
                .map(Declaration::getName)
                .forEach(uniqueReducer::reduce);
        uniqueReducer.getDuplicate()
                .forEach(d -> errors.add(new Error("Variable \"" + d + "\" already declared")));

        return errors;
    }

    public ErrorList check(TypeMap typeMap, LocalEnv localEnv) {
        LocalEnv local = (LocalEnv)localEnv.clone();
        ErrorList errors = new ErrorList();

        // Mount args
        arguments.forEach(arg -> {
            local.put(arg.getName(), arg.getType());
        });

        declarations.forEach(arg -> {
            local.put(arg.getName(), arg.getType());
        });

        local.setRet(getTypeName(returns));

        statements.stream()
                .map(s -> s.check(typeMap, local))
                .forEach(errors::addIfNotNull);

        return errors;
    }

    @Override
    public void buildIR3Function(IR3Class ir3Class) {
        IR3Function ir3Function = ir3Class.putFunction(this);
        declarations.forEach(ir3Function::putDeclaration);
        statements.forEach(s -> s.buildIR3Statement(ir3Function));
    }
}
