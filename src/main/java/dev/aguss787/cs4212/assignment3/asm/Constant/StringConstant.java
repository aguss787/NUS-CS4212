package dev.aguss787.cs4212.assignment3.asm.Constant;

import dev.aguss787.cs4212.assignment3.asm.statement.Dot;
import dev.aguss787.cs4212.assignment3.asm.statement.Label;
import dev.aguss787.cs4212.assignment3.asm.statement.Statement;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringConstant implements Constant {
    private final String label;
    private final String str;

    public StringConstant(String label, String str) {
        this.label = label;
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        Statement[] statements = new Statement[]{
                new Dot("data"),
                new Label(label),
                new Dot("asciz", str),
        };

        return Arrays.stream(statements).map(Statement::toString).collect(Collectors.joining("\n"));
    }
}
