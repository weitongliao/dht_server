
import java.io.*;
import java.net.*;

public class ChatServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server is running and waiting for connections...");

            Socket clientSocket1 = serverSocket.accept();
            System.out.println("Client 1 connected: " + clientSocket1.getInetAddress() + ":" + clientSocket1.getPort());

            Socket clientSocket2 = serverSocket.accept();
            System.out.println("Client 2 connected: " + clientSocket2.getInetAddress() + ":" + clientSocket2.getPort());

            // 创建并启动两个线程用于处理客户端的消息
            ClientHandler clientHandler1 = new ClientHandler(clientSocket1, clientSocket2);
            ClientHandler clientHandler2 = new ClientHandler(clientSocket2, clientSocket1);
            clientHandler1.start();
            clientHandler2.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

