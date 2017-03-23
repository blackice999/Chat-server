package com.chat.client;

import com.chat.client.model.Message;

import java.io.*;
import java.net.Socket;

/**
 * Created by vhernest on 19/11/15.
 */
public class ClientHandler extends Thread {

    public static final String MSG_SIGN_IN_OK = "OK";
    public static final String MSG_SIGN_IN_FAIL = "FAIL";

    private Socket socket;
    private BufferedReader reader;
    private PrintStream ps;
    private UserValidator validator;
    private MessageHandler messageHandler;
    private ClientDisconnectListener clientDisconnectListener;
    private String username;

    public ClientHandler(Socket socket, UserValidator validator, MessageHandler messageHandler) throws IOException {
        this.socket = socket;
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        reader = new BufferedReader(new InputStreamReader(in));
        ps = new PrintStream(out);
        this.validator = validator;
        this.messageHandler = messageHandler;
    }

    public ClientDisconnectListener getClientDisconnectListener() {
        return clientDisconnectListener;
    }

    public void setClientDisconnectListener(ClientDisconnectListener clientDisconnectListener) {
        this.clientDisconnectListener = clientDisconnectListener;
    }

    public synchronized void send(Message message) {
        ps.println(message.getId());
        ps.println(message.getWhen());
        ps.println(message.getUsername());
        ps.println(message.getText());
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void run() {
        try {
            username = reader.readLine();
            String password = reader.readLine();

            if (validator.isValid(username, password)) {
                ps.println(MSG_SIGN_IN_OK);

                String line;
                while ((line = reader.readLine()) != null) {
                    messageHandler.onMessage(this, line);
                    System.out.println(username + ": " + line);
                }
            } else {
                ps.println(MSG_SIGN_IN_FAIL);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
        if (clientDisconnectListener != null) {
            clientDisconnectListener.onClientDisconnected(this);
        }
    }

    public PrintStream getPs() {
        return ps;
    }

    interface UserValidator {
        boolean isValid(String username, String password);
    }

    interface MessageHandler {
        void onMessage(ClientHandler handler, String message);
    }

    interface ClientDisconnectListener {
        void onClientDisconnected(ClientHandler handler);
    }

    @Override
    public String toString() {
        return socket.getInetAddress().toString() + "/" + username;
    }
}
