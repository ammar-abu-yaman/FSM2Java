package com.graduation.parse;

import static org.junit.Assert.assertEquals;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Test;

import com.graduation.fsm.Fsm;
import com.graduation.fsm.Item.*;

import static com.graduation.TestUtils.*;

public class JsonParserTest {

    @Test
    public void testParse() throws Exception {
        String json = Files.readString(Paths.get("samples", "fsm.json"));
        JsonParser parser = new JsonParser(json);
        Fsm actual = parser.parse();
        Fsm expected = new Fsm(options(
                new Option("package", list("com.sample.one23")),
                new Option("class", list("Sample")),
                new Option("initial-state", list("AnyState")),
                new Option("actions", list("action1", "action2", "action3"))),
                list(
                        new State("State1", list(
                                new Transition(
                                        new Trigger("open", list(new Param("param", "String"))),
                                        Optional.of("param.isEmpty()"),
                                        "State2",
                                        "System.out.println(param.trim());\n")))));

        assertEquals(expected.getStates().get(0).transitions().get(0).getClass().getName(),
                actual.getStates().get(0).transitions().get(0).getClass().getName());

    }
}
