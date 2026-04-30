package org.nkosana.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.Gson;
import org.nkosana.tools.Message;

public class Handler implements Runnable {
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;

    Gson gson = new Gson();

    public Handler(Socket clientSocket) {
        this.client = clientSocket;
    }

    // This allows other threads to send a message TO this specific client
    public void sendMessage(Message message) {
        // 2. Serialize: Object -> JSON String
        String jsonOutput = gson.toJson(message);
        out.println(jsonOutput);
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);

            String line;
            while ((line = in.readLine()) != null) {



                // Deserialize: JSON String -> Object
                Message receivedMsg = gson.fromJson(line, Message.class);

                System.out.println("Broadcasting: " + receivedMsg.getSender());
                Server.broadcast(receivedMsg, this); // Send to everyone else!



            }
        } catch (IOException e) {
            System.out.println("A client disconnected.");
        } finally {
            Server.clients.remove(this); // Clean up
            try { client.close(); } catch (IOException e) {}
        }
    }
}