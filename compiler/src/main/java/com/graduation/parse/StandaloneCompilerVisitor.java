package com.graduation.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.Token;

import com.graduation.fsm.Fsm;
import com.graduation.fsm.Item;
import com.graduation.fsm.Item.*;
import com.graduation.parse.compilerParser.OptionContext;
import com.graduation.parse.compilerParser.ParamContext;
import com.graduation.parse.compilerParser.Param_listContext;
import com.graduation.parse.compilerParser.ProgramContext;
import com.graduation.parse.compilerParser.StateContext;
import com.graduation.parse.compilerParser.State_blockContext;
import com.graduation.parse.compilerParser.TransitionContext;
import com.graduation.parse.compilerParser.TriggerContext;

public class StandaloneCompilerVisitor extends compilerBaseVisitor<Item> {

    List<Param> params = new ArrayList<>();
    List<State> states = new ArrayList<>();

    @Override
    public Item visitProgram(ProgramContext ctx) {
        Map<String, Option> options = getOptions(ctx);
        visit(ctx.state_block());
        return new Fsm(options, states);
    }

    private Map<String, Option> getOptions(ProgramContext ctx) {
        Map<String, Option> options = ctx.options
                .stream()
                .map(optionCtx -> (Option) visit(optionCtx))
                .collect(Collectors.toMap(option -> option.name(), option -> option));
        return options;
    }

    @Override
    public Item visitOption(final OptionContext ctx) {
        return new Option(ctx.name.getText().substring(1), parseOptionValues(ctx));
    }

    private List<String> parseOptionValues(final OptionContext ctx) {
        return ctx.values
                .stream()
                .map(value -> value.getText().trim())
                .collect(Collectors.toList());
    }

    @Override
    public Item visitState_block(State_blockContext ctx) {
        states = ctx.states
                .stream()
                .map(stateCtx -> (State) visit(stateCtx))
                .collect(Collectors.toList());

        return super.visitState_block(ctx);
    }

    @Override
    public Item visitState(StateContext ctx) {
        String name = ctx.name.getText();
        String enterCode = ctx.enter != null ? removeBrackets(ctx.enter.code.getText()).trim() : "";
        String exitCode = ctx.exit != null ? removeBrackets(ctx.exit.code.getText()).trim() : "";
        List<Transition> transitions = getTransitions(ctx);
        return new State(name, transitions, enterCode, exitCode);
    }

    private List<Transition> getTransitions(StateContext ctx) {
        List<Transition> transitions = ctx.transitions
                .stream()
                .map(transitionCtx -> (Transition) visit(transitionCtx))
                .collect(Collectors.toList());
        return transitions;
    }

    @Override
    public Item visitTransition(TransitionContext ctx) {
        Trigger trigger = (Trigger) visit(ctx.trig);
        var guardToken = ctx.guard;
        Optional<String> guard = getGuard(guardToken);
        String nextState = ctx.nextState.getText();
        String code = removeBrackets(ctx.code.getText()).trim();
        return new Transition(trigger, guard, nextState, code);
    }

    private Optional<String> getGuard(Token guardToken) {
        Optional<String> guard = guardToken != null
                ? Optional.of(removeBrackets(guardToken.getText()).trim())
                : Optional.empty();
        return guard;
    }

    @Override
    public Item visitTrigger(TriggerContext ctx) {
        super.visitTrigger(ctx);
        String name = ctx.name.getText();
        return new Trigger(name, params != null ? params : List.of());
    }

    @Override
    public Item visitParam_list(Param_listContext ctx) {
        params = ctx.params
                .stream()
                .map(paramCtx -> (Param) visit(paramCtx))
                .collect(Collectors.toList());
        return super.visitParam_list(ctx);
    }

    @Override
    public Item visitParam(ParamContext ctx) {
        return new Param(ctx.name.getText(), ctx.type.getText());
    }

    private String removeBrackets(String text) {
        return text.substring(2, text.length() - 2);
    }

}
