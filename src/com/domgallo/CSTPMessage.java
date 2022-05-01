package com.domgallo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class CSTPMessage {
    private OpCode opcode;
    private Serializable payload;
    private String key;


    public CSTPMessage(OpCode opcode, Serializable payload, String key) {
        this.opcode = opcode;
        this.payload = payload;
        this.key = key;
    }

    byte[] generateRequest() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        // this feels really hacky
        ByteArrayOutputStream objectBoas = new ByteArrayOutputStream();
        // create object output stream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(objectBoas);
        // write object to stream
        objectOutputStream.writeObject(payload);
        // convert object byte stream into byte array
        byte[] objectBytes = objectBoas.toByteArray();
        //write length of object byte array
        byte[] payloadByteLength = ByteBuffer.allocate(4).putInt(objectBytes.length).order(ByteOrder.BIG_ENDIAN).array();
        output.write(opcode.code);
        output.write(payloadByteLength);
        // write object bytes
        output.write(objectBytes);
        byte[] finalOut = output.toByteArray();
        for(int i = 0; i < finalOut.length; i++){
            System.out.print(String.format("%02x ", finalOut[i]));
        }
        System.out.println("\n");
        return finalOut;
    }
    public OpCode getOpcode() {
        return opcode;
    }

    public void setOpcode(OpCode opcode) {
        this.opcode = opcode;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Serializable payload) {
        this.payload = payload;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
