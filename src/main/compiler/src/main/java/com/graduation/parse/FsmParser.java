package com.graduation.parse;

import java.io.File;

import com.graduation.fsm.Fsm;

//Strategy pattern
//JsonParser
//FsmJParser
//XmlParser
//allows to extend the parsing logic
public interface FsmParser {
    Fsm parse(File file) throws Exception;
}
