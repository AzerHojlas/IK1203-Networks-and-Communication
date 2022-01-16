package tcpclient;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;

public class TCPClient {

    private static int BUFFERSIZE = 1024;

    
    public static String askServer(String hostname, int port, String toServer) throws IOException {

        // create a byte array that stores the output from the server
        byte [] fromServerBuffer = new byte [BUFFERSIZE];

        // If we do not send anything to the server
        if (toServer == null) 
            return askServer(hostname, port);

        // Create a socket 
        Socket clientSocket = new Socket(hostname, port);    

        // Set a timer to close if enough time passes
        clientSocket.setSoTimeout(1337);

        // convert the ToServer string into a byte array using lecture code
        byte [] toServerBuffer = (toServer + "\r\n").getBytes(StandardCharsets.UTF_8);


        // Send the encoded string toServer
        clientSocket.getOutputStream().write(toServerBuffer,0,toServerBuffer.length);

        int outputLength = 0;

        // Read from the server 
        StringBuilder serverOutput = new StringBuilder();

        try {
            while(outputLength != -1){

                // Data reads from server output to stringbuilder
                outputLength = clientSocket.getInputStream().read(fromServerBuffer);
                String decodedOutput = new String(fromServerBuffer,0,outputLength, StandardCharsets.UTF_8);
                serverOutput.append(decodedOutput);
                
                
            }
        }
        catch (Exception SocketTimeoutException) 
        {
           
        }

        String output = serverOutput.toString();
        
        // Close the socketTCPClient
        clientSocket.close();

        // return the decoded string
        return output;
    
    }

    public static String askServer(String hostname, int port) throws IOException {

        // create a byte array that stores the output from the server
        byte [] fromServerBuffer = new byte [BUFFERSIZE];

        // Create a socket 
        Socket clientSocket = new Socket(hostname, port);    

        // Set a timer to close if enough time passes
        clientSocket.setSoTimeout(1337);

        // read the server output to my byte array create int for length
        int outputLength = 0;

        // Read from the server 
        StringBuilder serverOutput = new StringBuilder();

        

        try {
            while(outputLength != -1){

                // Lägger in data från server till stringbuilder
                String decodedOutput = new String(fromServerBuffer,0,outputLength, StandardCharsets.UTF_8);
                serverOutput.append(decodedOutput);
                outputLength = clientSocket.getInputStream().read(fromServerBuffer);

                
            }
        }
        catch (Exception SocketTimeoutException) 
        {
            
        }

        String output = serverOutput.toString();
        
        // Close the opened socket
        clientSocket.close();

        // return the decoded string
        return output;
        

    }

}

