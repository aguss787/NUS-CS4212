package dev.aguss787.cs4212.assignment2;

import dev.aguss787.cs4212.assignment1.ast.Program;
import dev.aguss787.cs4212.assignment1.cup.Parser;
import dev.aguss787.cs4212.assignment1.cup.SymbolFactories;
import dev.aguss787.cs4212.assignment1.jflex.Lexer;
import dev.aguss787.cs4212.assignment2.IR3.IR3Builder;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File(args[0]);
        FileReader fileReader = new FileReader(file);

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
        System.out.println(ir3Builder.toString());
    }
}
