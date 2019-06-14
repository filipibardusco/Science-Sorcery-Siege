package com.mygdx.game;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class MainClient {

    private LinkedBlockingQueue<String> sendqueue;
    private String ip;

    public MainClient(String ip) {
        this.sendqueue = new LinkedBlockingQueue<String>();
        this.ip = ip;
    }

    public void run() {
        Scanner input = new Scanner(System.in);
        try {
            Socket connector = new Socket(); // Makes a new socket object
            connector.connect(new InetSocketAddress(ip, 3000), 5000); // Connecting to the server
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

            Thread writer = new Thread(() -> {
                while (connector.isConnected()) {
                    try{
                        out.write(this.sendqueue.take());
                        out.flush();
                    } catch (Exception e) {
                    }
                }
            });

            writer.start();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void send(String str) {
        try {
            sendqueue.put(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
