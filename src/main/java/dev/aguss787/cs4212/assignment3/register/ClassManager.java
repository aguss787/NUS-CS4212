package dev.aguss787.cs4212.assignment3.register;

import dev.aguss787.cs4212.assignment1.ast.Declaration;
import dev.aguss787.cs4212.assignment1.ast.Type;
import dev.aguss787.cs4212.assignment2.IR3.IR3Class;
import dev.aguss787.cs4212.assignment2.typecheck.TypeDescriptor;
import dev.aguss787.cs4212.assignment2.typecheck.TypeMap;

import java.util.HashMap;
import java.util.Map;

public class ClassManager {
    private final Map<String, OffsetManager> offsetManagerMap;
    private final Map<String, Integer> size;
    private final Map<String, TypeDescriptor> fieldMap;

    public ClassManager() {
        offsetManagerMap = new HashMap<>();
        size = new HashMap<>();
        fieldMap = new HashMap<>();
    }
    
    public void addClass(IR3Class ir3Class) {
        OffsetManager offsetManager = new OffsetManager();
        offsetManagerMap.put(ir3Class.getName(), offsetManager);

        TypeDescriptor typeDescriptor = new TypeDescriptor();
        fieldMap.put(ir3Class.getName(), typeDescriptor);

        int currentOffset = 0;
        for(Declaration declaration: ir3Class.getDeclarations()) {
            offsetManager.put(declaration.getName(), currentOffset);
            typeDescriptor.put(declaration.getName(), declaration.getType());
            currentOffset += 4;
        }

        size.put(ir3Class.getName(), currentOffset);
    }

    public Integer getSize(String name) {
        return size.get(name);
    }

    public Type getField(String name, String field) {
        TypeDescriptor typeDescriptor = fieldMap.get(name);
        if (typeDescriptor == null) {
            return null;
        }
        return typeDescriptor.get(field);
    }

    public Integer getFieldOffset(String name, String field) {
        OffsetManager offsetManager = offsetManagerMap.get(name);
        if (offsetManager == null) {
            return null;
        }
        return offsetManager.get(field);
    }
}
