package dev.aguss787.cs4212.assignment3.asm.Atom;

import java.util.Objects;

public class AsmInteger implements Atom {
    private final Integer value;

    public AsmInteger(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AsmInteger integer = (AsmInteger) o;
        return Objects.equals(value, integer.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "#" + value.toString();
    }
}
