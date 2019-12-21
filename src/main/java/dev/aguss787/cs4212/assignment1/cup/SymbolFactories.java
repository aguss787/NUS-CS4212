package dev.aguss787.cs4212.assignment1.cup;

import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;

public class SymbolFactories implements SymbolFactory {
    public Symbol newSymbol(String var1, int var2, Symbol var3, Symbol var4, Object var5){
        return new PrettySymbol(var2, var3, var4, var5);
    }

    public Symbol newSymbol(String var1, int var2, Symbol var3, Symbol var4){
        return new PrettySymbol(var2, var3, var4);
    }

    public Symbol newSymbol(String var1, int var2, Symbol var3, Object var4){
        return new PrettySymbol(var2, var3, var4);
    }

    public Symbol newSymbol(String var1, int var2, Object var3){
        return new PrettySymbol(var2, var3);
    }

    public Symbol newSymbol(String var1, int var2){
        return new PrettySymbol(var2);
    }

    public Symbol startSymbol(String var1, int var2, int var3){
        Symbol s = new PrettySymbol(var2, var3);
        s.parse_state = var3;
        return s;
    }
}
