package com.graduation.fsm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fsm implements Item {
    private Map<String, Option> options = new HashMap<>();
    private List<State> states = new ArrayList<>();

    public Fsm(Map<String, Option> options, List<State> states) {
        this.options = options;
        this.states = states;
    }

    public String getOptionValue(String name) {
        return options.get(name).values().get(0);
    }

    public List<String> getOptionValues(String name) {
        return options.get(name).values();
    }

    public void addOption(Option option) {
        options.put(option.name(), option);
    }

    public boolean hasOption(String name) {
        return options.containsKey(name);
    }

    public Map<String, Option> getOptions() {
        return options;
    }

    public List<State> getStates() {
        return states;
    }

    public void addState(State state) {
        states.add(state);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((options == null) ? 0 : options.hashCode());
        result = prime * result + ((states == null) ? 0 : states.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Fsm other = (Fsm) obj;
        if (options == null) {
            if (other.options != null)
                return false;
        } else if (!options.equals(other.options))
            return false;
        if (states == null) {
            if (other.states != null)
                return false;
        } else if (!states.equals(other.states))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Fsm [properties=" + options + ", states=" + states + "]";
    }

}
