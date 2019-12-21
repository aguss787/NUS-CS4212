package dev.aguss787.cs4212.assignment3;

import dev.aguss787.cs4212.assignment1.ast.Program;
import dev.aguss787.cs4212.assignment1.cup.Parser;
import dev.aguss787.cs4212.assignment1.cup.SymbolFactories;
import dev.aguss787.cs4212.assignment1.jflex.Lexer;
import dev.aguss787.cs4212.assignment2.IR3.IR3Builder;
import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.optimizer.Optimizer;
import dev.aguss787.cs4212.assignment3.register.Allocation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File(args[0]);
        FileReader fileReader = new FileReader(file);

        boolean optimized = false;
        for(int i = 1; i < args.length; i++) {
            if(args[i].equals("-O")) {
                optimized = true;
            }
        }

        Lexer lexer = new Lexer(fileReader);
        Parser parser = new Parser(lexer, new SymbolFactories());

        Program program = (Program)parser.parse().value;

        ArrayList<Error> errors = program.check();

        for(Error e : errors) {
            System.out.println(e.toString());
        }

        if (errors.size() > 0) {
            return;
        }

        IR3Builder ir3Builder = program.buildIR3();

        if(optimized) {
            Optimizer.preReg(ir3Builder);
        }
        Allocation.go(ir3Builder);
        if(optimized) {
            Optimizer.postReg(ir3Builder);
        }

        System.out.println(ir3Builder.toString());

        AssemblyBuilder assemblyBuilder = ir3Builder.toAsm();
        String asm = assemblyBuilder.toString();
        System.out.println(asm);

        try {
            Files.write(Paths.get("output.s"), asm.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
