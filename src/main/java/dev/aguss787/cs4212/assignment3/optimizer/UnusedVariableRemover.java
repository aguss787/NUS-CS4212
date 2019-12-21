package dev.aguss787.cs4212.assignment3.optimizer;

import dev.aguss787.cs4212.assignment2.IR3.IR3Dot;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.IR3.statement.Assignment;
import dev.aguss787.cs4212.assignment2.IR3.statement.Statement;
import dev.aguss787.cs4212.assignment3.register.utils.IR3Graph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UnusedVariableRemover {
    private final Integer UNSET = -1;

    public UnusedVariableRemover() {

    }

    public void optimize(IR3Function ir3Function) {
        IR3Graph ir3Graph = new IR3Graph(ir3Function);
        Set<Integer> used = new HashSet<>();
        Map<Integer, Set<Integer>> visited = new HashMap<>();

        IntStream.range(0, ir3Graph.getStatements().size())
                .forEach(i -> visited.put(i, new HashSet<>()));

        go(ir3Graph, visited, used, 0, UNSET);

        ir3Function.setStatements(
                IntStream.range(0, ir3Function.getStatements().size()).boxed()
                        .filter(i -> !(ir3Function.getStatements().get(i) instanceof Assignment)
                                || used.contains(i))
                        .map(ir3Function.getStatements()::get)
                        .collect(Collectors.toList())
        );
    }

    private void go(IR3Graph ir3Graph,
                    Map<Integer, Set<Integer>> visited,
                    Set<Integer> used,
                    int cur,
                    int key) {
        if (visited.get(cur).contains(key)
                || used.contains(cur)) {
            return;
        }
        visited.get(cur).add(key);
        Statement statement = ir3Graph.getStatements().get(cur);
        if (key == UNSET) {
            if (statement instanceof Assignment) {
                if(((Assignment) statement).getTarget() instanceof IR3Dot) {
                    used.add(cur);
                } else {
                    go(ir3Graph, visited, used, cur, cur);
                }
            }
        } else if (cur != key) {
            Assignment keyStatement = (Assignment) ir3Graph.getStatements().get(key);
            List<String> writtenVar = keyStatement.getWrittenVariable();

            if (statement instanceof Assignment
                    && ((Assignment) statement).getTarget().equals(
                    ((Assignment) ir3Graph.getStatements().get(key)).getTarget())) {
                return;
            }

            if (statement.getWrittenVariable().containsAll(writtenVar)) {
                return;
            }

            Set<String> usedVar = new HashSet<>(statement.getUsedVariable());
            if (usedVar.containsAll(writtenVar)) {
                used.add(key);
                return;
            }
        }

        ir3Graph.getNext(cur).forEach(i -> go(ir3Graph, visited, used, i, key));
    }
}
