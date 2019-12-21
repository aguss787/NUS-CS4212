package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.typecheck.ErrorList;
import dev.aguss787.cs4212.assignment2.typecheck.LocalEnv;
import dev.aguss787.cs4212.assignment2.typecheck.TypeMap;
import dev.aguss787.cs4212.assignment2.typecheck.TypeWithErrors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.aguss787.cs4212.assignment2.typecheck.TypeCheck.getTypeName;

public class Println implements Statement{
    public final List<Type> PRINTABLE_TYPE = Arrays.asList(PrimitiveType.INT, PrimitiveType.BOOL, PrimitiveType.STRING);
    private final Expression expr;

    public Println(Expression expr) {
        this.expr = expr;
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public String prettyPrint(int indent) {
        return indent(indent) + "Println{\n" +
                indent(indent + 2) + "expr=\n" + expr.prettyPrint(indent + 4) + "\n" +
                indent(indent) + '}';
    }

    @Override
    public ErrorList check(TypeMap typeMap, LocalEnv localEnv) {
        ErrorList errors = new ErrorList();
        TypeWithErrors exprResult = expr.checkWithType(typeMap, localEnv);
        if(exprResult.hasError()) {
            return exprResult.getErrors();
        }

        Type type = exprResult.getType();
        if (!PRINTABLE_TYPE.contains(type)) {
            errors.addIfNotNull(new Error(String.format(
                    "\"%s\" [%s] is not printable",
                    getTypeName(type),
                    expr.getLabel()
            )));
            return errors;
        }

        return errors;
    }

    @Override
    public void buildIR3Statement(IR3Function ir3Function) {
        ir3Function.putPrintln(expr.buildIR3Expression(ir3Function));
    }
}
