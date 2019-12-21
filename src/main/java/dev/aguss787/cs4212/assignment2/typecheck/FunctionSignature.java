package dev.aguss787.cs4212.assignment2.typecheck;

import dev.aguss787.cs4212.assignment1.ast.Declaration;
import dev.aguss787.cs4212.assignment1.ast.Function;
import dev.aguss787.cs4212.assignment1.ast.Type;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class FunctionSignature implements Type {
    private final Type returns;
    private final ArrayList<Declaration> arguments;

    public FunctionSignature(Function function) {
        this.returns = function.getReturns();
        this.arguments = function.getArguments();
    }

    public Type getReturns() {
        return returns;
    }

    public ArrayList<Declaration> getArguments() {
        return arguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionSignature that = (FunctionSignature) o;
        return Objects.equals(returns, that.returns) &&
                Objects.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(returns, arguments);
    }

    @Override
    public String toString() {
        String args = arguments.stream().map(Declaration::getType).map(Type::toString).collect(Collectors.joining(","));
        return returns.toString() + "(" + args + ")";
    }

    @Override
    public String getLabel() {
        return toString();
    }
}
