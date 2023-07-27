import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private Socket targetSocket;

    public ClientHandler(Socket clientSocket, Socket targetSocket) {
        this.clientSocket = clientSocket;
        this.targetSocket = targetSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(targetSocket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + ": " + message);
                out.println("Client " + clientSocket.getPort() + ": " + message);
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

