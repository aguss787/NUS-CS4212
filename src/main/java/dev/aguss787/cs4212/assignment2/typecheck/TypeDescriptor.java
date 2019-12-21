package dev.aguss787.cs4212.assignment2.typecheck;

import dev.aguss787.cs4212.assignment1.ast.Type;

import java.util.HashMap;
import java.util.Map;

public class TypeDescriptor extends HashMap<String, Type> {
    public void addProp(String id, Type props) {
        this.put(id, props);
    }
}
