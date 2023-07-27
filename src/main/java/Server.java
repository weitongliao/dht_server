import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(12345);

            // 用于保存节点信息的Map，key为节点标识符，value为节点信息（IP和端口号）
            Map<Integer, NodeInfo> nodeMap = new HashMap<>();

            byte[] receiveData = new byte[1024];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                // 解析客户端请求信息，获取IP和端口号
                String clientIP = receivePacket.getAddress().getHostAddress();
                int clientPort = receivePacket.getPort();

                // 分配节点标识符，这里简单使用客户端的端口号作为标识符
                int nodeId = clientPort;

                // 保存节点信息到Map
                NodeInfo nodeInfo = new NodeInfo(clientIP, clientPort);
                nodeMap.put(nodeId, nodeInfo);

                // 将节点信息发送给客户端
//                sendNodeInfoToClient(nodeId, nodeInfo, serverSocket, receivePacket.getAddress(), receivePacket.getPort());

                // 如果有两个节点加入，将它们的信息互相发送
                System.out.println(nodeMap.keySet());
                if (nodeMap.size() == 2) {
                    for (int id : nodeMap.keySet()) {
                        NodeInfo otherNode = (id != nodeId) ? nodeMap.get(id) : null;
                        if (otherNode != null) {
                            sendNodeInfoToClient(id, otherNode, serverSocket, receivePacket.getAddress(), receivePacket.getPort());
                            System.out.println(otherNode);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 发送节点信息给客户端
    private static void sendNodeInfoToClient(int nodeId, NodeInfo nodeInfo, DatagramSocket serverSocket, java.net.InetAddress address, int port) {
        try {
            String response = nodeId + "," + nodeInfo.getIp() + "," + nodeInfo.getPort();
            byte[] sendData = response.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
            serverSocket.send(sendPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 节点信息类
    static class NodeInfo {
        private String ip;
        private int port;

        public NodeInfo(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        public String getIp() {
            return ip;
        }

        public int getPort() {
            return port;
        }
    }
}


//// Server.java
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class Server {
//    public static void main(String[] args) {
//        try {
//            int port = 12345;
//            ServerSocket serverSocket = new ServerSocket(port);
//            System.out.println("Server started on port " + port);
//
//            // Accept two clients
//            Socket clientSocket1 = serverSocket.accept();
//            Socket clientSocket2 = serverSocket.accept();
//
//            // Get client IP and port information
//            String client1IP = clientSocket1.getInetAddress().getHostAddress();
//            int client1Port = clientSocket1.getPort();
//            String client2IP = clientSocket2.getInetAddress().getHostAddress();
//            int client2Port = clientSocket2.getPort();
//
//            System.out.println("client1IP"+client1IP+client1Port);
//            System.out.println("client2IP"+client2IP+client2Port);
//            // Send client1 info to client2
//            System.out.println("Sending client1 info to client2...");
//            sendClientInfo(clientSocket2, client1IP, client1Port);
//
//            // Send client2 info to client1
//            System.out.println("Sending client2 info to client1...");
//            sendClientInfo(clientSocket1, client2IP, client2Port);
//
//            // Close the server
//            serverSocket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void sendClientInfo(Socket clientSocket, String clientIP, int clientPort) {
//        try {
//            String infoMessage = clientIP + "," + clientPort;
//            clientSocket.getOutputStream().write(infoMessage.getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}

//// Server.java
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Server {
//    private List<NodeConnection> nodeConnections;
//
//    public Server() {
//        nodeConnections = new ArrayList<>();
//    }
//
//    public void start(int port) {
//        try {
//            ServerSocket serverSocket = new ServerSocket(port);
//            System.out.println("Server started on port " + port);
//
//            while (true) {
//                Socket socket = serverSocket.accept();
//                NodeConnection nodeConnection = new NodeConnection(socket);
//                nodeConnections.add(nodeConnection);
//                nodeConnection.start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void closeServer() {
//        for (NodeConnection nodeConnection : nodeConnections) {
//            nodeConnection.closeConnection();
//        }
//    }
//
//    public static void main(String[] args) {
//        int port = 12345;
//        Server server = new Server();
//        server.start(port);
//    }
//}



//import java.io.*;
//import java.net.*;
//
//public class Server {
//    public static void main(String[] args) {
//        while(true){
//            try {
//                // 创建ServerSocket，并指定监听端口号
//                int port = 12345;
//                ServerSocket serverSocket = new ServerSocket(port);
//                System.out.println("Server is listening on port " + port);
//
//                // 等待客户端连接
//                Socket clientSocket = serverSocket.accept();
//                System.out.println("Client connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
//
//                // 读取客户端发送的消息
//                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                String message = in.readLine();
//                System.out.println("Client message: " + message);
//
//                // 关闭连接
//                in.close();
//                clientSocket.close();
//                serverSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
