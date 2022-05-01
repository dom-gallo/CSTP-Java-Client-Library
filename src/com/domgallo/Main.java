package com.domgallo;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	// write your code here
        try {
            Client c = new Client("localhost", 4423);
        } catch ( IOException e){
            System.out.println(e);
        }


    }
}
