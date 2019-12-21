package dev.aguss787.cs4212.assignment3.register;

import dev.aguss787.cs4212.assignment1.ast.ClassType;
import dev.aguss787.cs4212.assignment1.ast.Declaration;
import dev.aguss787.cs4212.assignment1.ast.Type;
import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Dot;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.typecheck.TypeDescriptor;
import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.Atom.AsmInteger;
import dev.aguss787.cs4212.assignment3.asm.Atom.Atom;
import dev.aguss787.cs4212.assignment3.asm.Atom.Memory;
import dev.aguss787.cs4212.assignment3.asm.Atom.Register;

import java.util.HashMap;
import java.util.Map;

public class StackManager {
    private final OffsetManager offsetManager;
    private final AssemblyBuilder assemblyBuilder;
    private final Integer totalSize;
    private final TypeDescriptor type;
    private final Map<String, Register> argsMap;

    public StackManager(IR3Function ir3Function, AssemblyBuilder assemblyBuilder, ClassManager classManager) {
        this.assemblyBuilder = assemblyBuilder;
        offsetManager = new OffsetManager();
        type = new TypeDescriptor();

        int currentOffset = 0;

        argsMap = new HashMap<>();
        int cntr = 2;
        type.put("this", new ClassType(ir3Function.getClassName()));
        argsMap.put("this", new Register("a1"));
        offsetManager.put("this", currentOffset);
        currentOffset += 4;

        for(Declaration decl: ir3Function.getArgs()) {
            type.put(decl.getName(), decl.getType());
            offsetManager.put(decl.getName(), currentOffset);
            currentOffset += 4;
            argsMap.put(decl.getName(), new Register("a", cntr));
            cntr++;
        }
        for(Declaration declaration: ir3Function.getDeclarations()) {
            offsetManager.put(declaration.getName(), currentOffset);
            currentOffset += 4;
        }

        for (Declaration declaration: ir3Function.getCompleteDeclarations()) {
            type.put(declaration.getName(), declaration.getType());
        }

        totalSize = currentOffset;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public Integer getOffset(String name) {
        return -offsetManager.get(name);
    }

    public Type getType(String name) {
        return type.get(name);
    }

    public Atom getArgRegister(String name) {
        if(offsetManager.containsKey(name)) {
            return new Memory(new Register("fp"), new AsmInteger(getOffset(name)));
        }
        return new IR3Dot(new IR3Atom("this"), new IR3Atom(name)).getAsmReference(assemblyBuilder);
    }

    public void moveArgsToStack() {
        if(argsMap.size() > 4) {
            return;
        }

        for(String i: argsMap.keySet()) {
            Integer offset = getOffset(i);
            assemblyBuilder.moveData(new Memory(new Register("fp"), new AsmInteger(offset)), argsMap.get(i));
        }
    }
}
