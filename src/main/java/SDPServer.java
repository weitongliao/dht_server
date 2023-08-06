import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SDPServer {
    private static final int PORT = 12345;
    private static final Map<String, String> sdpMap = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started, listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress().getHostAddress());

                // Handle client in a separate thread
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

                StringBuilder content = new StringBuilder();
                String line;
                while (true) {
                    line = reader.readLine();
                    if(Objects.equals(line, "")){
                        break;
                    }
//                    System.out.println("line"+line);
                    content.append(line).append("\n");
//                    System.out.println(content);
                }
//                System.out.println(content);

                String sdpDescription = content.toString();
//                System.out.println("SDP Description:");
//                System.out.println(sdpDescription);
                System.out.println("Received SDP description from client: " + sdpDescription);

                // Store the SDP description in the map
                System.out.println(socket.getInetAddress().getHostAddress());
                sdpMap.put(socket.getInetAddress().getHostAddress(), sdpDescription);

                while (true) {
                String request = reader.readLine();
                if (request.equals("GET_SDP")) {
                    String remoteIP = reader.readLine();
                    String remoteSDP = sdpMap.get(remoteIP);

                    // Send back the remote SDP description to the client
                    writer.println(remoteSDP);
                }
            }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    static class SendClientInfo extends Thread{
//        SendClientInfo(){
//
//        }
//
//        @Override
//        public void run() {
//            // Wait for incoming requests from other clients
//            while (true) {
//                String request = reader.readLine();
//                if (request.equals("GET_SDP")) {
//                    String remoteIP = reader.readLine();
//                    String remoteSDP = sdpMap.get(remoteIP);
//
//                    // Send back the remote SDP description to the client
//                    writer.println(remoteSDP);
//                }
//            }
//        }
//    }
}
