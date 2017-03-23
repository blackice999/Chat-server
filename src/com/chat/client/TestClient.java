package com.chat.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by vhernest on 05/11/15.
 */
public class TestClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1024);

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        PrintStream ps = new PrintStream(out);
        ps.println("Hello World");

        System.in.read();
    }
}
