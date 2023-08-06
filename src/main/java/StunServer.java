import java.io.*;
import java.net.*;

public class StunServer {

    public static void main(String[] args) {
        final int port = 12345; // 服务器监听的端口号

        try {
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("Mediator Server is running and listening on port " + port);

            while (true) {
                Socket clientSocket1 = serverSocket.accept();
                System.out.println("Received connection from Client 1: " + clientSocket1.getRemoteSocketAddress());

                Socket clientSocket2 = serverSocket.accept();
                System.out.println("Received connection from Client 2: " + clientSocket2.getRemoteSocketAddress());

                // 将两个客户端的信息发送给对方
                sendClientInfo(clientSocket1, clientSocket2);
                sendClientInfo(clientSocket2, clientSocket1);

                // 启动两个线程分别处理与两个客户端的通信
                new Thread(new clientHandler(clientSocket1, clientSocket2)).start();
                new Thread(new clientHandler(clientSocket2, clientSocket1)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendClientInfo(Socket toClient, Socket otherClient) throws IOException {
        String clientInfo = otherClient.getInetAddress().getHostAddress() + ":" + otherClient.getPort();
        PrintWriter writer = new PrintWriter(toClient.getOutputStream(), true);
        writer.println(clientInfo);
    }
}

class clientHandler implements Runnable {
    private final Socket clientSocket;
    private final Socket otherClient;

    public clientHandler(Socket clientSocket, Socket otherClient) {
        this.clientSocket = clientSocket;
        this.otherClient = otherClient;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(otherClient.getOutputStream(), true);

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Received from " + clientSocket.getRemoteSocketAddress() + ": " + line);
                writer.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}





//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Properties;
//import java.util.Set;
//
//public class StunServer {
//    protected static int serverPort;
//    protected static int proxyPort;
//    protected static String proxyIp;
//    //内存缓存
//    protected static HashMap<String,HashMap<Integer,Integer>> clientList=new HashMap<String,HashMap<Integer,Integer>>();
//    private static StringBuffer lists=null;
//    private static int udp_size=500;
//    //服务器主程序
//    public static void main(){
//        try{
//            Properties p=Stupid.getPZ();
//            int p1=Integer.parseInt(p.getProperty("serverPort"));
//            int p2=Integer.parseInt(p.getProperty("port"));
//            String ip=p.getProperty("ip");
//            serverPort=p1;
//            proxyPort=p2;
//            proxyIp=ip;
//            DatagramSocket socket=new DatagramSocket(serverPort);
//            while(true){//下面是并发代码
//                DatagramPacket request=new DatagramPacket(new byte[1024],1024);
//                socket.receive(request);
//                DatagramPacket response=null;
//                String msg=null;
//                //判断是不是已有数据
//                int port=request.getPort();
//                if(clientExit(request.getAddress().getHostAddress(),request.getPort())!=-1){
//                    msg=""+clientList.get(request.getAddress().getHostAddress()).get(request.getPort());
//                    response=new DatagramPacket(msg.getBytes(),msg.getBytes().length,request.getAddress(),request.getPort());
//                    socket.send(response);
//                    continue;
//                }
//                String bin=new String(request.getData(),0,request.getLength(),"ASCII");
//                switch(bin){
//                    case "1":
//                        InetAddress i=request.getAddress();
//                        msg=i.getHostAddress()+":"+port;
//                        response=new DatagramPacket(msg.getBytes(), msg.getBytes().length,request.getAddress(),request.getPort());
//                        socket.send(response);
//                        break;
//                    case "2":
//                        //切换ip或者用udp代理，这里用代理
//                        i=InetAddress.getByName(proxyIp);
//                        response=new DatagramPacket(msg.getBytes(),msg.getBytes().length,i,proxyPort);
//                        socket.send(response);
//                        break;
//                    case "3":
//                        getLists();
//                        int j=0;
//                        while(j!=-1){
//                            byte[] bn=new byte[500];
//                            j=readString(bn,j,j+500);
//                            response=new DatagramPacket(bn,bn.length,request.getAddress(),request.getPort());
//                            socket.send(response);
//                            DatagramPacket dp=new DatagramPacket(new byte[1],1);
//                            socket.receive(dp);
//                        }
//                        byte[] mop="-1".getBytes();
//                        response=new DatagramPacket(mop,mop.length,request.getAddress(),request.getPort());
//                        socket.send(response);
//                    default:
//                        if(bin.startsWith(">>>")){
//                            bin=bin.substring(3, bin.length());
//                            String[] bs=bin.split(":");
//                            bin=">>>"+request.getAddress().getHostAddress()+":"+request.getPort();
//                            response=new DatagramPacket(bin.getBytes(),
//                                    bin.getBytes().length,InetAddress.getByName(bs[0]),Integer.parseInt(bs[1]));
//                            socket.send(response);
//                        }else if(bin.startsWith("<<<")){//存储客户端的NAT类型
//                            String ipx=request.getAddress().getHostAddress();
//                            HashMap<Integer,Integer> h1=new HashMap<Integer,Integer>();
//                            if(!clientList.containsKey(ipx)){
//                                h1.put(request.getPort(), Integer.parseInt(bin.substring(3,bin.length())));
//                                clientList.put(ipx, h1);
//                            }else{
//                                clientList.get(ipx).put(request.getPort(), Integer.parseInt(bin.substring(3,bin.length())));
//                            }
//                        }
//                }
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//    public static int clientExit(String ip,int port){
//        if(!clientList.containsKey(ip))
//            return -1;
//        HashMap<Integer,Integer> l=clientList.get(ip);
//
//        if(l.containsKey(port))
//            return l.get(port);
//
//        return -1;
//    }
//
//    public static synchronized void getLists(){
//        if(lists!=null)
//            return;
//        Set<String> ss=clientList.keySet();
//        Iterator<String> it=ss.iterator();
//        while(it.hasNext()){
//            String k=it.next();
//            HashMap<Integer,Integer> h=clientList.get(k);
//            Iterator<Integer> it1=h.keySet().iterator();
//            while(it1.hasNext()){
//                Integer i=it1.next();
//                Integer i1=h.get(i);
//                lists.append(k+":"+i+":"+i1+" ");
//            }
//        }
//    }
//
//    public static int readString(byte[] r,int si,int oi){
//        String s=lists.toString();
//        byte[] b=s.getBytes();
//        int l=b.length;
//        for(int i=si;i<=oi;i++){
//            b[i]=r[i];
//        }
//        if(oi>=l){
//            return -1;
//        }
//        return oi+1;
//    }
//}