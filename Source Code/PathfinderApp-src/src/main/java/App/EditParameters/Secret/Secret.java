package App.EditParameters.Secret;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Secret {
    // Format:
    // username
    // password
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // https://stackoverflow.com/questions/15749192/how-do-i-load-a-file-from-resource-folder
    public void loadSecret() {
        List<String> strings;
        try {
            strings = Files.readAllLines(Paths.get("res/secret.txt"));
        }
        catch (IOException e) {
            System.err.println("Failed to read secret file");
            return;
        }

        username = strings.get(0);
        password = strings.get(1);
    }
}
