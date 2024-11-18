package org.example;

import org.example.impl.BIOServerImpl;

/**
 * 
 * 
 * @author chenqian
 * @date 2024/11/14 16:03
 **/public class Main {
     public static void main(String[] args) {
        Server server = new BIOServerImpl();
        server.start();
    }
}
