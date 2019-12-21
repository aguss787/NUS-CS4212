package dev.aguss787.cs4212.assignment3.register.utils;

import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.IR3.statement.Goto;
import dev.aguss787.cs4212.assignment2.IR3.statement.Jump;
import dev.aguss787.cs4212.assignment2.IR3.statement.Label;
import dev.aguss787.cs4212.assignment2.IR3.statement.Statement;

import java.util.*;
import java.util.stream.IntStream;

public class IR3Graph {
    private final Map<Integer, Integer> blockMap;
    private final Map<Label, Integer> labelIdx;
    private final Map<Label, List<Integer>> invertedLabelIdx;
    private final Map<String, List<Integer>> variableUsage;
    private final Map<Integer, Map<String, Integer>> nextVariableUsage;

    private final List<Statement> statements;

    public IR3Graph(IR3Function ir3Function) {
        blockMap = new HashMap<>();
        labelIdx = new HashMap<>();
        variableUsage = new HashMap<>();
        invertedLabelIdx = new HashMap<>();
        nextVariableUsage = new HashMap<>();

        statements = ir3Function.getStatements();
        label(statements);
        listVar(statements);

        variableUsage.keySet()
                .forEach(var -> getNextVarUsage(statements, var));
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public Map<Integer, Integer> getBlockMap() {
        return blockMap;
    }

    public Map<Label, Integer> getLabelIdx() {
        return labelIdx;
    }

    public Map<Label, List<Integer>> getInvertedLabelIdx() {
        return invertedLabelIdx;
    }

    public Map<String, List<Integer>> getVariableUsage() {
        return variableUsage;
    }

    public Map<Integer, Map<String, Integer>> getNextVariableUsage() {
        return nextVariableUsage;
    }

    public List<Integer> getNext(int cur) {
        Statement statement = statements.get(cur);
        List<Integer> res = new ArrayList<>();
        if (statement instanceof Jump) {
            int nxt = getLabelIdx().get(((Jump) statement).getLabel());
            res.add(nxt);
        }

        if (!(statement instanceof Goto) && cur + 1 < statements.size()) {
            int nxt = cur + 1;
            res.add(nxt);
        }
        return res;
    }

    private void label(List<Statement> statements) {
        label(statements, 0, 0);
    }

    private void label(List<Statement> statements, int idx, int cntr) {
        if (idx >= statements.size()) {
            return;
        }

        if (statements.get(idx) instanceof Label) {
            labelIdx.put((Label) statements.get(idx), idx);
            blockMap.put(idx, ++cntr);
        } else if (statements.get(idx) instanceof Goto) {
            Goto gotos = (Goto) statements.get(idx);
            List<Integer> idxs = invertedLabelIdx.getOrDefault(gotos.getLabel(), new ArrayList<>());
            idxs.add(idx);
            invertedLabelIdx.put(gotos.getLabel(), idxs);
            blockMap.put(idx, cntr++);
        } else {
            blockMap.put(idx, cntr);
        }

        label(statements, idx + 1, cntr);
    }

    private void listVar(List<Statement> statements) {
        IntStream.range(0, statements.size())
                .forEach(i -> {
                    statements.get(i).getInvolvedVariable().forEach(var -> {
                        List<Integer> v = variableUsage.getOrDefault(var, new ArrayList<>());
                        v.add(i);
                        variableUsage.put(var, v);
                    });
                });
    }

    private void getNextVarUsage(List<Statement> statements, String var) {
        nextVariableUsage.values().stream()
                .filter(c -> c.containsKey(var))
                .forEach(c -> c.remove(var));

        IntStream.range(0, statements.size())
                .filter(i -> !nextVariableUsage.containsKey(i))
                .forEach(i -> nextVariableUsage.put(i, new HashMap<>()));

        Queue<Integer> q = new LinkedList<>(variableUsage.getOrDefault(var, new ArrayList<>()));
        int separator = statements.size();
        q.add(separator);
        int dist = 1;
        boolean wasSeparator = true;
        while (!q.isEmpty()) {
            int current = q.poll();
            if (current == separator) {
                if (wasSeparator) {
                    break;
                }
                dist++;
                q.add(separator);
                wasSeparator = true;
                continue;
            }

            wasSeparator = false;

            Map<String, Integer> tmp = nextVariableUsage.getOrDefault(current, new HashMap<>());
            tmp.put(var, dist);
            nextVariableUsage.put(current, tmp);

            Statement statement = statements.get(current);
            if (statement instanceof Label) {
                invertedLabelIdx.getOrDefault((Label) statement, new ArrayList<>())
                        .stream()
                        .filter(nxt -> nextVariableUsage.get(nxt).containsKey(var))
                        .forEach(q::add);
            }

            if (current - 1 >= 0) {
                int nxt = current - 1;
                if (!nextVariableUsage.get(nxt).containsKey(var)
                        && !(statements.get(nxt) instanceof Goto)) {
                    q.add(nxt);
                }
            }
        }
    }
}
