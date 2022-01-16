import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class HTTPEcho {

    private static int BUFFERSIZE = 1024;

    public static void main( String[] args) throws IOException { 
        ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));
        byte[] fromClientBuffer = new byte[BUFFERSIZE];

            
        
        while (true) {
        
            // create a socket and accept
            Socket connectionSocket = welcomeSocket.accept();

            // Set a timer to close if enough time passes
            connectionSocket.setSoTimeout(1337);
            
             // Read from the server 
             StringBuilder serverOutput = new StringBuilder();
        
             // Help string
             String helpstring = "HTTP/1.1 200 OK\r\n\r\n";

             // append said string
             serverOutput.append(helpstring);

             int outputLength = 0;


             
            try {
                while(outputLength != -1){

                     // read to clientBuffer
                    outputLength = connectionSocket.getInputStream().read(fromClientBuffer);

                    // Data reads from server output to stringbuilder
                    String decodedOutput = new String(fromClientBuffer,0,outputLength, StandardCharsets.UTF_8);
                   
                    // append the decoded string to the stringbuilder
                    serverOutput.append(decodedOutput);
                 
                    // if the decoded string contains a new line, it means that everything has been read
                    if(decodedOutput.contains("\n")){
                        break;
                    }
                    
                    
                }
            }
            catch (Exception SocketTimeoutException) {}


            // change the stringbuilder to a string
            String output = serverOutput.toString();
            
            // encode a string into a byte array
            byte [] toClientBuffer = output.getBytes(StandardCharsets.UTF_8);

            // Use the same array when sending back, because we are echoing data
            connectionSocket.getOutputStream().write(toClientBuffer);

            connectionSocket.close();
            
            
        }
    }
}

