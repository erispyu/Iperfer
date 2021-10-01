import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Java Program for the Server
 */
public class Server {

    private static final int DATA_SIZE = 1000;

    /** Port on which this server will run */
    private final int port;

    /** Initialize socket and input stream */
    private ServerSocket    server   = null;
    private Socket          socket   = null;
    private DataInputStream in       = null;

    public Server(int port) {
        this.port = port;
    }

    /**
     * Start the server
     */
    void start() {
        try {
            // Specify what port is the Server going to run
            server = new ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            // Prepare for receiving
            DataInputStream in = new DataInputStream(socket.getInputStream());
            byte[] data = new byte[DATA_SIZE];
            int dataReceivedInByte = 0; // in byte
            double rate = 0; // in Mbps

            // Receive data
            double hasRunInNanoSec = 0;
            long startTimeInNanoSec, endTimeInNanoSec;
            while (true) {
                startTimeInNanoSec = System.nanoTime();
                int byteReceived = in.read(data, 0, DATA_SIZE);
                endTimeInNanoSec = System.nanoTime();
                if(byteReceived == -1){
                    break;
                }
                hasRunInNanoSec += (endTimeInNanoSec - startTimeInNanoSec);
                //recording
                dataReceivedInByte += byteReceived;
            }

            // Close the connection
            System.out.println("Closing connection");
            socket.close();
            in.close();

            // Shut down the server
            server.close();

            // Calculate and print the summary
            int dataReceivedInKB = dataReceivedInByte / 1000;
            rate = (dataReceivedInKB / 1000) * 8 / hasRunInNanoSec;
            System.out.printf("received=%d KB rate=%f Mbps\n", dataReceivedInKB, rate);
        } catch(IOException i) {
            System.out.println(i);
            System.exit(1);
        }
    }
}