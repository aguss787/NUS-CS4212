package dev.aguss787.cs4212.assignment2.IR3;

import dev.aguss787.cs4212.assignment1.ast.Declaration;
import dev.aguss787.cs4212.assignment1.ast.Function;
import dev.aguss787.cs4212.assignment1.ast.Type;
import dev.aguss787.cs4212.assignment2.IR3.statement.*;
import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.register.StackManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IR3Function {
    private final String className;
    private final String name;
    private final Function func;
    private final IR3Class ir3Class;

    private final List<Declaration> completeDeclarations = new ArrayList<>();
    private List<Declaration> declarations = new ArrayList<>();
    private List<Statement> statements = new ArrayList<>();

    public List<Statement> getStatements() {
        return statements;
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }

    public void setDeclarations(List<Declaration> declarations) {
        this.declarations = declarations;
    }

    public IR3Function(IR3Class ir3Class, Function func) {
        this.func = func;
        this.ir3Class = ir3Class;
        this.name = func.getName();
        this.className = ir3Class.getName();
    }

    public List<Declaration> getArgs() {
        return func.getArguments();
    }

    public String getName() {
        return name;
    }

    public List<Declaration> getCompleteDeclarations() {
        return completeDeclarations;
    }

    public List<Declaration> getDeclarations() {
        return declarations;
    }

    public String getClassName() {
        return className;
    }

    public String getNewLabel() {
        return ir3Class.getNewLabel();
    }

    public void putDeclaration(Declaration d) {
        declarations.add(d);
        completeDeclarations.add(d);
    }

    public IR3Atom getNewIdentifier(Type t) {
        IR3Atom ir3Atom = ir3Class.getNewIdentifier();
        putDeclaration(new Declaration(t, ir3Atom.getValue()));
        return ir3Atom;
    }

    public String getFunction(String fname) {
        return ir3Class.getClassFunction(className, fname);
    }

    public String getClassFunction(String className, String fname) {
        return ir3Class.getClassFunction(className, fname);
    }

    public void putLabel(String label) {
        statements.add(new Label(label));
    }

    public void putIf(IR3Atom cond, String label) {
        statements.add(new If(cond, label));
    }

    public void putGoto(String label) {
        statements.add(new Goto(label));
    }

    public void putReadln(String expr) {
        statements.add(new SysCall("readln", new IR3Atom(expr)));
    }

    public void putPrintln(IR3Atom expr) {
        statements.add(new SysCall("println", expr));
    }

    public void putAssignment(IR3Atom target, IR3Atom expr) {
        statements.add(new Assignment(target, expr));
    }

    public void putCall(IR3CallableAtom func, List<IR3Atom> args) {
        statements.add(new Call(func, args));
    }

    public String buildCall(IR3CallableAtom func, List<IR3Atom> args) {
        return new Call(func, args).toString().replaceFirst(".$","");
    }

    public String buildNew(String name) {
        return String.format("new %s()", name);
    }

    public void putReturn(IR3Atom expr) {
        statements.add(new Return(expr));
    }

    public void putReturn() {
        statements.add(new Return());
    }

    public String getIR3Name() {
        return getClassFunction(className, name);
    }

    public void buildString(StringBuilder stringBuilder) {
        String fname = getClassFunction(className, name);
        stringBuilder
                .append(String.format("%s %s(%s %s%s) {\n",
                        func.getReturns().getLabel(),
                        fname,
                        className,
                        "this",
                        func.getArguments().stream()
                                .map(a -> String.format(", %s %s", a.getType().getLabel(), a.getName()))
                                .collect(Collectors.joining())
                ));
        declarations.stream()
                .map(d -> String.format("    %s %s;\n", d.getType().getLabel(), d.getName()))
                .forEach(stringBuilder::append);
        if(declarations.size() > 0){
            stringBuilder.append("\n");
        }
        statements.stream()
                .map(s -> String.format("%s%s\n", s.getSpacing(), s.toString()))
                .forEach(stringBuilder::append);
        stringBuilder.append("}\n\n");
    }

    public void buildAsm(AssemblyBuilder assemblyBuilder) {
        StackManager stackManager = new StackManager(this, assemblyBuilder, assemblyBuilder.getClassManager());
        assemblyBuilder.setStackManager(stackManager);
        String fname = getClassFunction(className, name);
        assemblyBuilder.addFunctionHeader(fname, stackManager.getTotalSize());
        stackManager.moveArgsToStack();
        statements.forEach(s -> s.buildAsm(assemblyBuilder));
        assemblyBuilder.resetRegisterManager();
        assemblyBuilder.addFunctionFooter(fname, stackManager.getTotalSize());
    }
}
