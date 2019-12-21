package dev.aguss787.cs4212.assignment2.IR3;

import java.util.ArrayList;
import java.util.List;

public class IR3CallableAtom extends IR3Atom {
    private final String caller;

    public IR3CallableAtom(String caller, String method) {
        super(method);
        this.caller = caller;
    }

    public String getCaller() {
        return caller;
    }

    public String getMethod() {
        return this.getValue();
    }

    public List<String> getUsedVariable() {
        return new ArrayList<>(){{ add(caller); }};
    }
}
