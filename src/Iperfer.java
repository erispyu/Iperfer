import java.io.IOException;

public class Iperfer {
    private static final String USAGE_INFO = "Error: invalid arguments\nClient Mode: java Iperfer -c -h <server hostname> -p <server port> -t <time>\nSerevr Mode: java Iperfer -s -p <listen port>\n";
    private static final int PORT_MIN = 1024;
    private static final int PORT_MAX = 65535;
    private static final int DUMMY_INT = -1;

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsageAndExit();
        }

        String runMode = args[0];

        if (runMode == "-c") {
            if (args.length != 7) {
                printUsageAndExit();
            }
            if (args[1] == "-h" && args[3] == "-p" && args[5] =="-t") {
                String serverHostname = args[2];
                if (!checkHostname(serverHostname)) {
                    System.out.println("Error: invalid hostname. Should be in IPv4 format.");
                    System.exit(-1);
                }
                int serverPort = parsePort(args[4]);
                int time = parseTime(args[5]);
                if (serverPort == DUMMY_INT || time == DUMMY_INT) {
                    System.exit(-1);
                }
                clientMode(serverHostname, serverPort, time);
            } else {
                printUsageAndExit();
            }
        }

        else if (runMode == "-s") {
            if (args.length != 3) {
                printUsageAndExit();
            }
            if (args[1] == "-p") {
                int listenPort = parsePort(args[2]);
                if (listenPort == DUMMY_INT) {
                    System.exit(-1);
                }
                serverMode(listenPort);
            } else {
                printUsageAndExit();
            }
        }

        else {
            printUsageAndExit();
        }
    }

    private static void clientMode(String serverHostname, int serverPort, int time) {
        Client client = new Client(serverHostname, serverPort, time);
        client.start();
    }

    private static void serverMode(int listenPort) {
        Server server = new Server(listenPort);
        server.start();
    }

    private static void printUsageAndExit() {
        System.out.print(USAGE_INFO);
        System.exit(-1);
    }

    private static boolean checkHostname(String ipStr) {
        try {
            if ( ipStr == null || ipStr.isEmpty() ) {
                return false;
            }

            String[] parts = ipStr.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ipStr.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private static int parsePort(String portStr) {
        try {
            int port = Integer.parseInt(portStr);
            if (port >= PORT_MIN && port <= PORT_MAX) {
                return port;
            } else {
                System.out.printf("Error: port number must be in the range %d to %d\n", PORT_MIN, PORT_MAX);
                return DUMMY_INT;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: port should be an int");
            return DUMMY_INT;
        }
    }

    private static int parseTime(String timeStr) {
        try {
            int port = Integer.parseInt(timeStr);
            if (port > 0) {
                return port;
            } else {
                System.out.println("Error: time should be positive int");
                return DUMMY_INT;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: time should be an int");
            return DUMMY_INT;
        }
    }
}
