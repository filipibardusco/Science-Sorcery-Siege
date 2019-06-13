package com.mygdx.game;

import sun.awt.image.ImageWatched;
import sun.font.TrueTypeFont;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.net.InetAddress;

public class MainServer {

    public static void main(String[] args) {

        byte[] address = new byte[]{127,0,0,1};
        try {
            ServerSocket server = new ServerSocket(3000, 10, InetAddress.getByAddress(address));

            Socket client = server.accept(); // Accepting the player connection
            System.out.println("Connection established");
            clientConnection player = new clientConnection(client); // Creating a new thread and starting it

            Scanner stdin = new Scanner(System.in);

            while (player.clientSocket.isConnected()){
                try {
                    player.sendQueue.put(stdin.nextLine());
                } catch (InterruptedException e) {

                }
            }
        } catch (IOException e) {
            System.exit(9);
        }
    }
}


class clientConnection{

    Socket clientSocket;
    PrintWriter out;
    BufferedReader in;
    LinkedBlockingQueue<String> sendQueue = new LinkedBlockingQueue<>();
    Thread writer;
    Thread reader;

    clientConnection(Socket client) {
        try {
            this.clientSocket = client;
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.writer = new Thread(() -> {
                while (clientSocket.isConnected()) {
                    try{
                        String message = sendQueue.take();
                        out.write(message+"\n");
                        out.flush();
                    } catch (Exception e) {
                    }
                }
            });

            this.writer.start();

            this.reader = new Thread(() -> {
                String temp;
                while (clientSocket.isConnected()) {
                    try{
                        temp = in.readLine();
                        System.out.println(temp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            this.reader.start();

        } catch (Exception e) {
        }
    }
}
