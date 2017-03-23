package com.chat.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vhernest on 05/11/15.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Server server = new Server(1024);
        server.init();
        server.listen();

//        List<Integer> list = new ArrayList<>();
//
//        for (int i = 0; i < 100; i++) {
//            list.add(i);
//        }
//
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                list.add(1000);
//            }
//        }.start();
//
//        for (int e : list) {
//            System.out.println(e);
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e1) {
//                e1.printStackTrace();
//            }
//        }
    }

}
