package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment1.utils.Labelable;
import dev.aguss787.cs4212.assignment1.utils.Pair;

import java.util.*;

public enum Operator implements Labelable {
    PLUS("+", Arrays.asList(gen(PrimitiveType.INT, PrimitiveType.INT, PrimitiveType.INT))),
    MINUS("-", Arrays.asList(gen(PrimitiveType.INT, PrimitiveType.INT, PrimitiveType.INT))),
    DIV("/", Arrays.asList(gen(PrimitiveType.INT, PrimitiveType.INT, PrimitiveType.INT))),
    MUL("*", Arrays.asList(gen(PrimitiveType.INT, PrimitiveType.INT, PrimitiveType.INT))),
    LT("<", Arrays.asList(gen(PrimitiveType.INT, PrimitiveType.INT, PrimitiveType.BOOL))),
    LTQ("<=", Arrays.asList(gen(PrimitiveType.INT, PrimitiveType.INT, PrimitiveType.BOOL))),
    GT(">", Arrays.asList(gen(PrimitiveType.INT, PrimitiveType.INT, PrimitiveType.BOOL))),
    GTQ(">=", Arrays.asList(gen(PrimitiveType.INT, PrimitiveType.INT, PrimitiveType.BOOL))),
    EQ("==", Arrays.asList(gen(PrimitiveType.INT, PrimitiveType.INT, PrimitiveType.BOOL))),
    NEQ("!=", Arrays.asList(gen(PrimitiveType.INT, PrimitiveType.INT, PrimitiveType.BOOL))),
    AND("&&", Arrays.asList(gen(PrimitiveType.BOOL, PrimitiveType.BOOL, PrimitiveType.BOOL))),
    OR("||", Arrays.asList(gen(PrimitiveType.BOOL, PrimitiveType.BOOL, PrimitiveType.BOOL))),
    NEG("!", Arrays.asList(gen(PrimitiveType.BOOL, PrimitiveType.BOOL, PrimitiveType.BOOL)));

    final Map<Pair<Type, Type>, Type> availableOperand;
    final String label;

    Operator(String label, List<Pair<Pair<Type, Type>, Type>> types) {
        this.availableOperand = new HashMap<>();
        this.label = label;
        types.forEach(t -> availableOperand.put(t.getFirst(), t.getSecond()));
    }

    public Optional<Type> operate(Type a, Type b) {
        Pair<Type, Type> operands = new Pair<>(a, b);
        if (availableOperand.containsKey(operands)) {
            return Optional.of(availableOperand.get(operands));
        }
        return Optional.empty();
    }

    private static Pair<Pair<Type, Type>, Type> gen(Type a, Type b, Type ret) {
        return new Pair<>(new Pair<>(a, b), ret);
    }

    @Override
    public String getLabel() {
        return label;
    }
}
