package com.graduation.generate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

import com.graduation.fsm.Fsm;
import com.graduation.fsm.Item.*;

public class JavaGenerator extends Generator {

    private String packageName;
    private String ownerClass;
    private String contextClass;
    private String stateClass;
    private String actionsInterface;
    private String initialStateClass;
    private List<Trigger> triggers;
    private List<String> actions;

    public JavaGenerator(Fsm fsm, Path outputPath) {
        super(fsm, outputPath);
        init();
    }

    private void init() {
        addDefaultOptions();
        ownerClass = fsm.getOptionValue("class");
        stateClass = ownerClass + "State";
        contextClass = ownerClass + "Context";
        actionsInterface = ownerClass + "Actions";
        initialStateClass = fsm.getOptionValue("initial-state");
        packageName = fsm.getOptionValue("package");
        actions = fsm.getOptionValues("actions");
        triggers = aggregateTriggers();
    }

    @Override
    public void generate() throws IOException {
        outputPath.toFile().mkdirs();
        generateActionsInterface();
        generateContextClass();
        generateParentStateClass();
        generateBaseStateClass();
        generateStateClasses();
        generateOwnerClass();
    }

    private void generateOwnerClass() {
        Path filePath = outputPath.resolve(ownerClass + ".java");
        try (JavaWriter writer = new JavaWriter(filePath)) {
            generateOwnerClass(writer);
        } catch (Exception e) {
        }
    }

    private void generateOwnerClass(JavaWriter writer) {
        generateOwnerClassHeader(writer);
        generateOwnerClassActionMethods(writer);
        closeDefinition(writer);
    }

    private void generateOwnerClassHeader(JavaWriter writer) {
        writer.writePackage(packageName);
        writer.writeEmptyLine();

        writer.writeClassHeader(ownerClass, List.of("public"), Optional.empty(), List.of(actionsInterface));
        writer.writeEmptyLine();

        writer.writeCode("private " + contextClass + " context;", 1);
        writer.writeEmptyLine();

        writer.writeCode(format("""
                public %s() {
                    this.context = new %s(this);
                }
                """, ownerClass, contextClass),1);
        writer.writeEmptyLine();
    }

    private void generateOwnerClassActionMethods(JavaWriter writer) {
        for(String action: actions) {
            writer.writeMethodHeader(action, "void", List.of("public"), List.of());
            writer.writeCode(format("throw new UnsupportedOperationException(\"Action %s is not implemented\");", action), 2);
            writer.writeClosingBracket(1);
        }
    }

    private void generateContextClass() {
        Path filepPath = outputPath.resolve(contextClass + ".java");
        try (JavaWriter writer = new JavaWriter(filepPath)) {
            generateContextClass(writer);
        } catch (Exception e) {

        }
    }

    private void generateContextClass(JavaWriter writer) {
        generateContextClassHeader(writer);
        generateContextClassTriggers(writer);
        generateContextClassOwnerGetter(writer);
        closeDefinition(writer);
    }

    private void generateContextClassOwnerGetter(JavaWriter writer) {
        writer.writeEmptyLine();
        writer.writeCode(format("""
                public %s getOwner() {
                    return this.owner;
                }
                """, ownerClass),1);
    }

    private void generateContextClassTriggers(JavaWriter writer) {
        for (Trigger trigger : triggers) {
            writeContextClassTriggerBody(writer, trigger);
        }
    }

    private void writeContextClassTriggerBody(JavaWriter writer, Trigger trigger) {
        writer.writeMethodHeader(trigger.name(), "void", List.of("public"), trigger.params());
        writer.writeCode(format("""
                getState().%s(%s);
                """, trigger.name(),
                paramsToArguments(trigger.params())), 2);
        writer.writeCode("}", 1);
    }

    private void generateContextClassHeader(JavaWriter writer) {
        writer.writePackage(packageName);
        writer.writeEmptyLine();
        writer.writeImport("com.fsm4j.AbstractFsmContext");
        writer.writeEmptyLine();
        writer.writeClassHeader(contextClass, List.of("public"), Optional.of("AbstractFsmContext"));

        writer.writeCode("protected " + ownerClass + " owner;", 1);
        writer.writeEmptyLine();

        writer.writeCode(format("""
                public %s(%s owner) {
                    this.owner = owner;
                    setState(new %s(this));
                }
                """, contextClass, ownerClass, initialStateClass), 1);
    }

    private void generateBaseStateClass() {
        State defaultState = getStateByName("Base").orElseGet(() -> new State("Base", List.of()));
        generateState(defaultState, 0);
    }

    private void generateStateClasses() {
        int stateId = 1;
        for (State state : fsm.getStates()) {
            if (state.name().equals("Base"))
                continue;
            generateState(state, stateId++);
        }
    }

    private void generateState(State state, int stateId) {
        Path filePath = outputPath.resolve(state.name() + ".java");
        try (JavaWriter writer = new JavaWriter(filePath)) {
            generateState(state, stateId, writer);
        } catch (Exception e) {
        }
    }

    private void generateState(State state, int stateId, JavaWriter writer) {
        generateStateHeader(writer, state, stateId);
        generateStateEnterMethod(writer, state);
        generateStateExitMethod(writer, state);
        generateStateTriggers(writer, state);
        closeDefinition(writer);
    }

    private void generateStateExitMethod(JavaWriter writer, State state) {
        writer.writeMethodHeader("__enter__", "void", List.of("protected"), List.of());
        writer.writeCode(state.enterCode(), 2);
        writer.writeCode("}", 1);
    }

    private void generateStateEnterMethod(JavaWriter writer, State state) {
        writer.writeMethodHeader("__exit__", "void", List.of("protected"), List.of());
        writer.writeCode(state.exitCode(), 2);
        writer.writeCode("}", 1);
    }

    private void generateStateHeader(JavaWriter writer, State state, int stateId) {
        String className = state.name();
        String baseClassName = className.equals("Base") ? stateClass : "Base";
        writer.writePackage(packageName);
        writer.writeEmptyLine();
        writer.writeClassHeader(className, List.of("public"), Optional.of(baseClassName));
        writer.writeEmptyLine();

        writer.writeCode(format("""
                public %s(%s context) {
                    super(context, "%d", "%s");
                }
                """, className, contextClass, stateId, className), 1);
        writer.writeEmptyLine();
    }

    private void generateStateTriggers(JavaWriter writer, State state) {
        for (Trigger trigger : triggers) {
            generateTriggerMethod(writer, state, trigger);
        }
    }

    private void generateTriggerMethod(JavaWriter writer, State state, Trigger trigger) {
        writer.writeMethodHeader(trigger.name(), "void", List.of("public"), trigger.params());
        List<Transition> transitions = getMatchingTransitions(state, trigger);

        writer.writeCode(ownerClass + " ctx = context.getOwner();", 2);

        if (transitions.isEmpty()) {
            generateSuperDelegatedTransition(writer, trigger);
        } else {
            for (int i = 0; i < transitions.size(); i++) {
                generateTransitionBody(writer, transitions.get(i), state.name(), i == 0);
            }

            if (hasNoFailSafeTransition(transitions)) {
                generateSuperDelegatedTransition(writer, trigger);
            }
        }
        writer.writeCode("}", 1);
    }

    private List<Transition> getMatchingTransitions(State defaultState, Trigger trigger) {
        return defaultState.transitions()
                .stream()
                .filter(transition -> transition.trigger().equals(trigger))
                .sorted(JavaGenerator::compareTransitionsByGuard)
                .collect(Collectors.toList());
    }

    private void generateTransitionBody(JavaWriter writer, Transition transition, String stateName,
            boolean isFirstTransition) {
        writer.writeCode(format("%s%s{",
                isFirstTransition ? "" : "else ",
                transition.guard().isPresent() ? "if(" + transition.guard().get() + ") " : ""), 2);

        if (isValidNextState(transition))
            writer.writeCode("context.getState().__exit__();", 3);
        writer.writeCode("try {", 3);
        writer.writeCode(transition.code(), 4);
        writer.writeCode(format("""
                } finally {
                    context.setState(new %s(this));
                    %s
                }
                """, isValidNextState(transition) ? transition.nextState() : stateName,
                isValidNextState(transition) ? "context.getState().__enter__();" : ""), 3);
        writer.writeCode("return;", 3);
        writer.writeCode("}", 2);
    }

    private boolean isValidNextState(Transition transition) {
        return !transition.nextState().equals("null");
    }

    private void generateSuperDelegatedTransition(JavaWriter writer, Trigger trigger) {
        writer.writeCode(format("super.%s(%s);", trigger.name(), paramsToArguments(trigger.params())), 2);
    }

    private String paramsToArguments(List<Param> params) {
        return params
                .stream()
                .map(param -> param.name())
                .collect(Collectors.joining(", "));
    }

    boolean hasNoFailSafeTransition(List<Transition> transitions) {
        return transitions
                .stream()
                .allMatch(transition -> transition.guard().isPresent());
    }

    private Optional<State> getStateByName(String name) {
        return fsm.getStates()
                .stream()
                .filter(state -> state.name().equals(name))
                .findFirst();
    }

    private static int compareTransitionsByGuard(Transition a, Transition b) {
        if (a.guard().isPresent() == b.guard().isPresent())
            return 0;
        if (a.guard().isEmpty()) {
            return 1;
        }
        return -1;
    }

    private void generateParentStateClass() {
        Path filePath = outputPath.resolve(stateClass + ".java");
        try (JavaWriter writer = new JavaWriter(filePath)) {
            generateParentStateClass(writer);
        } catch (Exception e) {

        }
    }

    private void generateParentStateClass(JavaWriter writer) {
        generateParentStateHeader(writer);
        generateParentStateConstructor(writer);
        generateBaseTriggers(writer);
        closeDefinition(writer);
    }

    private void generateParentStateHeader(JavaWriter writer) {
        writer.writePackage(packageName);
        writer.writeEmptyLine();
        writer.writeImport("com.fsm4j.AbstractState");
        writer.writeEmptyLine();
        writer.writeClassHeader(stateClass, List.of("public", "abstract"), Optional.of("AbstractState"));
        writer.writeEmptyLine();

        writer.writeProperty("context", contextClass, "protected");

        writer.writeEmptyLine();
    }

    private void generateParentStateConstructor(JavaWriter writer) {
        writer.writeCode(format("""
                protected %s(%s context, String _id, String _name) {
                    super(_id, _name);
                    this.context = context;
                }
                """, stateClass, contextClass), 1);
        writer.writeEmptyLine();
    }

    private void generateBaseTriggers(JavaWriter writer) {
        for (Trigger trigger : triggers) {
            writer.writeMethodHeader(trigger.name(), "void", List.of("protected"),
                    trigger.params());
            writer.writeCode("Base();", 2);
            writer.writeClosingBracket(1);
        }

        writer.writeCode("""
                protected void Base() {
                    throw new RuntimeException("Undefined transtion");
                }
                """, 1);

    }

    private void generateActionsInterface() {
        Path filePath = outputPath.resolve(actionsInterface + ".java");
        try (JavaWriter writer = new JavaWriter(filePath)) {
            generateActionsInterface(writer);
        } catch (Exception e) {

        }
    }

    private void generateActionsInterface(JavaWriter writer) {
        generateActionsInterfaceHeader(writer);
        generateActionsInterfaceMethods(writer);
        closeDefinition(writer);
    }

    private void generateActionsInterfaceMethods(JavaWriter writer) {
        for (String action : actions) {
            writer.writeAbstractMethodHeader(action, "void", List.of(), List.of());
        }
    }

    private void generateActionsInterfaceHeader(JavaWriter writer) {
        writer.writePackage(packageName);
        writer.writeEmptyLine();
        writer.writeInterfaceHeader(actionsInterface, List.of("public"), List.of());
        writer.writeEmptyLine();
    }

    private void closeDefinition(JavaWriter writer) {
        writer.writeClosingBracket(0);
    }

}
