package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment1.utils.IR3Referenceable;
import dev.aguss787.cs4212.assignment1.utils.Labelable;
import dev.aguss787.cs4212.assignment1.utils.TypeCheckable;

public interface Referenceable extends Atom, Labelable, TypeCheckable, IR3Referenceable {
}
