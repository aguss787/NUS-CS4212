package dev.aguss787.cs4212.assignment2.typecheck;

import java.util.HashMap;

public class TypeMap extends HashMap<String, TypeDescriptor> {
    public Error putOnce(String id, TypeDescriptor typeDescriptor) {
        if (super.containsKey(id)) {
            return new Error("Type \"" + id + "\" already exist");
        }
        this.put(id, typeDescriptor);
        return null;
    }

    public Error containsKey(String id) {
        if(super.containsKey(id)) {
            return null;
        }
        return new Error("Type \"" + id + "\" doesn't exist");
    }
}
