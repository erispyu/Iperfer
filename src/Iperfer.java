public class Iperfer {
    private static final String USAGE_INFO = "Error: invalid arguments\nClient Mode: java Iperfer -c -h <server hostname> -p <server port> -t <time>\nSerevr Mode: java Iperfer -s -p <listen port>\n";
    private static final int PORT_MIN = 1024;
    private static final int PORT_MAX = 65535;
    private static final int DUMMY_PORT = -1;
    private static final double DUMMY_TIME = -1;

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsageAndExit();
        }

        String runMode = args[0];

        if (runMode.equals("-c")) {
            if (args.length != 7) {
                printUsageAndExit();
            }
            if (args[1].equals("-h") && args[3].equals("-p") && args[5].equals("-t")) {
                String serverHostname = args[2];
                if (!checkHostname(serverHostname)) {
                    System.out.println("Error: invalid hostname. Should be in IPv4 format.");
                    System.exit(-1);
                }
                int serverPort = parsePort(args[4]);
                double time = parseTime(args[5]);
                if (serverPort == DUMMY_PORT || time == DUMMY_TIME) {
                    System.exit(-1);
                }
                clientMode(serverHostname, serverPort, time);
            } else {
                printUsageAndExit();
            }
        }

        else if (runMode.equals("-s")) {
            if (args.length != 3) {
                printUsageAndExit();
            }
            if (args[1].equals("-p")) {
                int listenPort = parsePort(args[2]);
                if (listenPort == DUMMY_PORT) {
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

    private static void clientMode(String serverHostname, int serverPort, double time) {
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
                return DUMMY_PORT;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: port should be an integer");
            return DUMMY_PORT;
        }
    }

    private static double parseTime(String timeStr) {
        try {
            double time = Double.parseDouble(timeStr);
            if (time > 0) {
                return time;
            } else {
                System.out.println("Error: time should be a positive number");
                return DUMMY_TIME;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: time should be a positive number");
            return DUMMY_TIME;
        }
    }
}
