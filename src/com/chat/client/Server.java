package com.chat.client;

import com.chat.client.model.Message;
import com.chat.client.model.User;
import com.chat.client.model.UserLoader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by vhernest on 05/11/15.
 */
public class Server implements ClientHandler.UserValidator, ClientHandler.MessageHandler, ClientHandler.ClientDisconnectListener {

    private static final String COMMAND_HISTORY = "@history";
    public static final String HISTORY_REGEX = "@history\\s*\\d*";


    private AtomicInteger counter = new AtomicInteger(0);
    private Map<String, User> users;
    private int port;
    private List<ClientHandler> handlers = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    public Server(int port) {
        this.port = port;
        users = new HashMap<>();
    }

    public void init() throws IOException {
        List<User> userList = UserLoader.loadUsers("users.csv");
        for(User user : userList) {
            users.put(user.getUsername(), user);
        }
    }

    public void listen() throws IOException {
        ServerSocket ss = new ServerSocket(port);

        while (true) {
            Socket socket = ss.accept();
            ClientHandler handler = new ClientHandler(socket, this, this);
            System.out.println("Client connected: " + handler);
            handler.setClientDisconnectListener(this);
            handlers.add(handler);
            handler.start();
        }
    }

    @Override
    public boolean isValid(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    @Override
    public void onMessage(ClientHandler handler, String message) {
        String username = handler != null ? handler.getUsername() : "@";

        if (message.matches(HISTORY_REGEX)) {
            String[] split = message.split(" ");

            try {
//                int minIDNumber = Integer.valueOf(split[1]);

                if (split.length > 1) {
                    int minIDNumber = Integer.valueOf(split[1]);
                    for(Message msg : messages) {
                        if (msg.getId() > minIDNumber) {
                            handler.send(msg);
                        }
                    }
                } else {

                    for (Message msg : messages) {
                        handler.send(msg);
                    }
                }
            } catch (NumberFormatException e) {
                handler.getPs().println("Usage: @history number");
            }

        } else {

            Message msg = new Message();
            msg.setId(counter.incrementAndGet());
            msg.setWhen(System.currentTimeMillis());
            msg.setUsername(username);
            msg.setText(message);

            messages.add(msg);

            List<ClientHandler> clone = new ArrayList<>(handlers);
            for (ClientHandler h : clone) {
                h.send(msg);
            }
        }
    }

    @Override
    public void onClientDisconnected(ClientHandler handler) {
        handlers.remove(handler);
        String msg = "Client " + handler + " disconnected!";
        System.out.println(msg);
        onMessage(null, msg);
    }
}
