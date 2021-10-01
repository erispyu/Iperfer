import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    /** IP Address and port of the Server */
    private final String address;
    private final int port;

    /** Initialize socket and output streams */
    private Socket socket = null;
    private PrintWriter out = null;


    Client(String address, int port, int time) {
        this.address = address;
        this.port = port;
    }

    /**
     * Start the Client
     */
    void start() {
        try {
            // Establish a connection
            socket = new Socket(address, port);
            System.out.println("Connected to the Server");

            // Create a writer object
            out = new PrintWriter(socket.getOutputStream(), true);

            // Take the message from the user
            System.out.println("Type your message below ..");
            Scanner scr = new Scanner(System.in);
            String line = scr.nextLine();

            // Send the message
            out.println(line);
            System.out.println("Message sent");

            // Close the connection
            System.out.println("Closing connection");
            out.close();
            socket.close();
        } catch(IOException u) {
            System.out.println(u);
            System.exit(1);
        }
    }

    private void printSummary() {

    }
}
