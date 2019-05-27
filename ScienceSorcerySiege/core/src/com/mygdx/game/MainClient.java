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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.net.InetAddress;

public class MainClient {

    public static void main(String[] args) {

        byte[] address = new byte[]{127,0,0,1};
        try {
            ServerSocket server = new ServerSocket(3000, 10, InetAddress.getByAddress(address));

            LinkedBlockingQueue<LinkedBlockingQueue> WaitingPlayers = new LinkedBlockingQueue<LinkedBlockingQueue>(); // Creates a object to store the current players. Blocking is needed due the need for threading

            while (true) {
                Socket client = server.accept(); // Accepting the player connection
                System.out.println("Connection established");
                (new clientProcessor(client, WaitingPlayers)).start(); // Creating a new thread and starting it
            }


        } catch (IOException e) {
            System.exit(9);
        }
    }
}


class clientProcessor extends Thread{

    Socket clientSocket;
    PrintWriter out;
    BufferedReader in;
    LinkedBlockingQueue<String> communicate;
    LinkedBlockingQueue<LinkedBlockingQueue> globalPlayers;
    String enemy;
    String username;

    clientProcessor(Socket client, LinkedBlockingQueue<LinkedBlockingQueue> players) {
        try {
            this.clientSocket = client;
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String[] message = in.readLine().split(" ");
            this.username = message[1];
            if (message[0].equals("random")) {
                if (players.peek() != null){
                    communicate = players.take();
                    this.enemy = communicate.take();

                } else {
                    communicate = new LinkedBlockingQueue<>();
                    communicate.add(message[1]);
                    players.add(communicate);
                    out.write("Waiting for players");
                    out.flush();

                    while (true) {
                        if (!communicate.peek().equals(message[1]) && communicate.peek() != null){
                            this.enemy = communicate.take();
                            break;
                        }

                        try {
                            Thread.sleep(500);
                        }catch(InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }

                    }
                }
            }


        } catch (Exception e) {}
    }

    public void run() {
        String temp;
        while (clientSocket.isConnected()) {
            try{
                temp = in.readLine();
                System.out.println(temp);
            } catch (IOException e) {
            }
        }

    }
}
