package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment1.utils.Labelable;
import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Call;
import dev.aguss787.cs4212.assignment2.IR3.IR3CallableAtom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.typecheck.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Call implements Atom, Statement {
    private final Atom fun;
    private final ArrayList<Expression> args;

    Type savedType;

    public Call(Atom fun, ArrayList<Expression> args) {
        this.fun = fun;
        this.args = args;
    }

    public Atom getFun() {
        return fun;
    }

    public ArrayList<Expression> getArgs() {
        return args;
    }

    @Override
    public String prettyPrint(int indent) {
        return indent(indent) + "Call{\n" +
                indent(indent + 2) + "fun=\n" + fun.prettyPrint(indent + 4) + "\n" +
                indent(indent + 2) + ", args=" + prettyPrintList(indent + 4, args) + "\n" +
                indent(indent) + '}';
    }

    @Override
    public TypeWithErrors checkWithType(TypeMap typeMap, LocalEnv localEnv) {
        TypeWithErrors funResult = fun.checkWithType(typeMap, localEnv);
        List<TypeWithErrors> argsResult = args.stream()
                .map(e -> e.checkWithType(typeMap, localEnv))
                .collect(Collectors.toList());

        ErrorList errors = new ErrorList();
        errors.addIfNotNull(funResult.getErrors());
        argsResult.stream().map(TypeWithErrors::getErrors).forEach(errors::addIfNotNull);
        if (errors.size() > 0) {
            return new TypeWithErrors(errors);
        }

        Type funType = funResult.getType();
        if (!(funType instanceof FunctionSignature)) {
            errors.addIfNotNull(new Error(String.format(
                    "\"%s\" is not callable [%s]",
                    funType,
                    getLabel()
            )));
            return new TypeWithErrors(errors);
        }

        FunctionSignature functionSignature = (FunctionSignature) funType;
        List<Type> funcArgs = functionSignature.getArguments().stream()
                .map(Declaration::getType)
                .collect(Collectors.toList());
        List<Type> actualArgs = argsResult.stream()
                .map(TypeWithErrors::getType)
                .collect(Collectors.toList());

        if (!funcArgs.equals(actualArgs)) {
            errors.addIfNotNull(new Error(String.format(
                    "Function with signature (%s) not found [%s]",
                    actualArgs.stream().map(Labelable::getLabel).collect(Collectors.joining(",")),
                    getLabel()
            )));
            return new TypeWithErrors(errors);
        }

        savedType = functionSignature.getReturns();
        return new TypeWithErrors(functionSignature.getReturns());
    }

    @Override
    public Type getSavedType() {
        return savedType;
    }

    @Override
    public ErrorList check(TypeMap typeMap, LocalEnv localEnv) {
        return checkWithType(typeMap, localEnv).getErrors();
    }

    @Override
    public String getLabel() {
        return fun.getLabel() + "("
                + args.stream().map(Expression::getLabel).collect(Collectors.joining(","))
                + ")";
    }


    @Override
    public void buildIR3Statement(IR3Function ir3Function) {
        ir3Function.putCall(
                (IR3CallableAtom) fun.buildIR3Expression(ir3Function),
                args.stream().map(e -> e.buildIR3Expression(ir3Function)).collect(Collectors.toList())
        );
    }

    @Override
    public IR3Atom buildIR3Expression(IR3Function ir3Function) {
        IR3Atom id = ir3Function.getNewIdentifier(savedType);
        ir3Function.putAssignment(id,
                new IR3Call((IR3CallableAtom) fun.buildIR3Expression(ir3Function),
                        args.stream().map(e -> e.buildIR3Expression(ir3Function)).collect(Collectors.toList())
        ));
        return id;
    }


}
