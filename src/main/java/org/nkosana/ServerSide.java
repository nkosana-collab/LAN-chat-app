package org.nkosana;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.*;

public class ServerSide {
    // Thread-safe list to keep track of all clients
    public static List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler); // Add to the registry
                threadPool.execute(handler);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Method to send a message to everyone
    public static void broadcast(Message message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) { // Don't send the message back to the sender
                client.sendMessage(message);
            }
        }
    }
}