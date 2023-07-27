import java.io.*;
import java.net.*;

public class DHTServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server is running and waiting for connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                // 向客户端发送模拟的DHT ID和路由表
                String dhtId = "dht-id-123";
                String routingTable = "routing-table-data";

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(dhtId);
                out.println(routingTable);

                out.close();
                clientSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

