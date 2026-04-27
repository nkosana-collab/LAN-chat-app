package org.nkosana;

import com.google.gson.Gson;

public class JsonTest {
    public static void main(String[] args) {
        Gson gson = new Gson();

        // 1. Create the object
        Message msg = new Message("Nkosana", "Hello from Maven!", "12:00 PM");

        // 2. Serialize: Object -> JSON String
        String jsonOutput = gson.toJson(msg);
        System.out.println("JSON String: " + jsonOutput);

        // 3. Deserialize: JSON String -> Object
        Message receivedMsg = gson.fromJson(jsonOutput, Message.class);
        System.out.println("Back to Object: " + receivedMsg.getSender() + " said " + receivedMsg.getContent());
    }
}
