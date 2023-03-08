package com.graduation.parse;

import java.nio.file.Path;
import java.util.Optional;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.graduation.fsm.Fsm;

public class FsmJParser implements FsmParser {

    Path filePath;

    @Override
    public Fsm parse() throws Exception {

        var lexer = new compilerLexer(CharStreams.fromFileName(filePath.toString()));
        var tokens = new CommonTokenStream(lexer);
        var parser = new compilerParser(tokens);
        var tree = parser.program();

        var visitor = new StandaloneCompilerVisitor();
        Fsm fsm = (Fsm) visitor.visit(tree);
        return fsm;
    }

}
