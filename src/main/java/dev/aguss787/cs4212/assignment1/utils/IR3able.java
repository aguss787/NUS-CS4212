package dev.aguss787.cs4212.assignment1.utils;

import dev.aguss787.cs4212.assignment2.IR3.IR3Builder;

public interface IR3able {
    IR3Builder buildIR3(IR3Builder ir3Builder);

    default IR3Builder buildIR3() {
        return this.buildIR3(new IR3Builder());
    }
}
