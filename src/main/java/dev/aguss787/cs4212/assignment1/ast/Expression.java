package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment1.utils.IR3Expression;
import dev.aguss787.cs4212.assignment1.utils.Labelable;
import dev.aguss787.cs4212.assignment1.utils.PrettyPrintable;
import dev.aguss787.cs4212.assignment1.utils.TypeCheckable;

public interface Expression extends PrettyPrintable, Labelable, TypeCheckable, IR3Expression {
}
