package com.graduation.generate;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.graduation.fsm.Fsm;
import com.graduation.fsm.Item.Option;
import com.graduation.fsm.Item.State;

public abstract class Generator {
    protected Fsm fsm;
    protected Path outputPath;

    public Generator(Fsm fsm, Path outputPath) {
        this.fsm = fsm;
        this.outputPath = outputPath;
    }

    public abstract void generate() throws Exception;

    protected List<Fsm.Trigger> aggregateTriggers() {
        Set<Fsm.Trigger> triggers = new HashSet<>();
        for (var state : fsm.getStates())
            triggers.addAll(getStateTriggers(state));
        return triggers.stream().collect(Collectors.toList());
    }

    private List<Fsm.Trigger> getStateTriggers(State state) {
        return state.transitions()
                .stream()
                .map(transition -> transition.trigger())
                .collect(Collectors.toList());
    }

    protected void addDefaultOptions() {
        if (!fsm.hasOption("actions"))
            fsm.addOption(new Option("actions", new ArrayList<>()));
    }
}
