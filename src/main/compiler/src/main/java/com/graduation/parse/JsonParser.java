package com.graduation.parse;

import com.graduation.fsm.Fsm;
import com.graduation.fsm.Item.*;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public class JsonParser implements FsmParser {
    private String json;
    private ObjectMapper mapper;

    public JsonParser() {
        mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
    }

    @Override
    public Fsm parse(File file) throws Exception {
        json = Files.readString(file.toPath());
        var root = mapper.readTree(json);
        var optionsNode = root.get("options");
        var statesNode = root.get("states");
        List<State> states = getStates(statesNode);

        Map<String, Option> options = getOptions(optionsNode);

        return new Fsm(options, states);
    }

    private Map<String, Option> getOptions(JsonNode optionsNode) {
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(optionsNode.fields(), Spliterator.ORDERED), false)
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> new Option(entry.getKey(),
                        mapper.convertValue(entry.getValue(), new TypeReference<List<String>>() {
                        }))));
    }

    private List<State> getStates(JsonNode statesNode) throws JsonMappingException, JsonProcessingException {
        List<State> states = mapper.readValue(statesNode.toString(), new TypeReference<List<State>>() {
        });
        for (State state : states) {
            for (int i = 0; i < state.transitions().size(); i++)
                state.transitions().set(i, mapper.convertValue(state.transitions().get(i), Transition.class));
        }
        return states;
    }

}
