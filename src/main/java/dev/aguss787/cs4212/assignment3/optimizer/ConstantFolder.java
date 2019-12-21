package dev.aguss787.cs4212.assignment3.optimizer;

import dev.aguss787.cs4212.assignment1.ast.ClassType;
import dev.aguss787.cs4212.assignment2.IR3.IR3Atom;
import dev.aguss787.cs4212.assignment2.IR3.IR3Dot;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.IR3.statement.Assignment;
import dev.aguss787.cs4212.assignment2.IR3.statement.Statement;
import dev.aguss787.cs4212.assignment3.asm.Constant.Constant;
import dev.aguss787.cs4212.assignment3.register.utils.IR3Graph;

import java.util.*;
import java.util.stream.Collectors;

public class ConstantFolder {
    public void optimize(IR3Function ir3Function) {
        List<Statement> statements = ir3Function.getStatements();

        Map<String, IR3Atom> constant = new HashMap<>();
        Map<String, Integer> depCounter = new HashMap<>();
        Map<String, Set<String>> invertedDepGraph = new HashMap<>();
        Map<String, Set<String>> depGraph = new HashMap<>();
        Map<String, List<Assignment>> assignStatementMap = new HashMap<>();

        Queue<String> q = new LinkedList<>();

        ir3Function.getCompleteDeclarations()
                .stream()
                .filter(d -> !(d.getType() instanceof ClassType))
                .forEach(d -> {
                    depCounter.put(d.getName(), 0);
                    invertedDepGraph.put(d.getName(), new HashSet<>());
                    depGraph.put(d.getName(), new HashSet<>());
                    assignStatementMap.put(d.getName(), new ArrayList<>());
                });

        statements.stream()
                .filter(s -> s instanceof Assignment)
                .map(s -> (Assignment) s)
                .filter(s -> !(s.getTarget() instanceof IR3Dot))
                .forEach(s -> {
                    s.getWrittenVariable().forEach(target -> {
                        s.getUsedVariable().forEach(dep -> {
                            invertedDepGraph.getOrDefault(dep, new HashSet<>()).add(target);
                            depGraph.getOrDefault(target, new HashSet<>()).add(dep);
                        });
                        assignStatementMap.getOrDefault(target, new ArrayList<>()).add(s);
                    });
                });

        depCounter.keySet()
                .forEach(k -> depCounter.put(k, depGraph.get(k).size()));

        depCounter.keySet().stream()
                .filter(k -> depCounter.get(k) == 0)
                .forEach(q::add);

        while(!q.isEmpty()) {
            String currentVar = q.poll();

            List<IR3Atom> results = assignStatementMap.get(currentVar).stream()
                    .map(Assignment::getRight)
                    .map(a -> a.eval(constant))
                    .distinct()
                    .collect(Collectors.toList());

            if (results.size() == 1) {
                constant.put(currentVar, results.get(0));
                invertedDepGraph.get(currentVar)
                        .forEach(nxt -> {
                            if(depCounter.containsKey(nxt)) {
                                depCounter.put(
                                        nxt,
                                        depCounter.get(nxt) - 1
                                );
                                if(depCounter.get(nxt) == 0) {
                                    q.add(nxt);
                                }
                            }
                        });
            }
        }

        constant.keySet()
                .forEach(k -> {
                    assignStatementMap.get(k).forEach(a -> a.setRight(constant.get(k)));
                });
    }
}
