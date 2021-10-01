import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    private static final int DATA_SIZE = 1000;

    /** Initialization Arguments */
    private final String address;
    private final int port;
    private final double time; // the duration in seconds for which data should be generated

    /** Initialize socket and output streams */
    private Socket socket = null;
    private DataOutputStream out = null;


    Client(String address, int port, double time) {
        this.address = address;
        this.port = port;
        this.time = time;
    }

    /**
     * Start the Client
     */
    void start() {
        try {
            // Establish a connection
            socket = new Socket(address, port);
            System.out.println("Connected to the Server");

            // Create a DataOutputStream object
            out = new DataOutputStream(socket.getOutputStream());

            // Build data of all zeros
            byte[] data = new byte[DATA_SIZE];
            int dataSentInKB = 0; // in KB
            double rate = 0; // in Mbps

            // Send out data in <this.time> seconds
            double timeInNanoSec = this.time * Math.pow(10, 9);
            double hasRunInNanoSec = 0;
            long startTimeInNanoSec, endTimeInNanoSec;
            while (hasRunInNanoSec < timeInNanoSec) {
                startTimeInNanoSec = System.nanoTime();
                // Send out 1000 bytes (1KB) per time
                out.write(data);
                endTimeInNanoSec = System.nanoTime();
                hasRunInNanoSec += (endTimeInNanoSec - startTimeInNanoSec);
                // recording
                dataSentInKB++;
            }

            // Close the connection
            System.out.println("Closing connection");
            out.close();
            socket.close();

            // Calculate and print the summary
            rate = (dataSentInKB / 1000) * 8 / this.time;
            System.out.printf("sent=%d KB rate=%f Mbps\n", dataSentInKB, rate);
        } catch(IOException u) {
            System.out.println(u);
            System.exit(1);
        }
    }
}
