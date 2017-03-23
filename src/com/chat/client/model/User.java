package com.chat.client.model;

import java.util.StringTokenizer;

/**
 * Created by vhernest on 12/11/15.
 */
public class User {

    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static User parse(String line) {
        String[] values = line.split(",");

        String username = values[0].trim();
        String password = values[1].trim();

        return new User(username, password);
    }

}
