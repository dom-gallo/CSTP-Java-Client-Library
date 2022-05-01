package com.domgallo;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

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

            // takes input from socket
            input  = new DataInputStream(socket.getInputStream());

            // sends output to the socket
            output = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException u ){
            System.out.println(u);
        } catch (IOException i){
            System.out.println(i);
        }

        Serializable javaObjectPayload = new TestObject("Hello From Object Serializer");

        // Class returns an object encapsulating all data
        CSTPMessage myMessage = new CSTPMessage(OpCode.OP_GET, javaObjectPayload, "myKey");
        // returns a byte[] based off of the message
//        byte[] data = myMessage.getRequestBytes();
//        System.out.println("Sending data to server");
//        output.write(data);
        boolean didSend = sendCSTPRequest(myMessage);
        if(!didSend)
        {
            System.out.println("Couldn't send message");
        }
        socket.shutdownOutput();
        byte[] responseBuffer = input.readAllBytes();
        for(int i = 0; i < responseBuffer.length; i++){
            System.out.print(String.format("%02x ", responseBuffer[i]));
        }
        System.out.print("\n");
        TestObject objectA = null;
        ByteArrayInputStream byteToObjectStream = new ByteArrayInputStream(responseBuffer);
        ObjectInputStream ois = new ObjectInputStream(byteToObjectStream);
        try {
            objectA = (TestObject) ois.readObject();
            System.out.println("objectA message = " + objectA.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public boolean sendCSTPRequest(CSTPMessage message){
        try {
            byte[] data = message.getRequestBytes();
            output.write(data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Socket getSocket(){
        return this.socket;
    }
}

