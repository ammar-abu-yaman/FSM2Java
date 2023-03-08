package com.graduation.fsm;

import java.util.List;
import java.util.Optional;

public interface Item {
    public record Trigger(String name, List<Param> params) implements Item {
    }

    public record Param(String name, String type) implements Item {
    }

    public record State(String name, List<Transition> transitions, String enterCode, String exitCode) implements Item {
        public State(String name, List<Transition> transitions) {
            this(name, transitions, "", "");
        }
    }

    public record Transition(Trigger trigger, Optional<String> guard, String nextState, String code) implements Item {
    }

    public record Option(String name, List<String> values) implements Item {
    }
}
