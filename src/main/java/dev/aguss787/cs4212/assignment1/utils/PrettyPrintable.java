package dev.aguss787.cs4212.assignment1.utils;

import java.util.List;

public interface PrettyPrintable {
    default String indent(int indent) {
        StringBuilder str = new StringBuilder();
        for(int i = 0 ; i < indent; i ++) {
            str.append(" ");
        }
        return str.toString();
    }
    default String prettyPrintList(int indent, List<? extends PrettyPrintable> list) {
        StringBuilder stringBuilder = new StringBuilder();
        if (list != null) {
            for(PrettyPrintable declaration: list) {
                stringBuilder.append("\n" + declaration.prettyPrint(indent + 2));
            }
        }
        return stringBuilder.toString();
    }
    default String prettyPrint() {
        return prettyPrint(0);
    }
    default String prettyPrint(int indent) {
        return indent(indent) + toString();
    }
}
