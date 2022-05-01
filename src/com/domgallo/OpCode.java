package com.domgallo;

public enum OpCode {
    OP_GET('@'),
    OP_INSERT('I'),
    OP_DELETE('!'),
    OP_UPDATE('%');

    public final char code;

    OpCode(char code) {
        this.code = code;
    }
}
