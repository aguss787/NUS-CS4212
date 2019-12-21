package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.typecheck.ErrorList;
import dev.aguss787.cs4212.assignment2.typecheck.LocalEnv;
import dev.aguss787.cs4212.assignment2.typecheck.TypeMap;
import dev.aguss787.cs4212.assignment2.typecheck.TypeWithErrors;

import java.util.ArrayList;
import java.util.stream.Stream;

public class While implements Statement {
    private final Expression cond;
    private final ArrayList<Statement> statements;

    public While(Expression cond, ArrayList<Statement> statements) {
        this.cond = cond;
        this.statements = statements;
    }

    public Expression getCond() {
        return cond;
    }

    public ArrayList<Statement> getStatements() {
        return statements;
    }

    @Override
    public String prettyPrint(int indent) {
        return indent(indent) + "While{\n" +
                indent(indent + 2) +  "cond=\n" + cond.prettyPrint(indent + 4) + "\n" +
                indent(indent + 2) + ", statements=" + prettyPrintList(indent + 4, statements) + "\n" +
                indent(indent) + '}';
    }

    public ErrorList check(TypeMap typeMap, LocalEnv localEnv) {
        ErrorList errors = new ErrorList();
        TypeWithErrors condResult = cond.checkWithType(typeMap, localEnv);
        errors.addIfNotNull(condResult.getErrors());
        if (!condResult.getType().equals(PrimitiveType.BOOL)) {
            errors.addIfNotNull(new Error("While condition [" + cond.getLabel() + "] must be BOOL"));
        }
        statements.stream()
                .map(s -> s.check(typeMap, localEnv))
                .forEach(errors::addIfNotNull);
        return errors;
    }

    @Override
    public void buildIR3Statement(IR3Function ir3Function) {
        String label = ir3Function.getNewLabel();
        String endlabel = ir3Function.getNewLabel();
        ir3Function.putLabel(label);
        UnaryOperation neg = new UnaryOperation(cond, Operator.NEG);
        neg.setSavedType(PrimitiveType.BOOL);
        ir3Function.putIf(neg.buildIR3Expression(ir3Function), endlabel);
        statements.forEach(s -> s.buildIR3Statement(ir3Function));
        ir3Function.putGoto(label);
        ir3Function.putLabel(endlabel);
    }
}
