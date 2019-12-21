package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.typecheck.ErrorList;
import dev.aguss787.cs4212.assignment2.typecheck.LocalEnv;
import dev.aguss787.cs4212.assignment2.typecheck.TypeMap;
import dev.aguss787.cs4212.assignment2.typecheck.TypeWithErrors;

import java.util.ArrayList;

public class IfElse implements Statement {
    private final Expression cond;
    private final ArrayList<Statement> ifTrue;
    private final ArrayList<Statement> ifFalse;

    public IfElse(Expression cond, ArrayList<Statement> ifTrue, ArrayList<Statement> ifFalse) {
        this.cond = cond;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    public Expression getCond() {
        return cond;
    }

    public ArrayList<Statement> getIfTrue() {
        return ifTrue;
    }

    public ArrayList<Statement> getIfFalse() {
        return ifFalse;
    }

    @Override
    public String prettyPrint(int indent) {
        return indent(indent) + "IfElse{\n" +
                indent(indent + 2) +  "cond=\n" + cond.prettyPrint(indent + 4) + "\n" +
                indent(indent + 2) +  ", ifTrue=" + prettyPrintList(indent + 4, ifTrue) + "\n" +
                indent(indent + 2) +  ", ifFalse=" + prettyPrintList(indent + 4, ifFalse) + "\n" +
                indent(indent) + '}';
    }

    @Override
    public ErrorList check(TypeMap typeMap, LocalEnv localEnv) {
        ErrorList errors = new ErrorList();
        TypeWithErrors condResult = cond.checkWithType(typeMap, localEnv);
        errors.addIfNotNull(condResult.getErrors());
        if(errors.size() > 0) {
            return errors;
        }
        if (!condResult.getType().equals(PrimitiveType.BOOL)) {
            errors.addIfNotNull(new Error("If condition must be BOOL ["+ cond.getLabel() +"]"));
        }
        ifTrue.stream()
                .map(s -> s.check(typeMap, localEnv))
                .forEach(errors::addIfNotNull);
        ifFalse.stream()
                .map(s -> s.check(typeMap, localEnv))
                .forEach(errors::addIfNotNull);
        return errors;
    }

    @Override
    public void buildIR3Statement(IR3Function ir3Function) {
        String label = ir3Function.getNewLabel();
        String endLabel = ir3Function.getNewLabel();

        ir3Function.putIf(cond.buildIR3Expression(ir3Function), label);

        ifFalse.forEach(s -> s.buildIR3Statement(ir3Function));
        ir3Function.putGoto(endLabel);

        ir3Function.putLabel(label);
        ifTrue.forEach(s -> s.buildIR3Statement(ir3Function));

        ir3Function.putLabel(endLabel);
    }
}
