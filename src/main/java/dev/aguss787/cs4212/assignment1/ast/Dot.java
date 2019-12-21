package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3CallableAtom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Dot;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.typecheck.*;

import static dev.aguss787.cs4212.assignment2.typecheck.TypeCheck.getTypeName;

public class Dot implements Referenceable {
    private final Atom left;
    private final String right;
    private Type savedType;

    public Dot(Atom left, String right) {
        this.left = left;
        this.right = right;
    }

    public Atom getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }

    @Override
    public String prettyPrint(int indent) {
        return indent(indent) + "Dot{" + "\n" +
                indent(indent + 2) + "left=\n" + left.prettyPrint(indent + 4) + "\n" +
                indent(indent + 2) + ", right='" + right + '\'' + "\n" +
                indent(indent) + '}';
    }

    @Override
    public TypeWithErrors checkWithType(TypeMap typeMap, LocalEnv localEnv) {
        TypeWithErrors leftResult = left.checkWithType(typeMap, localEnv);
        if (leftResult.hasError()) {
            return leftResult;
        }

        TypeDescriptor typeDescriptor = typeMap.get(getTypeName(leftResult.getType()));
        if (!typeDescriptor.containsKey(right)) {
            ErrorList errors = new ErrorList();
            errors.addIfNotNull(new Error(String.format(
                    "\"%s\" does not have field \"%s\" [%s]",
                    getTypeName(leftResult.getType()),
                    right,
                    getLabel()
            )));
            return new TypeWithErrors(errors);
        }

        savedType = typeDescriptor.get(right);
        return new TypeWithErrors(typeDescriptor.get(right));
    }

    @Override
    public Type getSavedType() {
        return savedType;
    }

    @Override
    public String getLabel() {
        return left.getLabel() + "." + right;
    }

    @Override
    public IR3Atom buildIR3Expression(IR3Function ir3Function) {
        if (savedType instanceof FunctionSignature) {
            return new IR3CallableAtom(left.buildIR3Expression(ir3Function).toString(), getIR3Reference(ir3Function));
        }
        IR3Atom id = ir3Function.getNewIdentifier(savedType);
        ir3Function.putAssignment(
                id,
                new IR3Dot(left.buildIR3Expression(ir3Function), new IR3Atom(getIR3Reference(ir3Function)))
        );
        return id;
    }

    @Override
    public IR3Atom buildIR3Reference(IR3Function ir3Function) {
        return new IR3Dot(left.buildIR3Expression(ir3Function), new IR3Atom(getIR3Reference(ir3Function)));
    }

    private String getIR3Reference(IR3Function ir3Function) {
        if (savedType instanceof FunctionSignature) {
            // This assume that the left is a class.
            return ir3Function.getClassFunction(((ClassType) left.getSavedType()).getName(), right);
        } else {
            return right;
        }
    }
}
