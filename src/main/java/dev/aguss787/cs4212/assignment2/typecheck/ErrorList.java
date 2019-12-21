package dev.aguss787.cs4212.assignment2.typecheck;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ErrorList extends ArrayList<Error> {
    public boolean addIfNotNull(Error e) {
        if (e != null) {
            this.add(e);
        }
        return e != null;
    }

    public boolean addIfNotNull(List<Error> e) {
        if (e != null) {
            e.stream().filter(Objects::nonNull).forEach(this::add);
        }
        return e != null;
    }
}
