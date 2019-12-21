package dev.aguss787.cs4212.assignment1.ast;

public enum PrimitiveType implements Type {
    INT,
    STRING,
    BOOL,
    VOID,
    NULL;

    @Override
    public String getLabel() {
        String str = this.toString();
        return str.substring(0, 1) + str.substring(1).toLowerCase();
    }
}
