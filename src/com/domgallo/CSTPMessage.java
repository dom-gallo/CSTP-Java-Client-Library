package com.domgallo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/*
*   TODO:
*    1. Add Object<Class> getObjectWithKey(String key, Class clazz)
*    2. add int updateObjectWithKey(String key, Object:Serializable o)
*    3. add int insertObjectWithKey(String key, Object:Serializable o)
*    4. add int deleteForKey(String key)
*
* */
public class CSTPMessage {
    private OpCode opcode;
    private Serializable payload;
    private String key;
    private byte[] payloadBytes;
    private byte[] payloadLengthInBytes;
    ByteArrayOutputStream outputStream;

    public CSTPMessage(OpCode opcode, Serializable payload, String key) {
        this.opcode = opcode;
        this.payload = payload;
        this.key = key;
        this.payloadBytes = null;
        this.payloadLengthInBytes = null;
        this.outputStream = new ByteArrayOutputStream();

    }
    byte[] generateSimpleRequest(String message){
        byte[] data = null;
        byte[] messageBytes = message.getBytes();
        OpCode opCode = OpCode.OP_GET;
//        System.out.println(String.format("mesage = %s, size in bytes = ", message, ));
        byte[] dataLength = ByteBuffer.allocate(4).putInt(messageBytes.length).order(ByteOrder.BIG_ENDIAN).array();
        try {
            outputStream.flush();
            outputStream.write((byte) opCode.code);
            outputStream.write(dataLength);
            outputStream.write(messageBytes);
            data = outputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    byte[] getRequestBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        // this feels really hacky
        ByteArrayOutputStream objectBoas = new ByteArrayOutputStream();
        // create object output stream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(objectBoas);
        // write object to stream
        objectOutputStream.writeObject(payload);
        // convert object byte stream into byte array
        byte[] objectBytes = objectBoas.toByteArray();
        setPayloadBytes(objectBytes);
        //write length of object byte array
        byte[] payloadByteLength = ByteBuffer.allocate(4).putInt(objectBytes.length).order(ByteOrder.BIG_ENDIAN).array();
        setPayloadLengthInBytes(payloadByteLength);
        output.write(opcode.code);
        // Write the length of payload data
        output.write(payloadByteLength);
        byte[] hashBytes = getHashBytes(key);
        // Write the hash key
        output.write(hashBytes);
        // write object bytes
        output.write(objectBytes);
        byte[] finalOut = output.toByteArray();
        setPayloadBytes(finalOut);
        return finalOut;
    }
    // adapted from String.hashCode()
    private long hash(String key) {
        long h = 1125899906842597L; // prime
        int len = key.length();

        for (int i = 0; i < len; i++) {
            h = 31*h + key.charAt(i);
        }
        return h;
    }
    public byte[] getHashBytes(String key){
        long hash = hash(key);
        byte[] hashBytes = ByteBuffer.allocate(Long.BYTES).putLong(hash).array();
        System.out.print("Key \\\\ ");
        for(int i = 0; i < hashBytes.length; i++){
            System.out.print(String.format("%02x ", hashBytes[i]));
        }
        System.out.println(" // End of Key");
        return hashBytes;
    }
    public int getPayLoadByteLength(){
        return payloadBytes.length;
    }
    public int getPayloadByteArrayLength(){
        return payloadLengthInBytes.length;
    }
    public void printDataBytes(){
        for(int i = 0; i < payloadBytes.length; i++){
            System.out.print(String.format("%02x ", payloadBytes[i]));
        }
    }
    public OpCode getOpcode() {
        return opcode;
    }
    private void setPayloadLengthInBytes(byte[] lengthInBytes){
        this.payloadLengthInBytes = lengthInBytes;
    }
    private void setPayloadBytes(byte[] payloadBytes){
        this.payloadBytes = payloadBytes;
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
