package com.domgallo;

import java.io.Serializable;

public class TestMessage implements Serializable {
    public String message;

    public TestMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "testMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
