package dev.aguss787.cs4212.assignment3.register;

import dev.aguss787.cs4212.assignment2.IR3.IR3Builder;
import dev.aguss787.cs4212.assignment2.IR3.IR3Function;
import dev.aguss787.cs4212.assignment2.IR3.statement.*;
import dev.aguss787.cs4212.assignment3.register.IR3AddOns.Load;
import dev.aguss787.cs4212.assignment3.register.IR3AddOns.Store;
import dev.aguss787.cs4212.assignment3.register.utils.IR3Graph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Allocation {
    private final Map<Integer, Register> preReqRegisterMap;
    private final Map<Integer, Register> postRegisterMap;
    private final Map<Integer, List<Statement>> blockStatementMap;
    private final Set<Integer> visited;

    private Integer REGISTER_NUM = 5;

    private static Integer labelCounter = 0;
    private IR3Graph ir3Graph;

    private class Register implements Cloneable {
        private final List<String> registers;

        Register(int register_num) {
            registers = Arrays.asList(new String[5]);
        }

        void allocate(int idx, String var) {
            int biggest = Integer.MIN_VALUE;
            int res = 0;

            for (int i = 0; i < registers.size(); i++) {
                String cur = registers.get(i);
                if (var.equals(cur)) {
                    biggest = Integer.MIN_VALUE;
                    break;
                }

                if (cur == null) {
                    biggest = Integer.MAX_VALUE;
                    res = i;
                } else {
                    int nxt = ir3Graph.getNextVariableUsage()
                            .getOrDefault(idx, new HashMap<>())
                            .getOrDefault(cur, Integer.MAX_VALUE);
                    if (nxt > biggest) {
                        biggest = nxt;
                        res = i;
                    }
                }
            }

            if (biggest != Integer.MIN_VALUE) {
                registers.set(res, var);
            }
        }

        List<Statement> transform(int idx, Register other) {
            List<Statement> res = new ArrayList<>();
            for (int i = 0; i < registers.size(); i++) {
                String left = registers.get(i);
                String right = other.registers.get(i);
                if (right != null && !right.equals(left)) {
                    if (left != null
                            && ir3Graph.getNextVariableUsage().getOrDefault(idx, new HashMap<>()).containsKey(left)
                    ) {
                        res.add(new Store(left, i));
                    }
                    res.add(new Load(right, i));
                }
            }

            return res;
        }

        @Override
        public Register clone() {
            Register res = new Register(registers.size());
            IntStream.range(0, registers.size()).forEach(i -> res.registers.set(i, registers.get(i)));
            return res;
        }
    }

    public Allocation() {
        preReqRegisterMap = new HashMap<>();
        postRegisterMap = new HashMap<>();
        blockStatementMap = new HashMap<>();
        visited = new HashSet<>();
    }

    public static void go(IR3Builder ir3Builder) {
        Allocation allocation = new Allocation();
        ir3Builder.getClasses().forEach(c -> {
            c.getFunctions().forEach(allocation::allocate);
        });
    }

    private void allocate(IR3Function ir3Function) {
        ir3Graph = new IR3Graph(ir3Function);
        List<Statement> statements = ir3Function.getStatements();

        actuallyAllocate(statements);
        ir3Function.setStatements(transform(statements));
    }

    private List<Statement> transform(List<Statement> statements) {
        visited.clear();
        blockStatementMap.clear();
        transform(statements, 0);
        return blockStatementMap.keySet().stream()
                .map(blockStatementMap::get)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private Label getNewLabel() {
        return new Label("_opt_brch_" + (++Allocation.labelCounter).toString());
    }

    private void transform(List<Statement> statements, int idx) {
        visited.add(idx);
        Integer block = ir3Graph.getBlockMap().get(idx);
        List<Statement> res = blockStatementMap.getOrDefault(block, new ArrayList<>());

        Statement statement = statements.get(idx);

        Register prev = preReqRegisterMap.get(idx);
        Register post = postRegisterMap.get(idx);

        res.addAll(prev.transform(idx, post));

        if (statement instanceof If) {
            int jump = ir3Graph.getLabelIdx().get(((Jump) statement).getLabel());
            Register jumpRegister = preReqRegisterMap.get(jump);

            int cont = ir3Graph.getLabelIdx().get(((Jump) statement).getLabel());
            Register contRegister = preReqRegisterMap.get(cont);

            Label finishLabel = getNewLabel();
            Label jumpLabel = getNewLabel();
            Label actualLabel = ((If) statement).getLabel();

            res.add(new If(((If) statement).getCond(), jumpLabel.getValue()));

            res.addAll(post.transform(cont, contRegister));
            res.add(new Goto(finishLabel.getValue()));

            res.add(jumpLabel);
            res.addAll(post.transform(jump, jumpRegister));
            res.add(new Goto(actualLabel.getValue()));

            res.add(finishLabel);
        } else if (statement instanceof Goto) {
            int nxt = ir3Graph.getLabelIdx().get(((Jump) statement).getLabel());
            Register future = preReqRegisterMap.get(nxt);
            res.addAll(post.transform(nxt, future));
            res.add(statement);
        } else {
            if (idx + 1 < statements.size()) {
                int nxt = idx + 1;
                Register future = preReqRegisterMap.get(nxt);
                res.addAll(post.transform(nxt, future));
            }
            res.add(statement);
        }

        blockStatementMap.put(block, res);

        ir3Graph.getNext(idx)
                .stream()
                .filter(n -> !visited.contains(n))
                .forEach(n -> transform(statements, n));
    }

    private void actuallyAllocate(List<Statement> statements) {
        preReqRegisterMap.clear();
        postRegisterMap.clear();
        actuallyAllocate(statements, 0, new Register(REGISTER_NUM));
    }

    private void actuallyAllocate(List<Statement> statements, int idx, Register register) {
        Register currentRegister = register.clone();
        preReqRegisterMap.put(idx, currentRegister.clone());

        Statement statement = statements.get(idx);

        List<String> vars = statements.get(idx).getInvolvedVariable();
        vars.forEach(v -> currentRegister.allocate(idx, v));

        postRegisterMap.put(idx, currentRegister.clone());
        ir3Graph.getNext(idx)
                .stream()
                .filter(n -> !preReqRegisterMap.containsKey(n))
                .forEach(n -> actuallyAllocate(statements, n, currentRegister.clone()));
    }
}
