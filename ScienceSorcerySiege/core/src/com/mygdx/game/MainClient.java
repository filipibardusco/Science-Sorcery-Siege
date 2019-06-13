package com.mygdx.game;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.Scanner;

public class MainClient {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        try {
            Socket connector = new Socket(); // Makes a new socket object
            connector.connect(new InetSocketAddress("127.0.0.1", 3000), 5000); // Connecting to the server
            System.out.println("Connected");
            String text;
            PrintWriter out = new PrintWriter(connector.getOutputStream(), true); // Creates a writer to decode the inbound data
            BufferedReader in = new BufferedReader(new InputStreamReader(connector.getInputStream())); // Create a buffer reader to write outbound data
            Thread reader = new Thread(() -> {
                String temp;
                while (connector.isConnected()) {
                    try{
                        temp = in.readLine();
                        System.out.println(temp);
                    } catch (Exception e) {
                    }
                }
            });

            reader.start();

            while (true) {
                text = input.nextLine();
                out.write(text+"\n"); // Sends the data \n is needed because the buffer reader on the server side reads until a new line character
                out.flush();
            }
        } catch (IOException e){

        }

    }
}
