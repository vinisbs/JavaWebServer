package org.academiadecodigo;

import java.io.*;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

//Multi-threaded simple web server implementation based on AC web server single-threaded implementation

public class WebServer {

    private static final Logger logger = Logger.getLogger(WebServer.class.getName());

    private static final int DEFAULT_PORT = 8085;

    private ServerSocket bindSocket = null;

    public WebServer() {

        setupServerSocket();
    }

    public static void main(String[] args) {

        try {

            ExecutorService clientExecutor = Executors.newCachedThreadPool();

            WebServer webServer = new WebServer(); // Instantiates new web server

            // Server will always be listening and will create a new Thread per each request
            while (true) {

                webServer.bindSocket.accept(); // Blocking method, will create new Thread if bindSocket receives a connection

                clientExecutor.submit(new Listen(webServer.bindSocket));
            }

        } catch (NumberFormatException e) {

            System.err.println("Usage: WebServer [PORT]");

            System.exit(1);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void setupServerSocket() {

        try {

            bindSocket = new ServerSocket(DEFAULT_PORT);

            logger.log(Level.INFO, "server bind to " + getAddress(bindSocket));

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private String getAddress(ServerSocket socket) {

        if (socket == null) {

            return null;
        }

        return socket.getInetAddress().getHostAddress() + ":" + socket.getLocalPort();
    }
}
