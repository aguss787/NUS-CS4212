package dev.aguss787.cs4212.assignment3.optimizer;

import dev.aguss787.cs4212.assignment2.IR3.IR3Builder;
import dev.aguss787.cs4212.assignment2.IR3.IR3Class;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment3.asm.Constant.Constant;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Optimizer {
    public static void preReg(IR3Builder ir3Builder) {
        List<IR3Function> functions =  ir3Builder.getClasses()
                .stream()
                .map(IR3Class::getFunctions)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        DeadCodeRemover deadCodeRemover = new DeadCodeRemover();
        ConstantFolder constantFolder = new ConstantFolder();
        UnusedVariableRemover unusedVariableRemover = new UnusedVariableRemover();

        functions.forEach(deadCodeRemover::optimize);
        functions.forEach(constantFolder::optimize);
        functions.forEach(unusedVariableRemover::optimize);
        functions.forEach(deadCodeRemover::optimize);
    }

    public static void postReg(IR3Builder ir3Builder) {
        Stream<IR3Function> functions =  ir3Builder.getClasses()
                .stream()
                .map(IR3Class::getFunctions)
                .flatMap(List::stream);

        LoadReducer loadReducer = new LoadReducer();
        functions.forEach(loadReducer::optimize);
    }
}
