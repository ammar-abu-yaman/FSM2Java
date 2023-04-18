package com.graduation.parse;

import java.io.File;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.graduation.fsm.Fsm;

public class FsmJParser implements FsmParser {

    @Override
    public Fsm parse(File file) throws Exception {
        var lexer = new compilerLexer(CharStreams.fromFileName(file.toPath().toString()));
        var tokens = new CommonTokenStream(lexer);
        var parser = new compilerParser(tokens);
        var tree = parser.program();

        var visitor = new StandaloneCompilerVisitor();
        Fsm fsm = (Fsm) visitor.visit(tree);
        return fsm;
    }

}
