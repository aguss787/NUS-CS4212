package dev.aguss787.cs4212.assignment3.optimizer;

import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.IR3.statement.Return;
import dev.aguss787.cs4212.assignment3.register.utils.IR3Graph;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DeadCodeRemover {
    public void optimize(IR3Function ir3Function) {
        IR3Graph ir3Graph = new IR3Graph(ir3Function);
        Set<Integer> visited = new HashSet<>();

        go(ir3Graph, visited, 0);

        ir3Function.setStatements(
                IntStream.range(0, ir3Function.getStatements().size()).boxed()
                    .filter(visited::contains)
                    .map(ir3Function.getStatements()::get)
                    .collect(Collectors.toList())
        );
    }

    private void go(IR3Graph ir3Graph,
                    Set<Integer> visited,
                    int cur) {
        if (visited.contains(cur)) {
            return;
        }
        visited.add(cur);
        if(ir3Graph.getStatements().get(cur) instanceof Return) {
            return;
        }
        ir3Graph.getNext(cur).forEach(i -> go(ir3Graph, visited, i));
    }
}
