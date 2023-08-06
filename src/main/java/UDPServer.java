import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {
    public static void main(String[] args) {
        final int PORT = 12345;
        try {
            DatagramSocket serverSocket = new DatagramSocket(PORT);

            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            System.out.println("aaaa");
            // 接收第一个客户端的信息
            serverSocket.receive(receivePacket);
            String client1Message = new String(receivePacket.getData(), 0, receivePacket.getLength());
            InetAddress client1Address = receivePacket.getAddress();
            int client1Port = receivePacket.getPort();

            System.out.println("Received message from Client 1: " + client1Message);

            System.out.println(client1Address+":"+client1Port);

            // 接收第二个客户端的信息
            serverSocket.receive(receivePacket);
            String client2Message = new String(receivePacket.getData(), 0, receivePacket.getLength());
            InetAddress client2Address = receivePacket.getAddress();
            int client2Port = receivePacket.getPort();

            System.out.println("Received message from Client 2: " + client2Message);

            // 发送IP信息给第一个客户端
            String ipInfo = "Your IP: " + client2Address.getHostAddress() + ", Your Port: " + client2Port;
            byte[] sendData = ipInfo.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, client1Address, client1Port);
            serverSocket.send(sendPacket);

            // 发送IP信息给第二个客户端
            ipInfo = "Your IP: " + client1Address.getHostAddress() + ", Your Port: " + client1Port;
            sendData = ipInfo.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, client2Address, client2Port);
            serverSocket.send(sendPacket);

            // 关闭服务器Socket
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
