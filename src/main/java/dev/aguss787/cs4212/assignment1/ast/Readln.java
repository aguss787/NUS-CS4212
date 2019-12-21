package dev.aguss787.cs4212.assignment1.ast;

import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.typecheck.ErrorList;
import dev.aguss787.cs4212.assignment2.typecheck.LocalEnv;
import dev.aguss787.cs4212.assignment2.typecheck.TypeMap;
import dev.aguss787.cs4212.assignment2.typecheck.TypeWithErrors;

import java.util.Arrays;
import java.util.List;

import static dev.aguss787.cs4212.assignment2.typecheck.TypeCheck.getTypeName;

public class Readln implements Statement {
    public final List<Type> READABLE_TYPE = Arrays.asList(PrimitiveType.INT, PrimitiveType.BOOL, PrimitiveType.STRING);
    private final String id;

    public Readln(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Readln{" +
                "id='" + id + '\'' +
                '}';
    }

    @Override
    public ErrorList check(TypeMap typeMap, LocalEnv localEnv) {
        ErrorList errors = new ErrorList();
        errors.addIfNotNull(localEnv.containsKey(id));
        if (errors.size() > 0) {
            return errors;
        }

        Type type = localEnv.get(id);
        if (!READABLE_TYPE.contains(type)) {
            errors.addIfNotNull(new Error(String.format(
                    "\"%s\" [%s] is not writeable",
                    getTypeName(type),
                    id
            )));
            return errors;
        }

        return errors;
    }

    @Override
    public void buildIR3Statement(IR3Function ir3Function) {
        ir3Function.putReadln(id);
    }
}
