package com.domgallo;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Client c = null;
        try {
            c = new Client("localhost", 4423);

            c.getSocket().close();
        } catch ( IOException e){
            System.out.println(e);
        }
    }
}
