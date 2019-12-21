package dev.aguss787.cs4212.assignment1.utils;

import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;

public interface IR3Expression {
    IR3Atom buildIR3Expression(IR3Function ir3Function);
}
