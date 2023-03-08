package com.graduation;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.graduation.fsm.Fsm;
import com.graduation.fsm.Item.Option;
import com.graduation.parse.StandaloneCompilerVisitor;
import com.graduation.parse.compilerLexer;
import com.graduation.parse.compilerParser;

public class TestUtils {

    public static Fsm generateFsmFromFile(String filename) throws IOException {
        var lexer = new compilerLexer(CharStreams.fromFileName(filename));
        var tokens = new CommonTokenStream(lexer);
        var parser = new compilerParser(tokens);
        var tree = parser.program();

        var visitor = new StandaloneCompilerVisitor();
        Fsm fsm = (Fsm) visitor.visit(tree);
        return fsm;
    }

    public static <T> List<T> list(T... items) {
        return Arrays.asList(items);
    }

    public static Map<String, Option> options(Option... ops) {
        Map<String, Option> ret = new HashMap<>();
        for (Option op : ops)
            ret.put(op.name(), op);
        return ret;
    }

}
