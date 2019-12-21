package dev.aguss787.cs4212.assignment1.cup;

import java_cup.runtime.Symbol;

public class PrettySymbol extends Symbol {
    public PrettySymbol(int id, Symbol left, Symbol right, Object o) {
        super(id, left, right, o);
    }

    public PrettySymbol(int id, Symbol left, Symbol right) {
        super(id, left, right);
    }

    public PrettySymbol(int id, Symbol left, Object o) {
        super(id, left, left, o);
    }

    public PrettySymbol(int id, int l, int r, Object o) {
        super(id, l, r, o);
    }

    public PrettySymbol(int id, Object o) {
        super(id, o);
    }

    public PrettySymbol(int id, int l, int r) {
        super(id, l, r);
    }

    public PrettySymbol(int sym_num) {
        super(sym_num);
    }

    public String toString() {
        return "#" + Symbols.terminalNames[this.sym] + "(" + value + ", " + left + ", " + right + ")";
    }

}
