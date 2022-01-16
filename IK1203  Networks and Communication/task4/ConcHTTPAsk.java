import java.net.*;
import java.io.*;
import tcpclient.TCPClient;
import java.nio.charset.StandardCharsets;

public class ConcHTTPAsk {

    public static void main( String[] args) throws IOException {

        ServerSocket httpSocket = new ServerSocket(Integer.parseInt(args[0]));

        while(true){

            // create a socket and accept
            Socket connectionSocket = httpSocket.accept();

            // create a runnable object
            MyRunnable newClient = new MyRunnable(connectionSocket);
            Thread newClientThread = new Thread (newClient);
            newClientThread.start();
        }
    
    }
    
}

