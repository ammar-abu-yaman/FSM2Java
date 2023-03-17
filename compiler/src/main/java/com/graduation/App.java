package com.graduation;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.graduation.fsm.Fsm;
import com.graduation.fsm.Item;
import com.graduation.generate.JavaGenerator;
import com.graduation.parse.StandaloneCompilerVisitor;
import com.graduation.parse.compilerLexer;
import com.graduation.parse.compilerParser;

public class App {
  public static void main(String[] args) throws Exception {
    var filename = "enter-exit";
    var lexer = new compilerLexer(CharStreams.fromFileName(String.format("samples/%s.txt",
        filename)));
    var tokens = new CommonTokenStream(lexer);
    var parser = new compilerParser(tokens);
    var tree = parser.program();

    var visitor = new StandaloneCompilerVisitor();
    Fsm fsm = (Fsm) visitor.visit(tree);
    Path path = Paths.get("outputs", filename);
    JavaGenerator generator = new JavaGenerator(fsm, path);
    generator.generate();

    // ObjectMapper mapper = new ObjectMapper();
    // mapper.registerModule(new Jdk8Module());
    // String json = """
    // {
    // "name": "State1",
    // "transitions": [
    // {
    // "trigger": {
    // "name": "open",
    // "params": [
    // {
    // "name": "param",
    // "type": "String"
    // }
    // ]
    // },
    // "guard": "param.isEmpty()",
    // "nextState": "State2",
    // "code": "System.out.println(param.trim());"
    // }
    // ],
    // "enterCode": "",
    // "exitCode": ""
    // }
    // """;
    // Item.State state = mapper.readValue(json, new TypeReference<Item.State>() {

    // });
    // System.out.println(state.transitions().get(0));
  }
}
