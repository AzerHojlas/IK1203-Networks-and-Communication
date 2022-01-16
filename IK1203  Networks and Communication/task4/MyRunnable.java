import java.net.*;
import java.io.*;
import tcpclient.TCPClient;
import java.nio.charset.StandardCharsets;

public class MyRunnable implements Runnable {

    private static int BUFFERSIZE = 1024;

    Socket connectionSocket;

    public MyRunnable(Socket connectionSocket) {

        this.connectionSocket = connectionSocket;
    }

    public void run() {

        try {
            // Create a buffer
            byte[] fromClientBuffer = new byte[BUFFERSIZE];

            while (true) {

                // Set a timer to close if enough time passes
                connectionSocket.setSoTimeout(15000);

                // Read from the server
                StringBuilder serverOutput = new StringBuilder();

                // Help string
                String helpstring = "HTTP/1.1 200 OK\r\n\r\n";

                int outputLength = 0;

                try {
                    while (outputLength != -1) {

                        // read to clientBuffer
                        outputLength = connectionSocket.getInputStream().read(fromClientBuffer);

                        // Data reads from server output to stringbuilder
                        String decodedOutput = new String(fromClientBuffer, 0, outputLength, StandardCharsets.UTF_8);

                        // append the decoded string to the stringbuilder
                        serverOutput.append(decodedOutput);

                        // if the decoded string contains a new line, it means that everything has been
                        // read
                        if (decodedOutput.contains("\n")) {
                            break;
                        }

                    }
                } catch (Exception SocketTimeoutException) {
                }

                // change the stringbuilder to a string
                String request = serverOutput.toString();

                String hostname = null;

                String port = null;

                String toServer = null;

                // divide the arguments into parts
                String[] toClientArguments = request.split("[\\t\\n\\r?&= ]+");

                hostname = getHostName(toClientArguments);

                port = getPort(toClientArguments);

                toServer = getToServer(toClientArguments);

                // this stringbuilder adds the necessary http line
                StringBuilder helper = new StringBuilder();

                // appends the necessary code
                // helper.append(helpstring);

                // test code 400
                if (checker400(hostname, port, toClientArguments) != null)
                    helper.append(checker400(hostname, port, toClientArguments));

                else if (checker404(hostname, port, toClientArguments) != null)
                    helper.append(checker404(hostname, port, toClientArguments));
                else {
                    try {
                        String clientAnswer = TCPClient.askServer(hostname, Integer.parseInt(port), toServer);
                        helper.append(helpstring);
                        helper.append(clientAnswer);
                    }

                    catch (IOException e) {
                        helper.append("HTTP/1.1 404 Not Found\r\n");
                    }

                }

                // encode a string into a byte array
                byte[] toClientBuffer = helper.toString().getBytes(StandardCharsets.UTF_8);

                // Use the same array when sending back, because we are sending data
                connectionSocket.getOutputStream().write(toClientBuffer);

                connectionSocket.close(); // tab this
            }
        }

        catch (IOException e) {}
    }

    public static String getHostName(String[] toClientArguments) {

        String hostname = null;

        for (int i = 0; i < toClientArguments.length; i++)
            if (toClientArguments[i].equals("hostname"))
                hostname = toClientArguments[i + 1];

        return hostname;
    }

    public static String getPort(String[] toClientArguments) {

        String port = null;

        for (int i = 0; i < toClientArguments.length; i++)
            if (toClientArguments[i].equals("port"))
                port = toClientArguments[i + 1];

        return port;
    }

    public static String getToServer(String[] toClientArguments) {

        String toServer = null;

        for (int i = 0; i < toClientArguments.length; i++)
            if (toClientArguments[i].equals("string"))
                toServer = toClientArguments[i + 1];

        return toServer;
    }

    public static String checker400(String hostname, String port, String[] toClientArguments) {

        String http400 = "HTTP/1.1 400 Bad Request\r\n";

        if (hostname == null || port == null)
            return http400;

        for (int i = 0; i < toClientArguments.length; i++) {

            if (toClientArguments[i].equals("HTTP/1.1"))
                break;
            else if (i == (toClientArguments.length - 1))
                return http400;
        }

        for (int i = 0; i < toClientArguments.length; i++) {

            if (toClientArguments[i].equals("Host:")) {
                break;
            } else if (i == (toClientArguments.length - 1)) {
                return http400;
            }
        }

        for (int i = 0; i < toClientArguments.length; i++) {

            if (toClientArguments[i].equals("GET")) {
                break;
            } else if (i == (toClientArguments.length - 1)) {
                return http400;
            }
        }

        for (int i = 0; i < toClientArguments.length; i++) {

            if (toClientArguments[i].equals("hostname")) {
                break;
            } else if (i == (toClientArguments.length - 1)) {
                return http400;
            }
        }

        for (int i = 0; i < toClientArguments.length; i++) {

            if (toClientArguments[i].equals("port")) {
                break;
            } else if (i == (toClientArguments.length - 1)) {
                return http400;
            }
        }

        return null;
    }

    public static String checker404(String hostname, String port, String[] toClientArguments) {

        String http404 = "HTTP/1.1 404 Not Found\r\n";

        for (int i = 0; i < toClientArguments.length; i++) {

            if (toClientArguments[i].equals("/ask")) {
                break;
            } else if (i == (toClientArguments.length - 1)) {
                return http404;
            }
        }
        return null;
    }

}

