package org.nkosana;


import java.io.*;
import java.net.*;
import java.util.Scanner;
import com.google.gson.Gson;
import org.nkosana.tools.Message;

public class Client2 {
    public static void main(String[] args) {

        Gson gson = new Gson();
        try {
            Socket socket = new Socket("127.0.0.1", 5000);
            System.out.println("Connected to the chat server!");

            // 1. Start a background thread to "Listen" (The Ear)
            new Thread(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {

                        // Deserialize: JSON String -> Object
                        Message receivedMsg = gson.fromJson(serverMessage, Message.class);


                        System.out.println("\nIncoming: " + receivedMsg.getContent());
                        System.out.print("You: "); // Keep the prompt visible
                    }
                } catch (IOException e) {
                    System.out.println("Connection to server lost.");
                }
            }).start();

            // 2. Main thread handles "Talking" (The Mouth)
            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    System.out.print("You2: ");
                    String userInput = scanner.nextLine();
                    Message reply = new Message("Client:2", userInput, "12:00 PM");

                    //  Serialize: Object -> JSON String
                    String jsonOutput = gson.toJson(reply);

                    out.println(jsonOutput);

                    if (userInput.equalsIgnoreCase("exit")) break;
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
