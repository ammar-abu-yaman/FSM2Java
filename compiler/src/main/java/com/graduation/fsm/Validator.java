package com.graduation.fsm;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class Validator {
    private Fsm fsm;
    private List<String> errors;

    private final static List<String> REQUIRED_OPTIONS = List.of(
            "package",
            "initial-state",
            "class");

    private final static List<String> POSSIBLE_OPTIONS = List.of(
            "package",
            "initial-state",
            "class",
            "actions");

    public Validator(Fsm fsm) {
        this.fsm = fsm;
        this.errors = new ArrayList<>();
    }

    public List<String> validate() {
        generateMissingOptionsErrors();
        generateUnkownOptionsErrors();
        return errors;
    }

    private void generateMissingOptionsErrors() {
        for (String option : REQUIRED_OPTIONS) {
            if (!fsm.hasOption(option)) {
                errors.add(format("Required option '%s' not found", option));
            }
        }
    }

    private void generateUnkownOptionsErrors() {
        for (String option : fsm.getOptions().keySet()) {
            if (!POSSIBLE_OPTIONS.contains(option)) {
                errors.add(format("Unkown option 's' found", option));
            }
        }
    }

}
