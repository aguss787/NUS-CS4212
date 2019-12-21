package dev.aguss787.cs4212.assignment2.typecheck;

import dev.aguss787.cs4212.assignment1.ast.Type;

import java.util.HashMap;

public class LocalEnv extends HashMap<String, Type> {
    private String ret;

    public Error containsKey(String id) {
        if(super.containsKey(id)) {
            return null;
        }
        return new Error("Variable \"" + id + "\" not declared");
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }
}
