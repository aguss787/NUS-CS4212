package dev.aguss787.cs4212.assignment3.optimizer;

import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.IR3.statement.Statement;
import dev.aguss787.cs4212.assignment3.register.IR3AddOns.Load;
import dev.aguss787.cs4212.assignment3.register.utils.IR3Graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LoadReducer {
    private final Integer UNSET = -1;

    public LoadReducer() {

    }

    public void optimize(IR3Function ir3Function) {
        IR3Graph ir3Graph = new IR3Graph(ir3Function);
        Set<Integer> used = new HashSet<>();
        Map<Integer, Set<Integer>> visited = new HashMap<>();

        IntStream.range(0, ir3Graph.getStatements().size())
                .forEach(i -> visited.put(i, new HashSet<>()));

        go(ir3Graph, visited, used, 0, UNSET);

        IntStream.range(0, ir3Function.getStatements().size()).boxed()
                .filter(i -> ir3Function.getStatements().get(i) instanceof Load
                        && !used.contains(i))
                .map(ir3Function.getStatements()::get)
                .map(s -> (Load) s)
                .forEach(Load::setSetOnly);

        Set<String> usedStack = IntStream.range(0, ir3Function.getStatements().size()).boxed()
                .filter(used::contains)
                .map(ir3Function.getStatements()::get)
                .map(s -> (Load) s)
                .map(Load::getVarName)
                .collect(Collectors.toSet());

        ir3Function.setDeclarations(
                ir3Function.getDeclarations().stream()
                        .filter(d -> usedStack.contains(d.getName()))
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
            if (statement instanceof Load) {
                go(ir3Graph, visited, used, cur, cur);
            }
        } else if (cur != key) {
            Load keyStatement = (Load) ir3Graph.getStatements().get(key);

            if (statement instanceof Load
                    && ((Load) statement).getRegister().equals(
                    ((Load) ir3Graph.getStatements().get(key)).getRegister())) {
                return;
            }

            if (statement.getWrittenVariable().contains(keyStatement.getVarName())) {
                return;
            }

            Set<String> usedVar = new HashSet<>(statement.getUsedVariable());
            if (usedVar.contains(keyStatement.getVarName())) {
                used.add(key);
                return;
            }
        }

        ir3Graph.getNext(cur).forEach(i -> go(ir3Graph, visited, used, i, key));
    }
}
