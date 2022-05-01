package com.domgallo;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Client {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private String ipAddress;
    private int port;

    public Client(String ipAddress, int port) throws IOException {
        this.socket = null;
        this.input = null;
        this.output = null;
        this.ipAddress = ipAddress;
        this.port = port;

        try{
            socket = new Socket(ipAddress, port);
            System.out.println("Connected");

            // takes input from terminal
            input  = new DataInputStream(System.in);

            // sends output to the socket
            output = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException u ){
            System.out.println(u);
        } catch (IOException i){
            System.out.println(i);
        }

//        char opCode = '@';
//        String text = "This was added by a ByteArrayOutputStream";
//        byte[] payload = text.getBytes();
//        for(int a = 0; a < payload.length; a++){
//            System.out.print(String.format("%02x ", payload[a]));
//        }
//        byte[] payloadByteLength = ByteBuffer.allocate(4).putInt(payload.length).order(ByteOrder.BIG_ENDIAN).array();
//        int payloadLength = payload.length;
//        int messageLength = (1 + payloadByteLength.length+ payload.length);
//        byte opcodeByte = (byte) opCode;
//        byte[] message = new byte[messageLength];
//        //op code
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        out.write(opCode);
//        out.write(payloadByteLength);
//        out.write(payload);

//        message[0] = opcodeByte;
//        // data size
//        for(int j = 1; j < 5; j++){
//            message[j] = payloadByteLength[j - 1];
//        }
//        //data
//        for(int j = 5; j < payloadLength+5; j++){
//            message[j] = payload[j - 5];
//        }
//        for(int k = 0; k < messageLength; k++){
//            System.out.print( String.format("%02x ", message[k]));
//        }
        TestMessage aTestMessage = new TestMessage("Hello From Object Serializer");
        CSTPMessage myMessage = new CSTPMessage(OpCode.OP_GET, aTestMessage, "123");
        byte[] data = myMessage.generateRequest();
//        output.write(out.toByteArray());
        output.write(data);
    }
}

