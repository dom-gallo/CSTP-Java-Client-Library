package com.domgallo;

import java.io.Serializable;

public class TestObject implements Serializable {
    public String message;

    public TestObject(String message){
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
