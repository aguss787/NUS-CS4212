package dev.aguss787.cs4212.assignment3.asm.Atom;

import java.util.Objects;

public class Memory implements Atom {
    private final Atom base;
    private final Atom offset;

    public Memory(Atom base, Atom offset) {
        this.base = base;
        this.offset = offset;
    }

    public Memory(Atom base) {
        this.base = base;
        this.offset = null;
    }

    @Override
    public String toString() {
        if (this.offset == null) {
            return String.format("[%s]", base.toString());
        } else {
            return String.format("[%s, %s]", base.toString(), this.offset.toString());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Memory memory = (Memory) o;
        return Objects.equals(base, memory.base) &&
                Objects.equals(offset, memory.offset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, offset);
    }
}
