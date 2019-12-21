package dev.aguss787.cs4212.assignment2.typecheck;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UniqueReducer<U> {
    private Set<U> st = new HashSet<>();
    private ArrayList<U> duplicate = new ArrayList<>();

    public void reduce(U u) {
        if(!st.add(u)) {
            duplicate.add(u);
        }
    }

    public ArrayList<U> getDuplicate() {
        return duplicate;
    }

    public void reset() {
        st.clear();
        duplicate.clear();
    }
}
