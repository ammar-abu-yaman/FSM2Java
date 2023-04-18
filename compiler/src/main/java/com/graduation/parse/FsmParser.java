package com.graduation.parse;

import java.io.File;

import com.graduation.fsm.Fsm;

public interface FsmParser {
    Fsm parse(File file) throws Exception;
}
