package dev.aguss787.cs4212.assignment3.asm;

import dev.aguss787.cs4212.assignment2.IR3.IR3Dot;
import dev.aguss787.cs4212.assignment3.asm.Atom.*;
import dev.aguss787.cs4212.assignment1.ast.Type;
import dev.aguss787.cs4212.assignment2.IR3.IR3Class;
import dev.aguss787.cs4212.assignment2.IR3.IdentifierHelper;
import dev.aguss787.cs4212.assignment3.asm.Constant.Constant;
import dev.aguss787.cs4212.assignment3.asm.Constant.StringConstant;
import dev.aguss787.cs4212.assignment3.asm.statement.*;
import dev.aguss787.cs4212.assignment3.register.ClassManager;
import dev.aguss787.cs4212.assignment3.register.IR3AddOns.ReadlnHeader;
import dev.aguss787.cs4212.assignment3.register.RegisterManager;
import dev.aguss787.cs4212.assignment3.register.StackManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssemblyBuilder {
    private final List<Constant> constants = new ArrayList<>();
    private final List<Statement> statements = new ArrayList<>();
    private final List<Statement> header = new ArrayList<>();

    private final IdentifierHelper<String> stringLabel = new IdentifierHelper<>("str_");
    private final ClassManager classManager = new ClassManager();

    private StackManager stackManager;
    private final RegisterManager registerManager;

    private Label returnLabel;

    private boolean writelnFlag = false;

    public AssemblyBuilder() {
        header.add(new Dot("arch", "armv7-a"));
        registerManager = new RegisterManager(this);
    }

    public Label getReturnLabel() {
        return returnLabel;
    }

    public void setWritelnFlag() {
        writelnFlag = true;
    }

    public ClassManager getClassManager() {
        return classManager;
    }

    public StackManager getStackManager() {
        return stackManager;
    }

    public RegisterManager getRegisterManager() {
        return registerManager;
    }

    public void setStackManager(StackManager stackManager) {
        this.stackManager = stackManager;
    }

    public void addMain(IR3Class ir3Class) {
        this.addFunctionHeader("main", 0);
        this.addCallWithLink(
                ir3Class.getClassFunction(
                        ir3Class.getName(),
                        ir3Class.getFunctions().get(0).getName()
                )
        );
        statements.add(new Mov(new Register("a1"), new AsmInteger(0)));
        this.addFunctionFooter("main", 0);
    }

    public void setRegisterPos(String varName, Register register) {
        registerManager.setRegisterPos(varName, register);
    }

    public Register getRegisterPos(String varName) {
        return registerManager.getRegisterPos(varName);
    }


    public void registerClass(IR3Class ir3Class) {
        classManager.addClass(ir3Class);
    }

    public void add(Statement statement) {
        statements.add(statement);
    }

    public void addLabel(String label) {
        if (statements.size() > 0 && !(statements.get(statements.size() - 1) instanceof Jump)) {
            resetRegisterManager();
        }

        statements.add(new Label(label));
    }

    public void addFunctionHeader(String label, int frameSize) {
        returnLabel = new Label(label + "_func_ret");
        statements.add(new Dot("text"));
        statements.add(new Dot("global", label));
        statements.add(new Dot("type", label, "%function"));
        statements.add(new Label(label));
        statements.add(new Push("v1", "v2", "v3", "v4", "v5", "fp", "lr"));
        addMov(new Register("fp"), new Register("sp"));
        statements.add(new DataInstruction(
                "sub",
                new Register("sp"),
                new Register("sp"),
                new AsmInteger(frameSize)
        ));
    }

    public void addFunctionFooter(String label, int frameSize) {
        statements.add(returnLabel);
        statements.add(new DataInstruction(
                "add",
                new Register("sp"),
                new Register("sp"),
                new AsmInteger(frameSize)
        ));
        statements.add(new Pop("v1", "v2", "v3", "v4", "v5", "fp", "pc"));
    }

    public String addStringConstant(String str) {
        boolean isExist = stringLabel.hasIdentifier(str);
        String label = stringLabel.getIdentifier(str);
        if (!isExist) {
            constants.add(new StringConstant(label, str));
        }
        return label;
    }

    public void addMov(Atom target, Atom origin) {
        statements.add(new Mov(target, origin));
    }

    public void addLoadString(String register, String str) {
        String label = this.addStringConstant(str);
        statements.add(new Ldr(register, "=" + label));
    }

    public void addLoadMemory(Register register, Memory memory) {
        statements.add(new Ldr(register.toString(), memory.toString()));
    }

    public void addBEQ(Atom atom, Label label) {
        statements.add(new Cbnz(atom, label));
    }

    public void addJump(Label label) {
        resetRegisterManager();
        statements.add(new Jump(label));
    }

    public void addStoreMemory(Register register, Memory memory) {
        statements.add(new Str(register, memory));
    }

    public Register loadFromMemory(String name) {
        return this.registerManager.getRegisterPos(name);
    }

    public Register loadFromMemory2x(IR3Dot ref) {
        return this.registerManager.loadToRegister2x(ref);
    }

    public void storePtr(IR3Dot ref, Atom data) {
        this.registerManager.storePtr(ref, data);
    }

    public void pushArgs() {
        statements.add(new Push("a1", "a2", "a3", "a4"));
    }

    public void popArgs() {
        statements.add(new Pop("a1", "a2", "a3", "a4"));
    }

    public Integer getClassSize(String name) {
        return classManager.getSize(name);
    }

    public Integer getClassFieldOffset(String name, String field) {
        return classManager.getFieldOffset(name, field);
    }

    public void moveData(Atom target, Atom origin) {
        if (origin instanceof Register) {
            if (target instanceof Register) {
                addMov(target, origin);
            } else if (target instanceof Memory) {
                addStoreMemory((Register) origin, (Memory) target);
            } else {
                throw new Error(String.format("unsupported move (%s -> %s)", origin, target));
            }
        } else if (origin instanceof Memory) {
            if (target instanceof Register) {
                addLoadMemory((Register) target, (Memory) origin);
            } else {
                throw new Error(String.format("unsupported move (%s -> %s)", origin, target));
            }
        } else if (origin instanceof LabelAddress) {
            if (target instanceof Register) {
                statements.add(new Ldr(target.toString(), origin.toString()));
            } else {
                throw new Error(String.format("unsupported move (%s -> %s)", origin, target));
            }
        } else {
            if (target instanceof Register) {
                addMov((Register) target, origin);
            } else {
                throw new Error(String.format("unsupported move (%s -> %s)", origin, target));
            }
        }
    }

    public Atom getArgRegister(String name) {
        return stackManager.getArgRegister(name);
    }

    public Atom getMemory(String name) {
        return stackManager.getArgRegister(name);
    }

    public Type getType(String name) {
        return stackManager.getType(name);
    }

    public void addCallWithLink(String label) {
        statements.add(new Bl(label));
    }

    public void resetRegisterManager() {
        registerManager.reset();
    }

    public void addOr(Atom target, Atom left, Atom right) {
        statements.add(new Or(target, left, right));
    }

    public void addAnd(Atom target, Atom left, Atom right) {
        statements.add(new And(target, left, right));
    }

    public void addEq(Atom target, Atom left, Atom right) {
        statements.add(new Cmp(left, right));
        statements.add(new Mov(target, new AsmInteger(0)));
        statements.add(new Mov("eq", target, new AsmInteger(1)));
    }

    public void addNeq(Atom target, Atom left, Atom right) {
        statements.add(new Cmp(left, right));
        statements.add(new Mov(target, new AsmInteger(0)));
        statements.add(new Mov("ne", target, new AsmInteger(1)));
    }

    public void addCmp(String suffix, Atom target, Atom left, Atom right) {
        statements.add(new Cmp(left, right));
        statements.add(new Mov(target, new AsmInteger(0)));
        statements.add(new Mov(suffix, target, new AsmInteger(1)));
    }

    public void addAdd(Atom target, Atom left, Atom right) {
        statements.add(new DataInstruction("add", (Register) target, left, right));
    }

    public void addSub(Atom target, Atom left, Atom right) {
        statements.add(new DataInstruction("sub", (Register) target, left, right));
    }

    public void addMul(Atom target, Atom left, Atom right) {
        statements.add(new DataInstruction("mul", (Register) target, left, right));
    }

    public void addDiv(Atom target, Atom left, Atom right) {
        statements.add(new DataInstruction("sdiv", (Register) target, left, right));
    }

    public void addNeg(Atom target, Atom left) {
        statements.add(new SingleDataInstruction("neg", (Register) target, left));
    }

    @Override
    public String toString() {
        String prefix = "";
        if (writelnFlag) {
            prefix = new ReadlnHeader().toString();
        }
        return prefix + "\n"
                + header.stream().map(Object::toString).collect(Collectors.joining("\n"))
                + "\n"
                + constants.stream().map(Object::toString).collect(Collectors.joining("\n"))
                + "\n"
                + statements.stream().map(Object::toString).collect(Collectors.joining("\n"))
                + "\n";
    }
}
