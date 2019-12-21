package dev.aguss787.cs4212.assignment1;

import dev.aguss787.cs4212.assignment1.ast.Program;
import dev.aguss787.cs4212.assignment1.cup.Parser;
import dev.aguss787.cs4212.assignment1.cup.SymbolFactories;
import dev.aguss787.cs4212.assignment1.jflex.Lexer;

import java.io.File;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File(args[0]);
        FileReader fileReader = new FileReader(file);

        Lexer lexer = new Lexer(fileReader);
        Parser parser = new Parser(lexer, new SymbolFactories());
        System.out.println(((Program)parser.parse().value).prettyPrint());
    }
}
