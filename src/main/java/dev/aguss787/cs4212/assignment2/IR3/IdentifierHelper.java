package dev.aguss787.cs4212.assignment2.IR3;

import java.util.HashMap;
import java.util.Map;

public class IdentifierHelper<U> {
    private int counter;
    private final String prefix;
    private final Map<U, Integer> identifierMap;

    public IdentifierHelper(String prefix) {
        this.prefix = prefix;
        this.identifierMap = new HashMap<>();
        counter = 0;
    }

    public String getIdentifier(U id) {
        int res = counter + 1;
        if (identifierMap.containsKey(id)) {
            res = identifierMap.get(id);
        } else {
            counter = res;
            identifierMap.put(id, res);
        }
        return prefix + res;
    }

    public boolean hasIdentifier(U id) {
        return identifierMap.containsKey(id);
    }
}
