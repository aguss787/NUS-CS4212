package dev.aguss787.cs4212.assignment3.asm.Atom;

import java.util.Objects;

public class Register implements Atom {
    private final String value;

    public Register(String value) {
        this.value = value;
    }

    public Register(String prefix, int idx) {
        this.value = prefix + Integer.toString(idx);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Register register = (Register) o;
        return Objects.equals(value, register.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
