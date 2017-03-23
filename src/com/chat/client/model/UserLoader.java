package com.chat.client.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vhernest on 12/11/15.
 */
public class UserLoader {

    public static List<User> loadUsers(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        List<User> users = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            User user = User.parse(line);
            users.add(user);
        }

        return users;
    }

    public static List<User> loadUsers(String fileName) throws IOException {
        return loadUsers(new FileInputStream(fileName));
    }
}
