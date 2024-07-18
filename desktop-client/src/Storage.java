package src;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class Storage {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    protected static void saveUsers(ArrayList<User> users) {
        String json = gson.toJson(users);
        try {
            Files.write(Paths.get("users.json"), json.getBytes());
        } catch (IOException e) {
            System.out.println("[ERROR] Error while saving users to file");
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    protected static ArrayList<User> loadUsers() {
        try {
            Path path = Paths.get("users.json");
            if (!Files.exists(path)) {
                return new ArrayList<>();
            }
            String json = new String(Files.readAllBytes(path));
            User[] userArray = gson.fromJson(json, User[].class);
            ArrayList<User> users = new ArrayList<>();
            Collections.addAll(users, userArray);
            return users;
        } catch (IOException e) {
            System.out.println("[ERROR] Error while reading users from file");
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    protected static void loadMailer() {

        // load and set mailer credentials from email.conf
        try {
            Path path = Paths.get("email.conf");
            if (!Files.exists(path)) {
                System.out.println("[ERROR] email.conf does not exist");
                return;
            }
            String[] lines = new String(Files.readAllBytes(path)).split("\n");

            if (lines.length != 6) {
                System.out.println("[ERROR] email.conf is not properly formatted");
                return;
            }

            // remove any \r
            for (int i = 0; i < lines.length; i++) {
                lines[i] = lines[i].replace("\r", "");
            }
            Mailer.setUsername(lines[0]);
            Mailer.setPassword(lines[1]);
            Mailer.setSmtpServer(lines[2]);
            Mailer.setPort(lines[3]);
            Mailer.setSmtpAuth(lines[4]);
            Mailer.setStarttls(lines[5]);
        } catch (IOException e) {
            System.out.println("[ERROR] Error while reading email.conf");
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }
}
